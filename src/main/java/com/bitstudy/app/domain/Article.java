package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.time.LocalDateTime;

/** Lombok 사용
 * 주의 : maven때와 같은 방식인 것들도 이름이 다르게 되어있음. 헷갈림 주의
 * 순서
 * 1) 롬복을 이용해서 클래스를 엔티티로 변경
 * 2) getter/setter, toString등의 롬복 어노테이션 사용
 * 3) 동등성, 동일성 비교할 수 있는 코드 사용해보기
 * */
@Getter
@Setter
@ToString
@Entity
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
