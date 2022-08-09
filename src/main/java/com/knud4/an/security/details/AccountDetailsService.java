package com.knud4.an.security.details;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.security.details.AccountDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일과 일치하는 계정이 없습니다."));

        return new AccountDetails(account);
    }
}
