package com.bitstudy.app.controller;

import com.bitstudy.app.config.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 로그인 페이지 기능 테스트
 *  로그인 페이지는 스프링 시큐리티와 부트가 만들어 준 것. 즉 이미 테스트가 다 검증되서 별도로 테스트 할 것은 없음
 *  따라서 최소한의 테스트만 진행. 기능이 우리 서비스에 존재하는지 확인하면 됨.
 *  실제 controller 파일이 존재하지 않아도 됨.
 *
 *  get("/login")으로 요청을 보냈을 때 200이 뜨면 통과*/

@WebMvcTest
@Import(SecurityConfig.class) // ArticleControllerTest와 동일한 환경에서 테스트하기 위함
public class AuthControllerTest {

    private final MockMvc mvc;

    public AuthControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("[view][GET] 로그인 페이지 호출 테스트")
    void getLogin() throws Exception{
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }
}
