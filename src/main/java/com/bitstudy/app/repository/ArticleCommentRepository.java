package com.bitstudy.app.repository;

import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.QArticleComment;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>
        , QuerydslPredicateExecutor<ArticleComment>
        , QuerydslBinderCustomizer<QArticleComment> {



    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        bindings.excludeUnlistedProperties(true);
        bindings.including(root.content, root.registerDate, root.createdBy);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.registerDate).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
    /** 게시글에 연계된 댓글 검색*/
    List<ArticleComment> findByArticle_Id(Long articleId);
    /* _는 타고 들어가야 할 때 사용됨.
    * 게시글로 댓글을 검색해야 하는데 이런 경우 사용하는 방법
    * ArticleComment 안에는 Article이랑 UserAccount가 있는데, 그 안에 있는 객체 이름인
    * article을 쓰고 _ 로 내려가면 그 객체 안으로 들어간다는 선언(의미)*/

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);
}
