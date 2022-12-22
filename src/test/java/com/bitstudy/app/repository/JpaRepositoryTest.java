package com.bitstudy.app.repository;

import com.bitstudy.app.config.JpaConfig;
import com.bitstudy.app.domain.Article;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

/** 슬라이드 테스트
 * 메서드 각각 테스트 결과를 서로 인지 못하게 잘라서 만드는 것
 * */
@DataJpaTest //슬라이드 테스트
@Import(JpaConfig.class) // 원래는 JPA에서 모든 정보를 컨트롤한다. JpaConfig의 경우는 읽어오지 못함
    //시스템에서 만들지 않고 별도로 만든 파일이기 때문. 그래서 따로 import 해줘야 하는 것.
    //안하면 config 안에 명시한 JpaAuditing 기능이 동작하지 않는다.
@DisplayName("JPA 테스트")
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    /* 원래는 둘 다 @Autowired가 붙어야 하는데, JUnit5 버전과 최신버전의 스프링 부트를 이용하면
    * Test에서 생성자 주입패턴을 사용할 수 있다.*/

    /* 생성자 만들기 - 여기서는 다른 파일에서 매개변수로 보내주는 것을 받는 거라서 위와 상관 없이
    * @Autowired를 붙여줘야 한다.*/
    public JpaRepositoryTest(@Autowired ArticleRepository articleRepository, @Autowired ArticleCommentRepository articleCommentRepository){
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    /* transaction시 사용하는 메서드
    * 사용법 : repository명.메서드()
    * 1) findAll() - 모든 컬럼 조회시. 페이징(pageable) 가능
    *               select작업이 기본. 잠깐 사이에 해당 테이블에 어떤 변화가 있었는지 알 수 없기 때문에
    *               select 전에 먼저 최신 데이터를 구하기 위한 update를 한다.
    *               동작순서 : update -> select
    * 2) findById() - 한 건에 대한 데이터 조회시에 사용
    *               primary key로 레코드 한 건 조회.( ) 안에 글 번호(pk)넣어줘야함
    * 3) save() - 레코드 저장할 때 사용(insert, update)
    * 4) count() - 레코드 개수 뽑을 때 사용
    * 5) delete() - 레코드 삭제시.
    *
    * ----------------------------
    * - 테스트용 데이터 가져오기 mockaroo
    * */
    /* select test*/
    @DisplayName("셀렉트 테스트")
    @Test
    void selectTest(){
        /** 게시글 셀렉트 > articleRepository 기준으로 테스트
         * maven방식 : dao -> mapper 로 정보 보내고 DB 갔다와서 Controller까지 돌려보냄.
         *          dao에서 dto를 list에 담아서 return
         *
         * */
        /** assertJ를 이용한 테스트
         * articles가 NotNull 이고 사이즈가 expected 개면 통과*/
        List<Article> articles = articleRepository.findAll();
        assertThat(articles).isNotNull().hasSize(100); //assertJ
    }

    /* insert test*/
    @DisplayName("인서트 테스트")
    @Test
    void insertTest(){
        long count = articleRepository.count();
        articleRepository.save(Article.of("테스트", "테스트1", "#테스트"));
        long count2 = articleRepository.count();
        assertThat(count2).isEqualTo(count + 1);
        /* Article 클래스 엔티티에서 Auditing 쓴다는 명시 (@EntityListeners(AuditingEntityListener.class))
        * 없으면 에러난다.*/
    }

    /* update test*/
    @DisplayName("업데이트 테스트")
    @Test
    void updateTest(){
        /* 기존 영속성 컨텍스트에서 엔티티 하나 가져와서 업데이트로 한 컬럼 바꾸기
        * 1) 기존 영속성 컨텍스트에서 엔티티 하나 객체를 가져오기(DB에서 레코드 한줄 뽑아오기)
        *    articleRepository 기존 영속성 컨텍스트
        *   findById(1l) 엔티티 하나
        *   orElseThrow() 없으면 throw시켜서 테스트 끝나게
        * 2) 업데이트로 해시태그 바꾸기
        *  */
        Article article = articleRepository.findById(1l).orElseThrow();
        String hashtag = "#테스트";
        article.setHashtag(hashtag);
        articleRepository.saveAndFlush(article);
        /* flush : 변경점 감지, 수정된 엔티티를 sql 저장소에 등록,
        *      sql 저장소에 있는 쿼리를 db에 전송 */
        String updateHashtag =(articleRepository.findById(1l).orElseThrow()).getHashtag();
        Article savedArticle = articleRepository.findById(1l).orElseThrow();
//        assertThat((articleRepository.findById(1l).orElseThrow()).getHashtag()).isEqualTo(hashtag);
//        assertThat(updateHashtag).isEqualTo(hashtag);
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", hashtag);
    }

    @DisplayName("딜리트 테스트")
    @Test
    void deleteTest() {
        Long count = articleRepository.count();
        Article article = articleRepository.findById(1l).orElseThrow();
        Long oriCoCount = articleCommentRepository.count();
        int coCount = article.getArticleComments().size(); //연관 댓글 삭제도 테스트
//        articleRepository.deleteById(1l);
        articleRepository.delete(article);
        Long count2 = articleRepository.count();

        assertThat(count2).isEqualTo(count - 1);
        assertThat(articleCommentRepository.count()).isEqualTo(oriCoCount - coCount);
    }
}
