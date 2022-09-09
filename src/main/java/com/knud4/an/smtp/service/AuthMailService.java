package com.knud4.an.smtp.service;

import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 메일 서버 서비스
 * <p>지정한 smtp 서버에서 클라이언트에게 메일을 발송</p>
 * @see JavaMailSender
 */
@Service
@RequiredArgsConstructor
public class AuthMailService {
    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;

    private final AccountRepository accountRepository;

    @Value("${group.email}")
    private String SERVER_EMAIL;

    private final String RESEND_MAIL_PREFIX = "re::";

    /**
     * 인증 코드 발송
     * @param email 인증 코드 수신 메일 주소
     * @throws IllegalStateException
     *          <p>인증 코드가 이미 발송되었을 때</p>
     */
    public void sendAuthenticationCode(String email) throws IllegalStateException{
        if (redisUtil.get(email) == null) {
            doSendAuthenticationCode(email, false);
        } else {
            throw new IllegalStateException("이미 인증코드가 발송되었습니다.");
        }
    }

    /**
     * 인증 코드 재발송
     * @param email 인증 코드 수신 메일 주소
     * @throws IllegalStateException
     *          <p>인증 코드가 이미 재발송되었을 때</p>
     */
    public void resendAuthenticationCode(String email) throws IllegalStateException{
        if (redisUtil.get(RESEND_MAIL_PREFIX+email) == null) {
            doSendAuthenticationCode(email, true);
        } else {
            throw new IllegalStateException("잠시 후에 다시 시도해주세요.");
        }
    }

    /**
     * 서버의 인증 메일 전송 로직
     * <p>서버 스펙에서 정의하는 유효기간 만큼 인증코드를 저장</p>
     * @param email 인증 코드 수신 메일 주소
     * @param resend 재발송 여부
     * @throws IllegalStateException
     *          수신자 email 로 가입한 회원이 이미 존재할 때
     */
    private void doSendAuthenticationCode(String email, boolean resend) throws IllegalStateException{
        if (accountRepository.accountExistsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }

        String code = generateCode(4);

        redisUtil.set(email, code);
        redisUtil.expire(email, 300);

        if (resend) {
            String reSendMail = RESEND_MAIL_PREFIX + email;
            redisUtil.set(reSendMail, true);
            redisUtil.expire(reSendMail, 300);
        }

        SimpleMailMessage message = generateAuthenticationMail(email, code);
        mailSender.send(message);
    }

    /**
     * 서버 스펙 인증메일 제공
     * @param to 수신자 이메일 주소
     * @param code 인증 코드
     * @see SimpleMailMessage
     */
    private SimpleMailMessage generateAuthenticationMail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(SERVER_EMAIL);
        message.setSubject("이웃사이 인증 코드 입니다.");
        message.setText("인증 코드: " + code);

        return message;
    }

    /**
     * 인증 코드 제공
     * @param length 인증 코드 길이
     */
    private String generateCode(int length) {
        int leftLimit = 48;
        int rightLimit = 122;

        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString().toUpperCase();
    }
}
