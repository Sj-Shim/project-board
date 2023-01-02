package com.bitstudy.app.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** 슬라이스 테스트 : 기능별(레이어별)로 잘라서 특정 부분(기능)만 테스트 하는 것
 *
 *  -통합 테스트
 *      @SpringBootTest - 스프링이 관리하는 모든 빈을 등록시켜서 테스트(그래서 무겁다)
 *                  *가볍게 하기 위해서 @WebMvcTest 를 사용해서 web 레이어 관련 빈들만 등록한 상태로
 *                  테스르를 할 수도 있다. 단, web레이어 관련 빈들만 등록되므로 Service는 등록안됨.
 *                  그래서 Mock 관련 어노테이션을 이용해서 가짜로 만들어줘야 한다.
 *
 *  - 슬라이스 테스트
 *     1) @WebMvcTest - 슬라이스 테스트에서 대표적인 어노테이션.
 *                  Controller를 테스트할 수 있도록 관련 설정을 제공해준다.
 *                  @WebMvcTest를 선언하면 web과 관련된 Bean만 주입되고, MockMvc를 알아볼 수 있게됨.
 *                  MockMvc는 웹 어플리케이션을 어플리케이션서버에 배포하지 않고 가짜로 테스트용 MVC환경을
 *                  만들어서 요청 및 전송, 응답기능을 제공해주는 유틸리티 클래스
 *                  = 실제 서버에 올리지 않고 테스트용으로 시뮬레이션해서 MVC가 되도록 해주는 클래스
 *
 *      2) @DataJpaTest - JPA 레포지토리 테스트할 때 사용
 *                  @Entity가 있는 엔티티클래스 스캔해서 테스트를 위한 JPA레포지토리들을 설정
 *                  @Component나 @ConfigurationProperties Bean들은 무시
 *
 *      3) @RestClientTest - 클라이언트 입장에서의 API 연동 테스트
 *                  테스트 코드 내에서 Mock 서버를 띄울 수 있다.
 *                  Response, Request에 대한 사전 정의가 가능
 * */
@Disabled
@WebMvcTest
public class DataRestTest_fail {
    /** MockMvc 테스트 방법
     * 1) MockMvc 생성( bean 준비)
     * 2) MockMvc에게 요청에 대한 정보를 입력
     * 3) 요청에 대한 응답값을 expect()를 이용해서 테스트
     * 4) 다 통과하면 테스트 통과*/
    private final MockMvc mvc; //1. bean 준비

    public DataRestTest_fail(@Autowired MockMvc mvc) { // 2. 주입
        this.mvc = mvc;
    }
    /* api - 게시글 리스트 전체 조회*/
    @DisplayName("api 게시글 리스트 전체 조회")
    @Test
    void getAllArticlesTest() throws Exception {
        /* @WebMvcTest는 슬라이스 테스트라서 controller 외의 빈들은 로드하지 않았기 때문에
        * 실패로 뜬다. 그래서 일단 @WebMvcTest 대신 통합테스트로 돌릴 것임*/
        mvc.perform(get("/api/articles"))//MockMvcRequestBuilder
                //MockMvcResultMatchers
                .andExpect(status().isOk()) //현재 200이 들어왔는지 확인
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
        /** 특별한 import (딥 다이브)
         * 1) perform() 안에 get 치고 ctrl + space 누르면 딥다이브 함.
         *      * 그냥 기본으로 나오는건 getClass()인데 그거 말고 MockMvcRequestBuilder
         *      + alt + enter해서 static import
         *      * import statically : 필드나 메서드를 클래스를 지정하지 않고도 코드에서 사용할 수 있도록
         *
         * 2) andExpect(status) : status치고 ctrl space 두세번 하면 MockMvcResultMatchers.status()나옴.
         *
         * 3) andExpect(content().contentType())
         *      : content 검사는 contentType으로 하고 MediaType 사용함.
         *      valueOf 안에 들어갈 content-type은 HAL의 response headers에 있는 content-type에 있는거
         *      복사해오기.
         *      */
    }
}
