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
import com.knud4.an.gcp.storage.service.FileService;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import com.knud4.an.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final FileService fileService;

    private final RedisUtil redisUtil;
    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final AccountRepository accountRepository;
    private final HouseRepository houseRepository;
    private final LineRepository lineRepository;

    private final String VERIFIED_PREFIX = "vf::";

    private void validateAccountEmailDuplicated(String email) {
        if (accountRepository.accountExistsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }
    }

    private void validateAccountUsernameDuplicated(String username) {
        if (accountRepository.accountExistsByUsername(username)) {
            throw new IllegalStateException("이미 존재하는 아이디 입니다.");
        }
    }

    @Transactional
    public Long signUpAccount(SignUpAccountForm form) throws RuntimeException {
        Boolean isVerified = (Boolean) redisUtil.get(VERIFIED_PREFIX + form.getEmail());
        if (isVerified == null || !isVerified) {
            throw new NotAuthenticatedException("이메일 인증이 필요합니다.");
        }

        validateAccountEmailDuplicated(form.getEmail());
        validateAccountUsernameDuplicated(form.getUsername());

        Line line = lineRepository.findByName(form.getLineName())
                .orElseThrow(() -> new NotFoundException("라인을 찾을 수 없습니다."));

        House house = houseRepository.findByLineIdAndHouseName(line.getId(), form.getHouseName())
                .orElseThrow(() -> new NotFoundException("세대를 찾을 수 없습니다."));

        validatePassword(form.getPasswd());

        Account account = Account.builder()
                .username(form.getUsername())
                .password(passwordEncoder.encode(form.getPasswd()))
                .email(form.getEmail())
                .line(line)
                .house(house)
                .role(Role.ROLE_USER)
                .build();

        accountRepository.saveAccount(account);
        redisUtil.del(VERIFIED_PREFIX+form.getEmail());

        return account.getId();
    }

    public Account signInAccount(SignInAccountForm form) throws NotFoundException {
        Account findAccount = accountRepository.findAccountByUsername(form.getUsername())
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

        if (!passwordEncoder.matches(form.getPasswd(), findAccount.getPassword())) {
            throw new NotAuthenticatedException("비밀번호가 잘못되었습니다.");
        }

        return findAccount;
    }

    @Transactional
    public Long AddProfile(AddProfileForm form, String accountEmailFromToken) throws RuntimeException, IOException {

        Account account = accountRepository.findAccountByEmail(accountEmailFromToken)
                .orElseThrow(() -> new NotFoundException("계정이 존재하지 않습니다."));

        if (accountRepository.profileExistsByName(form.getName(), account.getId())) {
            throw new IllegalStateException("프로필 이름이 중복되었습니다");
        }

        Profile profile = Profile.builder()
                .name(form.getName())
                .account(account)
                .pin(passwordEncoder.encode(form.getPin()))
                .age(form.getAge())
                .gender(form.getGender())
                .build();

        accountRepository.saveProfile(profile);

        if (form.getImg() != null) {
            String url = fileService.uploadPNG(profile.getId() + "", form.getImg().getBytes());
            profile.updateImg(url);
        }

        return profile.getId();
    }

    public Profile signInProfile(SignInProfileForm form, String accountEmailFromToken) throws RuntimeException{

        Account account = accountRepository.findAccountByEmail(accountEmailFromToken)
                .orElseThrow(() -> new NotAuthenticatedException("계정 정보가 잘못되었습니다."));

        Profile findProfile = accountService.findProfileById(form.getProfileId());
        if (findProfile == null || !findProfile.getAccount().getId().equals(account.getId())) {
            throw new NotFoundException("프로필을 찾을 수 없습니다.");
        }

        if (!passwordEncoder.matches(form.getPin(), findProfile.getPin())) {
            throw new NotFoundException("핀 번호가 잘못되었습니다.");
        }

        return findProfile;
    }

    public void verifySignUpCode(String email, String code) throws NotFoundException {
        String savedCode = (String) redisUtil.get(email);
        if (!code.equals(savedCode)) {
            throw new NotFoundException("코드가 잘못되었습니다.");
        } else {
            redisUtil.del(email);
            redisUtil.set(VERIFIED_PREFIX + email, true);
            redisUtil.expire(VERIFIED_PREFIX + email, 3600);
        }
    }

    public Profile signInManager(SignInAccountForm form) {
        Account account = signInAccount(form);

        if (!account.getRole().equals(Role.ROLE_MANAGER)) {
            throw new IllegalStateException("매니저 계정이 아닙니다.");
        }

        Profile profile = accountRepository.findProfilesByAccountId(account.getId())
                .stream().findFirst().orElseThrow(() -> new NotFoundException("매니저 프로필 누락"));

        return profile;
    }

    private void validatePassword(String password) {
        int MIN_SIZE = 8;
        int MAX_SIZE = 50;

        String regexPassword = "^(?=.*[A-Za-z])(?=.*[0-9])[A-Za-z[0-9]]{" + MIN_SIZE
                + "," + MAX_SIZE + "}$";
        Pattern PATTERN = Pattern.compile(regexPassword);

        if (!PATTERN.matcher(password).matches())
                throw new IllegalStateException(MIN_SIZE + "자 이상의 "+ MAX_SIZE + "자 이하의 숫자, 영문자를 포함한 비밀번호를 입력해주세요");
    }
}
