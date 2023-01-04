package com.bitstudy.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** 이 테스트 코드를 작성하고 실행시 결과적으로 404 발생
 * : 아직 ArticleController에 작성된 내용 없고, DAO같은 클래스도 없기 때문
 * 우선 작성하고 실제 코드와 연결되는지 확인. 슬라이스 방식으로 테스트*/
/* autoConfiguration을 가져올 필요가 없기 때문에 슬라이스 테스트 사용 가능*/
//@WebMvcTest //이렇게만 쓰면 모든 컨트롤러를 다 읽어들인다. 컨트롤러에 클래스가 많아지면 모든 컨트롤러를 bean으로 읽어오기 때문에 아래처럼 필요한 클래스만 넣어주면 된다.
@WebMvcTest(ArticleController.class)
@DisplayName("view 컨트롤러 - 게시글")
class ArticleControllerTest {
    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("게시판 리스트 페이지")
    @Test
    void articlesPage() throws Exception{
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
    //뷰를 만들고있으니까 HTMl로 코드를 짤 것이므로 /articles로 받아온 데이터의 미디어타입이 html타입으로 되어있는지 확인
    //contentType : exact match라서 미디어타입이 딱 text/html로 나오는 것만 허용
    //contentTypeCompatibleWith : 호환되는 타입까지 허용해줌
                .andExpect(view().name("articles/index"))
                //가져온 뷰 파일 이름 체크 (articles폴더 안의 index 인지 확인중)
                .andExpect(model().attributeExists("articles"));
                //가져온 뷰에서 게시글들이 떠야하는데 그 말은 서버에서 데이터들을 가져왔다는 뜻이므로 모델 attribute에
                // articles가 있어야 할 것이다. 이 여부를 확인하는 테스트. 맵에 articles라는 키가 있는지 검사
    }

    @DisplayName("게시글 상세 페이지")
    @Test
    void articlePage() throws Exception {
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("articleComments"));
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
}