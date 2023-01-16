package com.bitstudy.app.service;

import com.bitstudy.app.domain.Article;
import com.bitstudy.app.domain.ArticleComment;
import com.bitstudy.app.domain.UserAccount;
import com.bitstudy.app.dto.ArticleCommentDto;
import com.bitstudy.app.dto.UserAccountDto;
import com.bitstudy.app.repository.ArticleCommentRepository;
import com.bitstudy.app.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

/** 코멘트 CRUD 관련 테스트
 * */
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService sut;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;

    /* 코멘트 리스트 조회 */
    @DisplayName("게시글ID로 조회 > 해당 코멘트리스트 반환")
    @Test
    public void givenArticleId_thenReturnCommentsList() {
        //Given
        Long articleId = 1l;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of((expected)));
        //When
        List<ArticleCommentDto> actual = sut.searchArticleComment(articleId);
        //Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);

    }

    /* 코멘트 저장 */
    @DisplayName("댓글 정보 입력, 댓글 저장")
    @Test
    public void givenCommentInfo_thenSaveComment() {
        //Given
        ArticleCommentDto dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        //When
        sut.saveArticleComment(dto);
        //Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }
    /* 코멘트 수정 */
    @DisplayName("댓글 정보 입력, 댓글 수정")
    @Test
    public void givenCommentInfo_thenUpdateComment() {
        //Given
        String oldContent = "content";
        String updateContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDto dto = createArticleCommentDto(updateContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

        //When
        sut.updateArticleComment(dto);
        //Then
        assertThat(articleComment.getContent()).isNotEqualTo(oldContent).isEqualTo(updateContent);
        then(articleCommentRepository).should().getReferenceById(dto.articleId());
        /*shouldHaveNoInteractions : 특정시점 이후로 인터랙션 있는지 체크*/
    }
    /* 코멘트 삭제 */
    @DisplayName("댓글 id 입력 , 댓글 삭제")
    @Test
    public void givenCommentId_thenDeleteComment() {
        //Given
        Long articleCommentId = 1L;
        String userId = "bitstudy";
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);
        //When
        sut.deleteArticleComment(articleCommentId, userId);
        //Then
        then(articleCommentRepository).should().deleteById(articleCommentId);
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount()
                , "title"
                , "content"
                , "#java"
        );
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                Article.of(
                        createUserAccount(),
                        "title",
                        "content",
                        "#java"
                ),
                createUserAccount(),
                content
        );
    }

    private ArticleCommentDto createArticleCommentDto(String content) {
        return ArticleCommentDto.of(
//                1L
                1L
                , createUserAccountDto()
                , content
//                , LocalDateTime.now()
//                , "bitstudy"
//                , LocalDateTime.now()
//                , "bitstudy"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
//                1L
                "bitstudy"
                , "password"
                , "bitstudy@email.com"
                , "bitstudy"
                , "memo"
//                , LocalDateTime.now()
//                , "bitstudy"
//                , LocalDateTime.now()
//                , "bitstudy"
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "bitstudy",
                "password",
                "bitstudy@email.com",
                "bitstudy",
                "memo"
        );
    }
}