package com.knud4.an.auth.service;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.account.service.AccountService;
import com.knud4.an.auth.dto.AddProfileForm;
import com.knud4.an.auth.dto.SignInAccountForm;
import com.knud4.an.auth.dto.SignInProfileForm;
import com.knud4.an.auth.dto.SignUpAccountForm;
import com.knud4.an.exceptions.NotAuthenticatedException;
import com.knud4.an.exceptions.NotFoundException;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.house.service.HouseService;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final HouseRepository houseRepository;
    private final LineRepository lineRepository;

    @Transactional
    public Long SignUpAccount(SignUpAccountForm form) {
//        이메일 중복 검사
//        유저 이름 중복 검사
//        패스워드 암호화
        Line line = lineRepository.findByName(form.getLineName())
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        House house = houseRepository.findByLineIdAndHouseName(line.getId(), form.getHouseName())
                .orElseThrow(() -> new NotFoundException("세대를 찾을 수 없습니다."));

        Account account = Account.builder()
                .username(form.getUsername())
                .password(form.getPasswd())
                .email(form.getEmail())
                .line(line)
                .house(house)
                .build();

        accountRepository.saveAccount(account);

        return account.getId();
    }

    public Account SignInAccount(SignInAccountForm form) {
        Account findAccount = accountService.findAccountByUserName(form.getUsername());

//        암호 검증

        return findAccount;
    }

    @Transactional
    public Long AddProfile(AddProfileForm form, Long accountIdFromToken) {
        if (!form.getAccountId().equals(accountIdFromToken)) {
            throw new NotAuthenticatedException("계정 정보가 잘못되었습니다.");
        }

        Account findAccount = accountService.findAccountByAccountId(accountIdFromToken);
        Profile profile = Profile.builder()
                .name(form.getName())
                .account(findAccount)
                .pin(form.getPin())
                .age(form.getAge())
                .build();

//        pin 번호 암호화

        accountRepository.saveProfile(profile);

        return profile.getId();
    }

    public Profile SignInProfile(SignInProfileForm form, Long accountIdFromToken) {
        if (!form.getAccountId().equals(accountIdFromToken)) {
            throw new NotAuthenticatedException("계정 정보가 잘못되었습니다.");
        }

        Profile findProfile = accountService.findProfileById(form.getProfileId());
//        pin 번호 검증

        return findProfile;
    }

}
