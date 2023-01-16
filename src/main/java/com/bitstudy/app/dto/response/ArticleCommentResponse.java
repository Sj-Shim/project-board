package com.bitstudy.app.dto.response;

import com.bitstudy.app.dto.ArticleCommentDto;

import java.io.Serializable;
import java.time.LocalDateTime;


public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime registerDate,
        String email,
        String nickname,
        String userId
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime registerDate, String email, String nickname, String userId) {
        return new ArticleCommentResponse(id, content, registerDate, email, nickname, userId);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.registerDate(),
                dto.userAccountDto().email(),
                nickname,
                dto.userAccountDto().userId()
        );
    }


}
