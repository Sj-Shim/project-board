package com.bitstudy.app.domain;

import java.time.LocalDateTime;

public class Ex00_2_ArticleComment {
    private Long id;
    private Article article; // 연관관계 매핑
    /** 연관관계 매핑
     * 연관관계 없이 만들면 private Long articleId; 식으로 하게 된다.(관계형 DB스타일)
     * Article과 ArticleComment가 관계를 맺고 있는 것을 객체지향적으로 표현하기 위한 방법.
     * */
    private String content; // 본문
    //메타데이터
    private LocalDateTime registerDate; //생성일자
    private String createdBy; // 생성자
    private LocalDateTime modifiedDate; // 수정일자
    private String modifiedBy; // 수정자
}
