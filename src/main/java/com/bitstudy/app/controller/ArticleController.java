package com.bitstudy.app.controller;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.type.FormStatus;
import com.bitstudy.app.domain.type.SearchType;
import com.bitstudy.app.dto.ArticleDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.dto.request.ArticleRequest;
import com.bitstudy.app.dto.response.ArticleResponse;
import com.bitstudy.app.dto.response.ArticleWithCommentsResponse;
import com.bitstudy.app.security.BoardPrincipal;
import com.bitstudy.app.service.ArticleService;
import com.bitstudy.app.service.PaginationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequiredArgsConstructor //필수 필드에 대한 생성자 자동 생성
//@RequiredArgsConstructor는 초기화되지 않은 final필드 또는 @NonNull이 붙은 필드에 대해 생성자를 생성해주는 롬복 어노테이션
@Controller
@RequestMapping("/articles") //모든 경로들이 /articles로 시작하므로 클래스 레벨에 걸어줌
public class ArticleController {
    private final ArticleService articleService;
    private final PaginationService paginationService;
    @GetMapping
    public String articles(
            @RequestParam(required = false) SearchType searchType,
            @RequestParam(required = false) String searchValue,
            //@RequestParam : 검색어 받기 위한 어노테이션. getParameter 불러옴. 필수는 아니다.
            //Test로 null 쓰려면 null 허용해주기 위한 (required = false) 필요(없으면 게시글 전체조회됨)
            @PageableDefault(size = 10, sort = "registerDate", direction = Sort.Direction.DESC) Pageable pageable, //@PageableDefault : 페이징 기본 설정
            ModelMap map) {
        /* ModelMap : 테스트파일에서 attribute 체크도 넣어놔서 필요함.
        * Model , ModelMap 차이 : Model은 인터페이스 ModelMap은 클래스(구현체). 사용법은 같음*/
//        map.addAttribute("articles", List.of()); //키 : articles, 값: 그냥 list
//        map.addAttribute("articles", articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from));
        /*실제로 정보를 넣어주기 위해 ArticleService.java의 메서드에 값을 넣어 줌.
          searchArticle()의 반환타입은 dto인데 dto는 모든 엔티티의 데이터를 다 다루고 있어서
          그걸 한번 더 가공해서 필요한 것들만 쓸 것임. 그래서 게시글 내용만 가진
          ArticleResponse를 사용.
        */
        Page<ArticleResponse> articles = articleService.searchArticles(searchType, searchValue, pageable).map(ArticleResponse::from);
        List<Integer> barNumbers = paginationService.getPaginationBarNumbers(pageable.getPageNumber(), articles.getTotalPages());

        map.addAttribute("articles", articles);
        map.addAttribute("paginationBarNumbers", barNumbers);
        map.addAttribute("searchTypes", searchType.values());
        /*enum .values() : enum인 요소들을 배열로 넘겨준다.*/

        return "articles/index";
    }

    @GetMapping("/{articleId}")
    public String articleOne(@PathVariable Long articleId, ModelMap map){
        ArticleWithCommentsResponse article = ArticleWithCommentsResponse.from(articleService.getArticleWithComments(articleId));

//        map.addAttribute("article", null);//테스트할때는 null말고 뭔가 넣어줘야함
//        map.addAttribute("articleComments", List.of());
        map.addAttribute("article", article);
        map.addAttribute("articleComments", article.articleCommentsResponse());
        //
        map.addAttribute("totalCount", articleService.getArticleCount());
        return "articles/detail";
    }
    @GetMapping("/form")
    public String articleForm(Model m) {
        m.addAttribute("formStatus", FormStatus.CREATE);

        return "articles/form";
    }
    @PostMapping("/form")
    public String postNewArticle(ArticleRequest articleRequest) {
        articleService.saveArticle(articleRequest.toDto(
                UserAccountDto.of("bitstudy", "asdf", "bitstudy@email.com", "bitstudy", "vamos", null, null, null, null)
        ));
        return "redirect:/articles";
    }

    /* 게시글 수정화면 띄우기만 하기 */
    @GetMapping("/{articleId}/form")
    public String updateArticleForm(@PathVariable Long articleId, ModelMap map) {
        ArticleResponse article = ArticleResponse.from(articleService.getArticle(articleId));

        map.addAttribute("article", article);
        map.addAttribute("formStatus", FormStatus.UPDATE);

        return "articles/form";
    }

    /* 게시글 수정한거 DB에 업데이트 하기 */
/* 새로 추가 - 게시글 수정할때도 사용자 인증을 받아야 한다.
            유저 정보를 UserAccountDto.of를 통해서 임의로 만들어진 데이터를 사용했다.
            그러나 이제 사용정보를 받아올수 있으니 boardPrincipal을 이용해서 정의할수 있다  */
    @PostMapping("/{articleId}/form")
    public String updateArticle(@PathVariable Long articleId,
                                ArticleRequest articleRequest,
            /*새로추가*/                     @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ){
        /*삭제*/ //articleService.updateArticle(articleId, articleRequest.toDto(UserAccountDto.of(
        //        "bitstudy", "asdf", "bitstudy@email.com", "bitstudy", "memo", null, null, null, null
        //)));
        //return "redirect:/articles/" + articleId;
        System.out.println("articleId : " + articleId);
        System.out.println("title:" + articleRequest.title());
        /*추가*/ articleService.updateArticle(articleId, articleRequest.toDto(boardPrincipal.toDto()));
        /* boardPrincipal.toDto() 가 userAccountDto 를 반환해준다.
         *   반환된 유저dto 를 articleRequest로 아티클dto로 또 바꿔서  updateArticle 로 보낸다.
         * */
        return "redirect:/articles";
    }


    /* 게시글 삭제하기 */
    /* 원래 delete 는 REST 방식에서는 delete 라고 했었지만 form 태그 쓸때는 get 과 Post만 허용되기 떄문에 어쩔수 없음. */
    @PostMapping ("/{articleId}/delete")
    public String deleteArticle(
            @PathVariable Long articleId,

            /* 추가 - 인증정보 가져온다.*/
            @AuthenticationPrincipal BoardPrincipal boardPrincipal
    ) {
        // TODO: 인증 정보를 넣어줘야 한다.
        /*삭제*///articleService.deleteArticle(articleId, userId);
        /*추가*/articleService.deleteArticle(articleId, boardPrincipal.getUsername());

        return "redirect:/articles";
    }


    /* 여기까지 하고 ArticleControllerTest 가서 전체 테스트 돌리면 다 통과함*/
}
