package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@ToString
@Table(indexes = {
    @Index(columnList = "content"),
    @Index(columnList = "registerDate"),
    @Index(columnList = "createdBy"),
})
public class ArticleComment extends AuditingFields{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne(optional = false)
//    @JoinColumn(name = "articleId")
    private Article article; // 연관관계 매핑
    /** 연관관계 매핑
     * 연관관계 없이 만들면 private Long articleId; 식으로 하게 된다.(관계형 DB스타일)
     * Article과 ArticleComment가 관계를 맺고 있는 것을 객체지향적으로 표현하기 위한 방법.
     * 단방향 에너테이션 @ManyToOne
     * optional = false 의미 : 필수값이라는 뜻(옵션이 아니다)
     * 코멘트 여러개 : 게시글 1개 == N : 1 == ManyToOne
     * */
    @Setter
    @ManyToOne(optional = false)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false, length = 500)
    private String content; // 본문
//    //메타데이터
//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime registerDate; //생성일자
//    @CreatedBy
//    @Column(nullable = false, length = 100)
//    private String createdBy; // 생성자
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedDate; // 수정일자
//    @LastModifiedBy
//    @Column(nullable = false, length = 100)
//    private String modifiedBy; // 수정자

    protected ArticleComment(){}

    private ArticleComment(Article article, UserAccount userAccount, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content){
        return new ArticleComment(article, userAccount, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleComment that = (ArticleComment) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
