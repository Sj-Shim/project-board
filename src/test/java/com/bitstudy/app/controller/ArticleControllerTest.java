package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.ArticleWithCommentsDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.service.ArticleService;
import com.bitstudy.app.service.PaginationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 이 테스트 코드를 작성하고 실행시 결과적으로 404 발생
 * : 아직 ArticleController에 작성된 내용 없고, DAO같은 클래스도 없기 때문
 * 우선 작성하고 실제 코드와 연결되는지 확인. 슬라이스 방식으로 테스트
 *
 * SpringSecurity 적용 후 : 401 발생 ; 파일을 찾았으나 인증을 못받아서 접속불가
 * config > SecurityConfig를 읽어오게 하면 됨*/
/* autoConfiguration을 가져올 필요가 없기 때문에 슬라이스 테스트 사용 가능*/
//@WebMvcTest //이렇게만 쓰면 모든 컨트롤러를 다 읽어들인다. 컨트롤러에 클래스가 많아지면 모든 컨트롤러를 bean으로 읽어오기 때문에 아래처럼 필요한 클래스만 넣어주면 된다.
@WebMvcTest(ArticleController.class)
@Import(SecurityConfig.class)
@DisplayName("view 컨트롤러 - 게시글")
class ArticleControllerTest {
    private final MockMvc mvc;
    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;
    /*@MockBean : 테스트시 테스트에 필요한 객체를 bean으로 등록시켜서 기존 객체대신 사용할 수 있게 만들어 줌
    * ArticleController에 있는 private final ArticleService articleService; 부분의
    * articleService를 배제하기 위해서 @MockBean 사용 : MockMvc가 입출력 관련된 것들만 보게 하기 위해서
    * 실제 서비스 로직을 끊어주기 위함*/

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("게시판 리스트 페이지")
    @Test
    void articlesPage() throws Exception{
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
    //뷰를 만들고있으니까 HTMl로 코드를 짤 것이므로 /articles로 받아온 데이터의 미디어타입이 html타입으로 되어있는지 확인
    //contentType : exact match라서 미디어타입이 딱 text/html로 나오는 것만 허용
    //contentTypeCompatibleWith : 호환되는 타입까지 허용해줌
                .andExpect(view().name("articles/index"))
                //가져온 뷰 파일 이름 체크 (articles폴더 안의 index 인지 확인중)
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
                //가져온 뷰에서 게시글들이 떠야하는데 그 말은 서버에서 데이터들을 가져왔다는 뜻이므로 모델 attribute에
                // articles가 있어야 할 것이다. 이 여부를 확인하는 테스트. 맵에 articles라는 키가 있는지 검사
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());
    }

    @Test
    @DisplayName("게시판 리스트 페이지 - 검색어와 함께 호출")
    public void givenSearchKeyword_thenReturnArticles() throws Exception{
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(),anyInt())).willReturn(List.of(0,1,2,3,4)); /* 검색했을 때 페이징기능을 호출하긴 하는지 확인*/

        //When
        mvc.perform(
                get("/articles")
                .queryParam("searchType", searchType.name())
                .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));

        //Then
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(),anyInt());

    }

    @DisplayName("게시글 상세 페이지")
    @Test
    void articlePage() throws Exception {
        Long articleId = 1L;
        long totalCount = 1l;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        given(articleService.getArticleCount()).willReturn(totalCount);

        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("totalCount"));

        then(articleService).should().getArticle(articleId);
        then(articleService).should().getArticleCount();
    }

    @DisplayName("게시글 검색 전용 페이지 - 정상호출")
    @Test
    void articlesSearch() throws Exception{
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }

    @DisplayName("해시태그 검색 전용 페이지")
    @Test
    void hashtagSearch() throws Exception {
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"));
    }

    private ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
//                Set<ArticleCommentDto> articleCommentDtos,
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "bitstudy",
                "password",
                "bitstudy@email.com",
                "bitstudy",
                "memo",
                LocalDateTime.now(),
                "bitstudy",
                LocalDateTime.now(),
                "bitstudy"
        );
    }
}