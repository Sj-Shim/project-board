package com.bitstudy.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/** 뷰 엔드포인트 관련 컨트롤러
 * (엑셀 api에 정의해놓은 view 부분 url 참조)
 * /articles    GET 게시판페이지
 * /articles/{article-id}   GET 게시글페이지
 * /articles/search GET 게시판 검색페이지
 * /articles/search-hashtag GET 게시판 해시태그 검색 페이지
 *
 * Thymeleaf : 뷰 파일은 html로 작업할건데, 타임리프를 설치하여 스프링은 이제 html 파일을 마크업으로 보지 않고
 *  타임리프 템플릿 파일로 인식한다. html파일들은 resources의 templates 디렉터리에만 작성
 *  static 디렉터리에는 css,js, img등 정적 파일 작성.
 *
 * */
//@Controller
@RequestMapping("/articles") //모든 경로들이 /articles로 시작하므로 클래스 레벨에 걸어줌
public class ArticleController {
    /* BDD 하러가기*/
}
