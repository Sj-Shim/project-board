package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.QArticle;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/** TDD를 위해 임시로 만든 저장소(DB접근용)
 * TDD만드는 방법
 * 1) 우클릭 > Go to > Test (ctrl + shift + t)
 * 2) JUnit5 확인
 * 3) 이름 JpaRepositoryTest로 변경*/
/** 클래스 레벨에 @RepositoryRestResource 붙여서 해당 클래스를 spring rest data 사용할 준비*/

/** QueryDsl의 QuerydslPredicateExecutor와 QuerysalBinderCustomizer를 이용해서 검색기능 만들기*/
@RepositoryRestResource
public interface ArticleRepository  extends
        JpaRepository<Article, Long>
        ,QuerydslPredicateExecutor<Article>
        ,QuerydslBinderCustomizer<QArticle> {
// QuerydslBinderCustomizer는 QArticle을 사용하는데 이건 build.gradle에서 queryDsl을 build하고 와야 함
    /* QuerydslPredicateExecutor는 Article 안에 있는 모든 필드에 대한 기본 검색기능을 추가해준다. (딱 똑같은 문자(열)만 검색해줌)
    *       순서 : 1. 바인딩
    *              2. 검색용 필드를 추가*/

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String title, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String title, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String title, Pageable pageable);
    Page<Article> findByHashtagContaining(String title, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        /* 1. 바인딩
         *   현재 QuerydslPredicateExecutor 때문에 Article에 있는 모든 필드에 대한 검색이 열린 상태.
         *   선택적 필드(제목, 내용, id, 글쓴이, 태그)만 검색에 사용되도록 하기 위해 bindings.excludeUnlistedProperties를 사용
         *   리스팅 하지 않은 프로퍼티는 검색에 포함할지 말지 결정하는 메서드(true면 검색에서 제외, false는 모든 프로퍼티 허용(기본))*/
        bindings.excludeUnlistedProperties(true);

        /* 2. 검색용 (원하는) 필드를 지정(추가)하는 부분
        *   including을 이용해서 title, content, createdBy, hashtag 검색 가능하게 만들기
        *   (id는 인증기능을 달아서 유저정보를 알아올 수 있을때 추가)
        *   including 사용법 : root.필드명*/
        bindings.including(root.title, root.content, root.createdBy, root.hashtag);
        /* 3. 정확한 검색만 됐던 것을 'or검색' 가능하게 바꾸기*/
//        bindings.bind(root.title).first(StringExpression::likeIgnoreCase);// like '%{문자열}' 로 들어감. %없이 들어가는거라서 수동으로 넣어줘야 함
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        //날짜를 검색할 경우 : bindings.bind(root.createdAt).first(DateTimeExpression::eq); 날짜니까 DateTimeExpression, eq는 equals(정확한검색), 날짜필드는 정확한 검색만 되도록 설정.
        //  그런데 이렇게 하면 시분초가 다 0으로 인식됨. 나중에 시간 처리할 때 해결할 부분

    }
}
