package com.bitstudy.app.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.catalina.User;
import org.hibernate.loader.collection.OneToManyJoinWalker;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/** Lombok 사용
 * 주의 : maven때와 같은 방식인 것들도 이름이 다르게 되어있음. 헷갈림 주의
 * 순서
 * 1) 롬복을 이용해서 클래스를 엔티티로 변경
 * 2) getter/setter, toString등의 롬복 어노테이션 사용
 * 3) 동등성, 동일성 비교할 수 있는 코드 사용해보기
 *
 * JPA : 자바 ORM 기술 표준. Entity를 분석, create나 insert같은 sql쿼리를 생성해준다.
 * JDBC API를 사용해서 Db접근도 해주고 객체와 테이블을 매핑해준다.
 * */
/** Article과 ArticleComment에 있는 공통 필드(메타데이터) 별도로 관리하기
 * 이유 : 앞으로 fk로 엮인 테이블을 만들 경우 모든 domain 안에 있는 파일들에 많은 중복 코드들이 생김
 *  그래서 별도의 파일에 공통되는 부분을 몰아 넣는 연습.
 * 참고 : 공통코드 빼는 것은 팀마다 다르다.
 *  장점 :유지보수
 *  단점 : 힘들다
 *
 *  중복코드 분리 안할 경우 장점 : 각 파일에 모든 정보가 다 있어서 파악하기 좋음. 변경시 유연한 코드작업 가능
 *
 *  추출 방법
 *  1) @Embedded - 공통되는 필드들을 하나의 클래스로 만들어서 @Embedded 있는 곳에서 하는 방식
 *  2) @MappedSuperClass - (요즘 실무에서 사용) @MappedSuperClass 어노테이션 붙은 곳에서 사용
 *   차이 : @Embedded 방식 - 필드가 하나 추가된다.
 *      영속성 컨텍스트를 통해 데이터를 넘겨 받아서 어플리케이션으로 열었을 때는 어짜피 AuditingField와 똑같이 보임
 *          @MappedSuperClass는 표준 JPA에서 제공해주는 클래스. 중간단계 따로 없이 바로 동작.
 * */
//@EntityListeners(AuditingEntityListener.class)
@Entity // 롬복을 이용해서 클래스를 엔티티로 변경 @Entity가 붙은 클래스는 JPA가 관리하게 된다.
        // 그래서 기본키(PK) 뭔지 알려줘야 하는 것.(@Id)
@Getter // getter/setter toString 등의 롬복 어노테이션 사용시 자동으로 모든 필드의 메서드 생성됨
@ToString(callSuper = true) //모든 필드의 toString생성. 상위(UserAccount)에 있는 toString까지 출력할수있도록
@Table(indexes = { // @Table : 엔티티와 매핑할 정보를 지정
        //사용법) @Index(name="원하는명칭", columnList = "사용할 컬럼명)
        //name생략시 컬럼명 이름 그대로 사용
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "registerDate"),
        @Index(columnList = "createdBy"),
        
        //@Index : 데이터베이스 인덱스는 추가, 쓰기 및 저장공간을 희생해서 테이블에 대한 데이터 검색 속도를
        //      향상시키는 데이터 구조. @Entity와 세트로 사용
})
public class Article extends AuditingFields {
    @Id //전체 필드 중 PK가 무엇인지 선언. 없으면 @Entity 에러난다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //해당 필드가 auto_increment인 경우 @Generatedvalue 써서 자동으로 값이 생성되게 해줘야 함. 기본키 전략
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    /*@JoinColumn은 외래키FK를 맵핑할 때 사용*/
    private UserAccount userAccount;
    @Setter @Column(nullable = false) private String title; // 제목
    @Setter @Column(nullable = false, length = 10000) private String content; // 본문
    @Setter private String hashtag; // 해시태그
    //Setter를 클래스단위로 안걸로 필드단위로 건 이유
    //접근할 수 있는 필드에 제한을 주기 위함(id의 경우 JPA에서 자동으로 부여하는 번호, 메타데이터도 JPA가 자동으로 세팅해 줄 내용들임. 그래서 사용자/개발자가 건들지 못하게 하기 위함. Setter는 필요한 필드에만 부여하는 것이 좋음
    //@Column : 해당 컬럼이 not null인 경우(@Column(nullable=false) 작성
    // 기본값은 true라서 안쓰면 null 가능. length = 바이트수(숫자) 안쓰면 기본값 255 적용

