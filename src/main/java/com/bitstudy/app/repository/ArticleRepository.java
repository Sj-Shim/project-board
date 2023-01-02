package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/** TDD를 위해 임시로 만든 저장소(DB접근용)
 * TDD만드는 방법
 * 1) 우클릭 > Go to > Test (ctrl + shift + t)
 * 2) JUnit5 확인
 * 3) 이름 JpaRepositoryTest로 변경*/
/** 클래스 레벨에 @RepositoryRestResource 붙여서 해당 클래스를 spring rest data 사용할 준비*/
@RepositoryRestResource
public interface ArticleRepository  extends JpaRepository<Article, Long> {
}
