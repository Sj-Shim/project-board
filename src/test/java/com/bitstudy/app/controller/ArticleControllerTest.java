package com.bitstudy.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void articlePage() throws Exception{
    }
}