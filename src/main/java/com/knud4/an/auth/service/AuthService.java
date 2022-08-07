package com.knud4.an.auth.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.entity.Role;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.auth.dto.profile.AddProfileForm;
import com.knud4.an.auth.dto.account.SignInAccountForm;
import com.knud4.an.auth.dto.profile.SignInProfileForm;
import com.knud4.an.auth.dto.account.SignUpAccountForm;
import com.knud4.an.exception.NotAuthenticatedException;
import com.knud4.an.exception.NotFoundException;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final AccountRepository accountRepository;
    private final HouseRepository houseRepository;
    private final LineRepository lineRepository;

    private boolean isDuplicateAccountEmail(String email) {
        return accountRepository.accountExistsByEmail(email);
    }

    private boolean isDuplicateAccountUsername(String username) {
        return accountRepository.accountExistsByEmail(username);
    }

    @Transactional
    public Long signUpAccount(SignUpAccountForm form) throws RuntimeException {

        if (isDuplicateAccountEmail(form.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }
        if (isDuplicateAccountUsername(form.getUsername())) {
            throw new IllegalStateException("이미 존재하는 아이디 입니다.");
        }

        Line line = lineRepository.findByName(form.getLineName())
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        House house = houseRepository.findByLineIdAndHouseName(line.getId(), form.getHouseName())
                .orElseThrow(() -> new NotFoundException("세대를 찾을 수 없습니다."));

        Account account = Account.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPasswd()))
                .email(form.getEmail())
                .line(line)
                .house(house)
                .role(Role.ROLE_USER)
                .build();

        accountRepository.saveAccount(account);

        return account.getId();
    }

    public Account signInAccount(SignInAccountForm form) throws NotFoundException {
        Account findAccount = accountRepository.findAccountByUsername(form.getUsername())
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

        if (!passwordEncoder.matches(form.getPasswd(), findAccount.getPassword())) {
            throw new NotFoundException("비밀번호가 잘못되었습니다.");
        }

        return findAccount;
    }

    @Transactional
    public Long AddProfile(AddProfileForm form, String accountEmailFromToken) throws RuntimeException{

        Account account = accountRepository.findAccountByEmail(accountEmailFromToken)
                .orElseThrow(() -> new NotAuthenticatedException("계정 정보가 잘못되었습니다."));

        if (!form.getAccountId().equals(account.getId())) {
            throw new NotAuthenticatedException("계정 정보가 잘못되었습니다.");
        }

        Profile profile = Profile.builder()
                .name(form.getName())
                .account(account)
                .pin(passwordEncoder.encode(form.getPin()))
                .age(form.getAge())
                .gender(form.getGender())
                .build();

        accountRepository.saveProfile(profile);

        return profile.getId();
    }

    public Profile signInProfile(SignInProfileForm form, String accountEmailFromToken) throws RuntimeException{

        Account account = accountRepository.findAccountByEmail(accountEmailFromToken)
                .orElseThrow(() -> new NotAuthenticatedException("계정 정보가 잘못되었습니다."));

        if (!form.getAccountId().equals(account.getId())) {
            throw new NotAuthenticatedException("계정 정보가 잘못되었습니다.");
        }

        Profile findProfile = accountService.findProfileById(form.getProfileId());

        if (!passwordEncoder.matches(form.getPin(), findProfile.getPin())) {
            throw new NotFoundException("판 번호가 잘못되었습니다.");
        }

        return findProfile;
    }

}
