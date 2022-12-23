package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/** Article, ArticleComment의 중복필드 추출하기
 * 1) Article의 메타데이터들 가져오기
 * 2) Class 레벨에 @MappedSuperClass 달아주기
 * 3) Auditing과 관련된 것들(어노테이션 등) 다 가져오기
 * 
 * 파싱 : 일정한 문법을 토대로 나열된 data를 그 문법에 맞춰서 분석해서 새롭게 구성하는 것
 * */
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class AuditingFields {
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime registerDate; //생성일자

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy; // 생성자
    // 생성일시 같은 다른 것들은 알아낼 수 있는데, 최초 생성자는 (현재 코드상태 상)인증받고 오지 않아서 따로 알아낼 수 없음. 이때 아까 만든 jpaConfig 파일을 사용.
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate; // 수정일자

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy; // 수정자
}
