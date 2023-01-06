package com.bitstudy.app.service;

import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 기능 관련 로직을 담을 클래스 = Service*/
@Service
@RequiredArgsConstructor //필수 필드에 대한 생성자를 자동으로 만들어주는 롬복 어노테이션
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository; //아티클 서비스이므로 해당 레포지토리 필요
    /** 검색용*/
    @Transactional(readOnly = true) //트랜잭션 읽기전용 설정. 실수로 커밋해도 flush되지않아서 엔티티가 등록,수정,삭제되지 않음
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        return Page.empty();
    }
}
