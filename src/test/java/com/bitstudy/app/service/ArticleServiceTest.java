package com.bitstudy.app.service;

import com.bitstudy.app.repository.ArticleRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
/** 서비스 비즈니스 로직 테스트
 * (슬라이스 테스트 기능 없이)
 * 스프링 부트 어플리케이션 컨텍스트가 뜨는 데 걸리는 시간을 없애기 위함
 * 디펜던시가 추가되야 하는 부분은 Mocking하는 방식으로 진행.
 * 주로 사용되는 라이브러리 : mockito(스프링 테스트 패키지에 내장)*/
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    /* Mock을 주입하는 거에(테스트 대상) @InjectionMocks 달기. 그 외의 것들(의존성 대상)에겐 @Mock 달기*/
    @InjectMocks
    private ArticleService sut; //sut - system under test. 테스트시 자주 사용되는 이름. 테스트 대상이라는 뜻

    @Mock
    private ArticleRepository articleRepository;
}