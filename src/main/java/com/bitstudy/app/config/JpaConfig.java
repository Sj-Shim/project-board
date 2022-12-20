package com.bitstudy.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

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
        return () -> Optional.of("bitstudy");
        //이렇게 하면 앞으로 JPA Auditing 할때마다 사람이름은 이걸로 넣게 됨
        // TODO : 나중에 Spring Security로 인증기능 붙일 때 수정 필요하다.
    }
}
