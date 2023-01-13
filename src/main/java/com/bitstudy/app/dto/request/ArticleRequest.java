package com.bitstudy.app.dto.request;

import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.UserAccountDto;

/** 사용자가 작성한 게시글 관련 데이터만 모은 DTO*/
public record ArticleRequest(
        String title,
        String content,
        String hashtag
) {
    public static ArticleRequest of(String title,
                                    String content,
                                    String hashtag) {
        return new ArticleRequest(title, content, hashtag);
    }

    public ArticleDto toDto(UserAccountDto userAccountDto) {
        return ArticleDto.of(
                userAccountDto,
                this.title,
                this.content,
                this.hashtag
        );
    }
}
