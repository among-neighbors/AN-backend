package com.knud4.an.smtp.service;

import com.knud4.an.account.repository.AccountRepository;
import com.knud4.an.utils.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthMailService {
    private final RedisUtil redisUtil;
    private final JavaMailSender mailSender;

    private final AccountRepository accountRepository;

    @Value("${group.email}")
    private String SERVER_EMAIL;

    private final String RESEND_MAIL_PREFIX = "re::";

    public void sendAuthenticationCode(String email) throws IllegalStateException{
        if (redisUtil.get(email) == null) {
            doSendAuthenticationCode(email, false);
        } else {
            throw new IllegalStateException("이미 인증코드가 발송되었습니다.");
        }
    }

    public void resendAuthenticationCode(String email) throws IllegalStateException{
        if (redisUtil.get(RESEND_MAIL_PREFIX+email) == null) {
            doSendAuthenticationCode(email, true);
        } else {
            throw new IllegalStateException("잠시 후에 다시 시도해주세요.");
        }
    }

    private void doSendAuthenticationCode(String email, boolean resend) throws IllegalStateException{
        if (accountRepository.accountExistsByEmail(email)) {
            throw new IllegalStateException("이미 존재하는 이메일 입니다.");
        }
//        인증 코드 생성
        String code = generateCode(4);
//        레디스 캐싱 (email, 인증코드)
        redisUtil.set(email, code);
        redisUtil.expire(email, 300);

        if (resend) {
            String reSendMail = RESEND_MAIL_PREFIX + email;
            redisUtil.set(reSendMail, true);
            redisUtil.expire(reSendMail, 300);
        }
//        인증 메일 발송
        SimpleMailMessage message = generateAuthenticationMail(email, code);
        mailSender.send(message);
    }

    private SimpleMailMessage generateAuthenticationMail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setFrom(SERVER_EMAIL);
        message.setSubject("인증 코드");
        message.setText("인증 코드: " + code);

        return message;
    }
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
