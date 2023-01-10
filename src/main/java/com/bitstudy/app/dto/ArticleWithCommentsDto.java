package com.bitstudy.app.dto;

import com.bitstudy.app.domain.Article;
import org.apache.catalina.User;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record ArticleWithCommentsDto(
        Long id,
        UserAccountDto userAccountDto,
        Set<ArticleCommentDto> articleCommentDtos,
        String title,
        String content,
        String hashtag,
        LocalDateTime registerDate,
        String createdBy,
        LocalDateTime modifiedDate,
        String modifiedBy) {
    public static ArticleWithCommentsDto of(Long id, UserAccountDto userAccountDto, Set<ArticleCommentDto> articleCommentDtos, String title, String content, String hashtag, LocalDateTime registerDate, String createdBy, LocalDateTime modifiedDate, String modifiedBy) {
        return new ArticleWithCommentsDto(id, userAccountDto, articleCommentDtos, title, content, hashtag, registerDate, createdBy, modifiedDate, modifiedBy);
    }

    public static ArticleWithCommentsDto from(Article entity) {
        return  new ArticleWithCommentsDto(
                entity.getId(),
                UserAccountDto.from(entity.getUserAccount()),
                entity.getArticleComments().stream()
                        .map(ArticleCommentDto::from)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getTitle(),
                entity.getContent(),
                entity.getHashtag(),
                entity.getRegisterDate(),
                entity.getCreatedBy(),
                entity.getModifiedDate(),
                entity.getModifiedBy()
        );
    }
}
