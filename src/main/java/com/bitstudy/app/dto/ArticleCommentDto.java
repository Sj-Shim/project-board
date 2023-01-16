package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.UserAccount;

import java.time.LocalDateTime;

public record ArticleCommentDto(
        Long id,
        Long articleId,
        UserAccountDto userAccountDto,
        String content,
        LocalDateTime registerDate,
        String createdBy,
        LocalDateTime modifiedDate,
        String modifiedBy
) {
    public static ArticleCommentDto of(Long id,
                                       Long articleId,
                                       UserAccountDto userAccountDto,
                                       String content,
                                       LocalDateTime registerDate,
                                       String createdBy,
                                       LocalDateTime modifiedDate,
                                       String modifiedBy) {
        return new ArticleCommentDto(id, articleId, userAccountDto, content, registerDate, createdBy, modifiedDate, modifiedBy);
    }
    public static ArticleCommentDto of(
                                       Long articleId,
                                       UserAccountDto userAccountDto,
                                       String content) {
        return new ArticleCommentDto(null, articleId, userAccountDto, content, null, null, null, null);
    }


    public static ArticleCommentDto from(ArticleComment entity) {
        return new ArticleCommentDto(entity.getId(), entity.getArticle().getId(), UserAccountDto.from(entity.getUserAccount()), entity.getContent(), entity.getRegisterDate(), entity.getCreatedBy(), entity.getModifiedDate(), entity.getModifiedBy());
    }

    public ArticleComment toEntity(Article article, UserAccount userAccount) {
        return ArticleComment.of(article, userAccount, this.content);
    }
}
