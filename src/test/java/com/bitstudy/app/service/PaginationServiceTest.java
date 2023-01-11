package com.bitstudy.app.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
/* webEnvironment : 기본값 Mock. Mocking된 웹 환경 구성. None을 넣어서 웹환경을 넣지 않아서 부트테스트를 가볍게 할 수 있음
* classes = PaginationService.class : 설정 클래스를 지정해서 가볍게 만들기*/
class PaginationServiceTest {

    /* 그냥 @SpringBootTest 쓸거면 new 필요
    * private final PaginationService sut = new PaginationService();*/
    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService sut) {
        this.sut = sut;
    }

    @DisplayName("현재 페이지 번호, 총 페이지 수 주입 > 페이지 바 만들기")
    @ParameterizedTest(name = "[{index}] 현재페이지:{0}, 총페이지 {1} => {2}")
    @MethodSource // 이게 있어야 Arguments 메서드 가져온다.
    /*@ParameterizedTest : 여러 argument를 등록해놓고 한번에 여러번 돌릴 수 있는 기능을 가진 테스트.
    *       그걸 제외하면 @Test와 같음 = @Test 생략 가능
    *       @ParameterizedTest 사용시 테스트를 위해 들어가는 값, 객체 필요함(source)
    *           Source 종류
    *           1)ValueSource - 같은 타입의 여러가지 단순한 값(literal value)들을 테스트할 때 사용
    *                   @ValueSource(ints = {0, 101})
    *           2)CsvSource - comma(,)로 구분되는 값을 사용
    *                   @CsvSource({"10, "true" , "100, false"})
    *           3)MethodSource(주로쓰임) - 메서드에서 리턴되는 값을 인자로 사용. 입력값이 별도의 메서드로 있어야 함
    *
    *   name을 이용해서 출력 포멧을 설정할 수도 있다.(@DisplayName같은 것)
    *       {0], {1}, {2} : 메서드의 매개변수 순서
    *       0 : int currentPageNumber
    *       1 : int totalPages
    *       2 : List<Integer> expected*/
    void givenCurrPageNumberAndTotalPages_thenReturnBarList(int currentPageNumber, int totalPages, List<Integer> expected) {
        //Given

        //When
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNumber, totalPages);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrPageNumberAndTotalPages_thenReturnBarList() {
        return Stream.of(

                /* arguments는 딥다이브해서 org.junit.jupiter.paras.provider.Arguments
                * 매개변수 순서는 위의 메서드의 매개변수 순서(currentPageNumber, totalPages, List
                *
                * 1페이지는 0으로 표시
                * */
                arguments(0, 10, List.of(0,1,2,3,4)),
                arguments(1, 10, List.of(0,1,2,3,4)),
                arguments(2, 10, List.of(0,1,2,3,4)),
                arguments(3, 10, List.of(1,2,3,4,5)),
                arguments(4, 10, List.of(2,3,4,5,6)),
                arguments(5, 10, List.of(3,4,5,6,7)),
                arguments(6, 10, List.of(4,5,6,7,8)),
                arguments(7, 10, List.of(5,6,7,8,9)),
                arguments(8, 10, List.of(6,7,8,9)),
                arguments(9, 10, List.of(7,8,9))
        );
    }

    @DisplayName("현재 설정된 페이지네이션 바 길이 알아내기")
    @Test
    void given_whenCallMethod() {
        //Given
        //When
        int barLength = sut.currentBarLength();
        //Then
        assertThat(barLength).isEqualTo(5);
    }
}