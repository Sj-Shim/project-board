package com.bitstudy.app.repository;

import com.bitstudy.app.domain.QUserAccount;
import com.bitstudy.app.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserAccountRepository extends
        JpaRepository<UserAccount, Long> {
//        , QuerydslPredicateExecutor<UserAccount>
//        , QuerydslBinderCustomizer<QUserAccount> {
}
