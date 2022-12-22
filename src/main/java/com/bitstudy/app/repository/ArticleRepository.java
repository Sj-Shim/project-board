package com.bitstudy.app.repository;

import com.bitstudy.app.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

/** TDD를 위해 임시로 만든 저장소(DB접근용)
 * TDD만드는 방법
 * 1) 우클릭 > Go to > Test (ctrl + shift + t)
 * 2) JUnit5 확인
 * 3) 이름 JpaRepositoryTest로 변경*/
public interface ArticleRepository  extends JpaRepository<Article, Long> {
}
