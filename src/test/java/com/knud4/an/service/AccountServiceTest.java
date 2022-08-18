package com.knud4.an.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountServiceTest {
    @Autowired
    private AccountService accountService;

    @PersistenceContext
    EntityManager em;

    @Test
    public void findAccount() {
        Account account = Account.builder()
                .email("abc@123")
                .password("123")
                .username("test")
                .build();

        em.persist(account);

        Account findAccount = accountService.findAccountByAccountId(account.getId());

        assertThat(findAccount).isEqualTo(account);
    }
}
