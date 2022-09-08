package com.knud4.an.security.details;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String profileId) throws UsernameNotFoundException {
        Profile profile = accountRepository.findProfileById(Long.parseLong(profileId))
                        .orElseThrow(() -> new UsernameNotFoundException("프로필이 존재하지 않습니다."));

        Account account = accountRepository.findAccountById(profile.getAccount().getId());

        return new AccountDetails(account);
    }
}
