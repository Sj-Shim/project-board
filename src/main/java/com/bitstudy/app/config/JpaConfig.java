package com.bitstudy.app.config;

import com.bitstudy.app.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/** @Configuration : 설정파일 만들기 위한 어노테이션 / Bean을 등록하기 위한 어노테이션
 * 이걸 쓰면 JpaConfig 클래스는 Configuration bean 이 된다.
 * 이 어노테이션을 쓰면 시스템에 이 클래스가 설정파일이니 bean으로 등록해달라고 요청하는 것.
 * @EnableJpaAuditing : Jpa에서 auditing을 가능하게 하는 어노테이션
 *  jpa auditing : Spring Data Jpa 에서 자동으로 값을 넣어주는 기능. jpa에서 자동으로 세팅하게 해줄때 사용하는 기능
 *
 * */
@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
//        return () -> Optional.of("bitstudy");
//        //이렇게 하면 앞으로 JPA Auditing 할때마다 사람이름은 이걸로 넣게 됨
//        // TODO : 나중에 Spring Security로 인증기능 붙일 때 수정 필요하다.
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                /* 해설:  ofNullable: 일반 객체뿐만 아니라 null값까지 입력으로 받을 수 있다는 뜻. 인증이 안됐을수도 있으니까
                 *       SecurityContextHolder: 모든 인증정보를 가지고 있음
                 *       getContext(): SecurityContextHolder 안에서 시큐리티 컨텍스트를 가져온다.(컨텍스트란 애플리케이션 환경에 대한 인터페이스 이자, 추상 클래스 이다. 즉, 애플리케이션의 현재 상태 를 뜻한다) */
                .map(SecurityContext::getAuthentication)
                /* SecurityContext 는 org.springframework.security.core.context 사용
                 * getAuthentication: 인증 정보를 불러온다. Authentication(인증), Authorization(권한) 이라는 뜻 */
                .filter(Authentication::isAuthenticated) /* isAuthenticated: filter를 이용해서 인증이 됐는지 확인 하는 뜻. 로그인 한 상태인지 보라는거임.  */
                .map(Authentication::getPrincipal) /* Authentication 안에 Principal 이라는 인증정보 가져오라는 뜻 */
                .map(BoardPrincipal.class::cast) /* 아까 만든 BoardPrincipal class 를 불러온다.  */
                .map(BoardPrincipal::getUsername); /* BoardPrincipal 에서 인증받은 유저 이름을 가져온다. */
    }

/* 다 했으면
    인증정보를 이용해서 만들어놓은 기능들을 사용할 수 있게 바꿔줘야 한다.

    test >  config 폴더 생성  > Ex19_4_TestSecurityConfig.java 만들기
*  */
}