    /* 양방향 바인딩 */
//    @OrderBy("id") //양방향 바인딩의 정렬 기준을 id로 .
    @OrderBy("registerDate desc ") //댓글리스트를 최근순으로 정렬되도록 바꿈
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    @ToString.Exclude /* ** 상단 @ToString 어노테이션 마우스오버시 @ToString includes ~ lazy load...
       퍼포먼스, 메모리 저하 유발할 수 있어 성능에 악영향가능성. 그래서 해당 필드를 가려달라는 요청*/
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();
    /* ** 순환참조 : @ToString.Exclude 안해주면 순환참조 이슈가 생길 수 있음.
    * 여기서 ToString이 id, title, content, hashtag 찍고 Set<ArticleComment>부분 찍으려고
    * ArticleComment.java 파일에 가서 거기 있는 @ToSTring이 요소들을 다 찍으려고 하면서 요소들 중
    * private Article article을 보는 순간 다시 Article을 참조. Article의 @ToString이 재동작...
    * 그래서 @ToString.Exclude 필수
    * Article에서 달아주는 이유는 댓글은 글에 종속되있지만 글이 댓글에 종속되는 것은 아니기 때문
    * (글이 댓글을 참조하는 건 일반적인 경우가 아니기 때문)*/
    public void addComment(ArticleComment comment){
        this.articleComments.add(comment);
    }
    //메타데이터
    /** JPA Auditing : jpa에서 자동으로 세팅하게 해줄 때 사용하는 기능
     *  config 파일이 별도로 필요함 config 패키지 만들어서 JpaConfig 클래스 만들기
     *  */
//    @CreatedDate
//    @Column(nullable = false)
//    private LocalDateTime registerDate; //생성일자
//
//    @CreatedBy
//    @Column(nullable = false, length = 100)
//    private String createdBy; // 생성자
//    // 생성일시 같은 다른 것들은 알아낼 수 있는데, 최초 생성자는 (현재 코드상태 상)인증받고 오지 않아서 따로 알아낼 수 없음. 이때 아까 만든 jpaConfig 파일을 사용.
//    @LastModifiedDate
//    @Column(nullable = false)
//    private LocalDateTime modifiedDate; // 수정일자
//
//    @LastModifiedBy
//    @Column(nullable = false, length = 100)
//    private String modifiedBy; // 수정자
    /**  위처럼 어노테이션 붙여주기만 하면 auditing이 작동
     * @CreateDate : 최초에 insert 할 때 자동으로 한번 넣어줌
     * @CreateBy
     * @LastModifiedDate : 작성 당시의 시간을 실시간으로 넣어줌
     * @LastModifiedBy : 작성 당시의 작성자의 이름을 넣어줌
     * */

    /** Entity를 만들 때는 무조건 기본 생성자가 필요
     * public 또는 protected만 가능. 어디에서도 기본생성자는 안쓰이게 하고싶어서 protected로 변경*/
    protected Article() { }
    /** 사용자가 입력하는 값만 받기. 나머지는 시스템이 알아서 작성하게 만듦.*/
    private Article(UserAccount userAccount,String title, String content, String hashtag) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
    /*접근 제한자 정리
    * public : 어디에서나 접근 가능
    * private : 해당 클래스 외에는 접근 불가
    * protected : 같은 패키지 및 상속 클래스에서 접근 가능
    * default : 같은 패키지에서 접근 가능 */
    public static Article of(UserAccount userAccount, String title, String content, String hashtag){
        return new Article(userAccount, title, content, hashtag);
    }
    /* 정적 팩토리 메서드 (factory method pattern 중 하나)
    * : 객체생성 역할을 하는 클래스 메서드라는 뜻
    * of 메서드를 이용해서 위의 private 생성자를 직접적으로 사용해서 객체를 생성하게 하는 방법
    * 특징 : 무조건 static으로 놔야 함.
    * 장점 :
    *   1) static이기 때문에 new를 이용해서 생성자를 만들지 않아도 됨.
    *   2) return을 갖고 있기 때문에 상속시 값을 확인할 수 있다.
    *   3) 객체 생성을 캡슐화할 수 있다.**
    * */

    /* Article 클래스를 이용해서 게시글들을 list에 담아 화면을 구성한다면 Collection을 이용해야 함
    *   Collection : 객체의 모음(그룹)
    *                 자바가 제공하는 최상위 컬렉션(인터페이스)
    *               하이버네이트는 중복을 허용하고 순서를 보장하지 않음을 가정
    * Set : 중복허용x. 순서유지안함.
    * List : 순서유지. 중복저장가능
    * Map : key,value 구조.
    *
    * list에 넣거나 list에 있는 중복 요소를 제거하거나  정렬할 때 비교를 할 수 있어야하기 때문에
    * 동일성, 동등성 비교를 할 수 있는 equals와 hashcode를 구현해야 함
    * id(PK)만 같으면 두 엔티티가 같다는 뜻이므로 id만 비교하는 메서드를 구현
    * (모든 데이터를 다 불러와 비교하면 느려질 수 있음)
    * */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
