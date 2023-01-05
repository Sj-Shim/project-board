package com.bitstudy.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** spring security를 설치하면 어떤 url로 접근하든 무조건 로그인화면으로 넘어감.
 * (DefaultLoginPageGeneratingFilter 의 "/login"부분)
 * 아직은 로그인 기능이 없으므로 요청하는 url대로 화면이 나타나게 만들기(로그인페이지가 없어지는것은 아님)*/
@EnableWebSecurity //블로그, 책 보면 무조건 넣으라고 하는 어노테이션이지만 이제 안넣어도 됨(auto-configuration에 들어가있음)
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* HttpSecurity : 세부적인 보안 기능을 설정할 수 있는 api를 제공.
        *       - URL 접근 권한 설정
        *       - 커스텀 로그인 페이지 지원
        *       - 인증 후 성공/실패 핸들링
        *       - 사용자 로그인/로그아웃
        *       - CSRF 공격으로부터 보호*/
        return http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                //HttpSecurity의 authorizeHttpRequests에서 모든 요청(anyRequest())에 대한 인증을 허용
                .formLogin()//로그인 페이지를 만들어라
//                .loginPage("로그인페이지 경로")
                .and().build(); // 그리고 빌드해라
        /* http.formLogin() 이후 사용할 수 있는 메서드
        *   .loginPage("/login.html") : 커스텀 로그인페이지 보여줄때
        *   .defaultSuccessUrl("/index") : 로그인 성공 후 이동할 페이지 경로
        *   .failureUrl("/login.html?error=true") : 로그인 실패 후 이동할 페이지
        *   .usernameParameter("유저네임") : 아이디 파라미터명 설정*/
    }
}
