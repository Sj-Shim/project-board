package com.bitstudy.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Article {
    private Long id;
    private String title; // 제목
    private String content; // 본문
    private String hashtag; // 해시태그
    //메타데이터
    private LocalDateTime registerDate; //생성일자
    private String createdBy; // 생성자
    private LocalDateTime modifiedDate; // 수정일자
    private String modifiedBy; // 수정자
}
