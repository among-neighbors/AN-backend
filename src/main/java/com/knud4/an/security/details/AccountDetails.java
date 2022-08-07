package com.knud4.an.security.details;

import com.knud4.an.account.entity.Account;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

public class AccountDetails extends User {

    public AccountDetails(Account account) {
        super(account.getEmail(), account.getPassword(),
                AuthorityUtils.createAuthorityList(account.getRole().toString()));
    }

    public String getEmail() {
        return this.getUsername();
    }
}
