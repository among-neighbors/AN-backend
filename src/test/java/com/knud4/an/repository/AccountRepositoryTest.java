package com.knud4.an.repository;

import com.knud4.an.account.entity.Account;
import com.knud4.an.account.entity.Gender;
import com.knud4.an.account.entity.Profile;
import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.house.entity.House;
import com.knud4.an.house.repository.HouseRepository;
import com.knud4.an.line.entity.Line;
import com.knud4.an.line.repository.LineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

@SpringBootTest
@Transactional
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private LineRepository lineRepository;

    Line line;
    House house;


    @BeforeEach
    public void initDB() {
        this.line = Line.builder()
                .name("103")
                .build();

        lineRepository.save(line);

        this.house = House.builder()
                .name("2000")
                .build();

        Line findLine = lineRepository.findByName("103");

        house.setLine(findLine);

        houseRepository.save(house);
    }

    @Test
    public void createAccount() {
//      예외처리 필요

        Line findLine = lineRepository.findByName("103");
        House findHouse = houseRepository.findByName(findLine.getId(), "2000");

//        이메일 인증
//        ID 중복 확인
//        비밀번호 암호화 이후 저장

        Account account = Account.builder()
                .email("abc@abc.com")
                .house(findHouse)
                .line(findHouse.getLine())
                .username("sampleId123")
                .password("1234")
                .build();

        accountRepository.saveAccount(account);

        Optional<Account> findId = accountRepository.findAccountByUsername("sampleId123");

        assertThat(findId.get()).isEqualTo(account);

    }

    @Test
    public void addProfile() {
        Line findLine = lineRepository.findByName("103");
        House findHouse = houseRepository.findByName(findLine.getId(), "2000");

        Account account = Account.builder()
                .email("abc@abc.com")
                .house(findHouse)
                .line(findHouse.getLine())
                .username("sampleId123")
                .password("1234")
                .build();

        accountRepository.saveAccount(account);

        Profile profile = Profile.builder()
                .name("user1")
                .age(12)
                .account(account)
                .pin("1234")
                .gender(Gender.MALE)
                .build();

//        account Token 검증

        accountRepository.saveProfile(profile);

        Optional<Profile> findProfile = accountRepository.findProfileByID(profile.getId());

        assertThat(findProfile.get()).isEqualTo(profile);
    }


}
