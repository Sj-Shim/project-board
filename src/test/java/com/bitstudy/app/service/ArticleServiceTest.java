package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleRepository;
import com.bitstudy.app.repository.UserAccountRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/** 서비스 비즈니스 로직 테스트
 * (슬라이스 테스트 기능 없이)
 * 스프링 부트 어플리케이션 컨텍스트가 뜨는 데 걸리는 시간을 없애기 위함
 * 디펜던시가 추가되야 하는 부분은 Mocking하는 방식으로 진행.
 * 주로 사용되는 라이브러리 : mockito(스프링 테스트 패키지에 내장)*/
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    /* Mock을 주입하는 거에(테스트 대상) @InjectionMocks 달기. 그 외의 것들(의존성 대상)에겐 @Mock 달기*/
    @InjectMocks
    private ArticleService sut; //sut - system under test. 테스트시 자주 사용되는 이름. 테스트 대상이라는 뜻

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserAccountRepository userAccountRepository;


    /** 테스트 할 기능
     * 1. 검색
     * 2. 각 게시글 선택 시 해당 게시글 상세 페이지 이동
     * 3. 페이지네이션*/

    /* 검색 */
    @Test
    @DisplayName("검색 테스트 - case1) 검색어 없이 검색버튼>게시글리스트 반환")
    public void givenBlankKeyword_whenGetSearch_thenReturnArticlesList () {
        //Given : 페이지 기능
        Pageable pageable = Pageable.ofSize(20); //한 페이지당 얼마나 가져올건지
        given(articleRepository.findAll(pageable)).willReturn(Page.empty());
        //When : 케이스. 입력이 없을 때(실제 테스트 내용)
        Page<ArticleDto> articles = sut.searchArticles(null, null, pageable);
        //Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findAll(pageable);
    }

    @Test
    @DisplayName("검색 테스트 - case2) 검색어 넣고 검색> 검색결과 반환")
    public void givenKeyword_whenGetSearch_thenReturnArticlesList() {
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchKeyword = "title";
        Pageable pageable = Pageable.ofSize(20);
        given(articleRepository.findByTitleContainingIgnoreCase(searchKeyword, pageable)).willReturn(Page.empty());
        //When
        Page<ArticleDto> articles = sut.searchArticles(searchType, searchKeyword, pageable);
        //Then
        assertThat(articles).isEmpty();
        then(articleRepository).should().findByTitleContainingIgnoreCase(searchKeyword, pageable);
    }

    /* 게시글 선택*/
    @Test
    @DisplayName("게시글 선택 - 게시글 반환")
    public void givenArticle_whenSelectArticle_thenReturnArticleOne() {
        //Given
        Article article = createArticle();
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        //When
//        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);
        ArticleDto dto = sut.getArticle(articleId);
        //Then
        assertThat(dto)
                .hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }
    @DisplayName("게시글 수 조회, 게시글 수 반환")
    @Test
    public void givenNothing_thenReturnArticleCount() {
        //Given
        long expected = 0l;
        given(articleRepository.count()).willReturn(expected);
        //When
        long actual = sut.getArticleCount();
        //Then
        assertThat(actual).isEqualTo(expected);
        then(articleRepository).should().count();
    }
    /* 게시글 생성 */
    @DisplayName("게시글 정보 입력, 게시글 생성")
    @Test
    public void givenArticleInfo_whenCreateArticle() {
        //Given
        ArticleDto dto = createArticleDto();
        given(articleRepository.save(any(Article.class))).willReturn(createArticle());
        //When
        sut.saveArticle(dto);
        //Then
        then(articleRepository).should().save(any(Article.class));
    }

    /* 게시글 수정 */
    @DisplayName("게시글 정보 입력, 게시글 수정")
    @Test
    public void givenArticleInfo_whenUpdateArticle() {
        //Given
        Long articleId = 1l;
        ArticleDto dto = createArticleDto("title", "content", "#java");
        Article article = createArticle();
        given(articleRepository.getReferenceById(anyLong())).willThrow(EntityNotFoundException.class);
        // record는 별도의 getter를 만들지 않아도 됨. 일반 필드처럼 가져다 쓰면 됨.
        //When
        sut.updateArticle(anyLong(), dto);
        //Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("title", dto.title())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("hashtag", dto.hashtag());
        then(articleRepository).should().getReferenceById(anyLong());
    }

    /* 게시글 삭제 */
    @DisplayName("게시글 아이디 입력, 게시글 삭제")
    @Test
    public void givenArticleId_whenDeleteArticle() {
        //Given
        Long articleId = 1L;
        String userId = "bitstudy";
        willDoNothing().given(articleRepository).deleteById(articleId);
        //When
        sut.deleteArticle(articleId, userId);
        //Then
        then(articleRepository).should().deleteById(articleId);
    }

    @Test
    @DisplayName("게시글 조회, 댓글 달린 게시글 반환")
    void givenNothing_thenReturnArticleWithComment() {
        //Given
        Long articleId = 1l;
        Article article = createArticle();
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

//        When
        ArticleWithCommentsDto dto = sut.getArticleWithComments(articleId);

//        Then
        assertThat(dto).hasFieldOrPropertyWithValue("title", article.getTitle())
                .hasFieldOrPropertyWithValue("content", article.getContent())
                .hasFieldOrPropertyWithValue("hashtag", article.getHashtag());
        then(articleRepository).should().findById(articleId);
    }

    @Test
    @DisplayName("없는 게시글 조회, 예외 발생")
    void givenNoneExistArticleId_whenSearchArticle_thenThrowsException() {
        //Given
        Long articleId = 0l;
        given(articleRepository.findById(articleId)).willReturn(Optional.empty());

        //When
        Throwable t = Assertions.catchThrowable(() -> sut.getArticle(articleId));
        //catchThrowable() : ()안에 예외 발생시킬 코드 넣기

        //Then
        assertThat(t).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("게시글이 없습니다 - articleId:" + articleId);
        then(articleRepository).should().findById(articleId);
    }
    private UserAccount createUserAccount() {
        return UserAccount.of(
                "bitstudy",
                "password",
                "bitgg@email.com",
                "bitgg",
                "null"
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }

    private ArticleDto createArticleDto() {
        return createArticleDto("title", "content", "java");
    }
    private ArticleDto createArticleDto(String title, String content, String hashtag){
        return ArticleDto.of(
//                1L,
                createUserAccountDto(),
                title,
                content,
                hashtag
//                LocalDateTime.now(),
//                "bitgg",
//                LocalDateTime.now(),
//                "bitgg"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
//                1L,
                "bitstudy",
                "password",
                "bitgg@email.com",
                "bitgg",
                "memos"
//                ,LocalDateTime.now(),
//                "bitgg",
//                LocalDateTime.now(),
//                "bitgg"
        );
    }
}