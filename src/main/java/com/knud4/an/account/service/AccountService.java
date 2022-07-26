package com.knud4.an.account.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    public Account findAccountByAccountId(Long id) {
        return accountRepository.findAccountById(id)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다.")
        );
    }

    public Account findAccountByEmail(String email) throws NotFoundException{
        return accountRepository.findAccountByEmail(email)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));
    }

    public Account findAccountByUserName(String userName) throws NotFoundException{
        return accountRepository.findAccountByUsername(userName)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));
    }

    public Profile findProfileById(Long id) throws NotFoundException{
        return accountRepository.findProfileById(id)
                .orElseThrow(() -> new NotFoundException("프로필이 존재하지 않습니다."));
    }

    public List<Profile> findProfilesByAccountId(Long id) {
        return accountRepository.findProfilesByAccountId(id);
    }

    public Profile findProfileByAccountIdAndProfileName(Long accountId, String profileName) {
        return accountRepository.findProfileByAccountIdAndProfileName(accountId, profileName)
                .orElseThrow(() -> new NotFoundException("프로필이 존재하지 않습니다."));
    }
}
