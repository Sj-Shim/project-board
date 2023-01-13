package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.repository.ArticleRepository;
import com.bitstudy.app.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

/** 기능 관련 로직을 담을 클래스 = Service*/
@Slf4j
@Service
@RequiredArgsConstructor //필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 어노테이션
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository; //아티클 서비스이므로 해당 레포지토리 필요
    private final UserAccountRepository userAccountRepository; //유저 정보 이용
    /** 검색용*/
    @Transactional(readOnly = true) //트랜잭션 읽기전용 설정. 실수로 커밋해도 flush되지않아서 엔티티가 등록,수정,삭제되지 않음
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
//        return Page.empty();
        /** blackKeyword 부분 실제 구현
         * When 부분에서 searchArticles의 메서드를 이용해서 값들을 전달함.
         * 검색 관련부분이므로 검색어인 searchKeyword를 이용해서 코드 짤 것임*/
        /* 검색어 없는 경우 */
        if(searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDto::from);
            //Page 클래스 맨 아래 map() 이 있음. 이건 Page 안의 내용물을 형변환해서 다시 페이지로 새로 만들어 줌.
            //(map()이 article을 articleDto로 바꿔서 리턴해 줌)
        }
        /* 검색어 있는 경우 */
        /** searchType은 enum(열거형)으로 되어있음. enum을 주제로 각각의 쿼리 만들 것*/
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContainingIgnoreCase(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContainingIgnoreCase(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword, pageable)
                    .map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtagContaining(searchKeyword, pageable)
                    .map(ArticleDto::from);

        };
        /* Page : 전체 데이터 건수 조회 (데이터를 다 가지고 있다)
                getTotalElements() : 개수 뽑기
                getTotalPages() : 별도의 size를 줘서 총 페이지 개수 구하기
                getNumber() : 현재 페이지의 번호를 뽑아줌
        * Pageable : 페이징 기능(JPA에서 DB쿼리 날릴 때 limit를 날려서 데이터 가져옴)*/

    }

    @Transactional(readOnly = true)
    public ArticleDto getArticle(Long articleId){
        return articleRepository.findById(articleId)
                .map(ArticleDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticleWithComments(Long articleId){
        return articleRepository.findById(articleId) //articleId로 게시글 하나 포착
                .map(ArticleWithCommentsDto::from) //포착한 게시글을 통해 코멘트Dto map으로 변환
                .orElseThrow(()-> new EntityNotFoundException("해당 게시글이 없습니다. articleId - " + articleId));
    }

    public void saveArticle(ArticleDto dto){ // db에 저장할 뿐 가져올 정보는 없어서 void
        UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
        articleRepository.save(dto.toEntity(userAccount));
        //toEntity를 이용해서 매개변수로 받은 dto(단순히 데이터를 가지고 있을 뿐, 누가 어떤 정보인지 모르는 상태)정보로부터 엔티티를 하나 만들어서 세이브 하는 코드
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id());
            UserAccount userAccount = userAccountRepository.getReferenceById(dto.userAccountDto().userId());
            if(article.getUserAccount().equals(userAccount)) {

                if (dto.title() != null) {
                    article.setTitle(dto.title());
                }
                if (dto.content() != null) {
                    article.setContent(dto.content());
                }
                article.setHashtag(dto.hashtag());//hashtag는 null가능이므로 if로 검사할 필요 없음
            }

        } catch(EntityNotFoundException e){
            // 수정 정보를 넣었을 때 게시글이 없어진 경우
            log.warn("게시글 업데이트 실패. 게시글을 찾을 수 없습니다.");
        }
    }
    /** 게시글 삭제 */
    public void deleteArticle(Long articleId, String userId){
        articleRepository.deleteByIdAndUserAccount_UserId(articleId, userId);
    }

    /** 게시글 개수 구하기 - 마지막 글일 경우 '다음' 버튼 비활성화 시키기 위함*/
    public long getArticleCount() {
        return articleRepository.count();
    }
}
