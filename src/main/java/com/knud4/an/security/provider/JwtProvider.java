package com.knud4.an.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.knud4.an.security.details.AccountDetails;
import com.knud4.an.security.details.AccountDetailsService;
import com.knud4.an.security.details.ProfileDetailsService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtProvider implements AuthenticationProvider {

    private final AccountDetailsService accountDetailsService;
    private final ProfileDetailsService profileDetailsService;

    private static final long TOKEN_VALIDATION_SECOND = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_VALIDATION_TIME = 1000L * 60 * 60 * 48;

    public static final String ACCOUNT_TOKEN_NAME = "account_refresh_token";
    public static final String PROFILE_TOKEN_NAME = "profile_refresh_token";


    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Value("${group.name}")
    private String ISSUER;

    private Algorithm getSigningKey(String secretKey) {
        return Algorithm.HMAC256(secretKey);
    }

    private Map<String, Claim> getAllClaims(DecodedJWT token) {
        return token.getClaims();
    }

    public String getEmailFromToken(String token) {
        DecodedJWT verifiedToken = validateToken(token, getAccountTokenValidator());
        String result = verifiedToken.getClaim("email").asString();
        return result;
    }

    public Long getAccountIdFromToken(String token) {
        DecodedJWT verifiedToken = validateToken(token, getAccountTokenValidator());
        String result = verifiedToken.getClaim("account_id").asString();
        return Long.parseLong(result);
    }

    public Long getProfileIdFromToken(String token) {
        DecodedJWT verifiedToken = validateToken(token, getProfileTokenValidator());
        String result = verifiedToken.getClaim("profile_id").asString();
        return Long.parseLong(result);
    }

    private JWTVerifier getAccountTokenValidator() {
        return JWT.require(getSigningKey(SECRET_KEY))
                .withClaimPresence("email")
                .withClaimPresence("account_id")
                .withIssuer(ISSUER)
                .build();
    }

    private JWTVerifier getAccountRefreshTokenValidator() {
        return JWT.require(getSigningKey(SECRET_KEY))
                .withClaimPresence("email")
                .withClaimPresence("account_id")
                .acceptExpiresAt(REFRESH_TOKEN_VALIDATION_TIME)
                .withIssuer(ISSUER)
                .build();
    }

    private JWTVerifier getProfileTokenValidator() {
        return JWT.require(getSigningKey(SECRET_KEY))
                .withClaimPresence("email")
                .withClaimPresence("account_id")
                .withClaimPresence("profile_id")
                .withIssuer(ISSUER)
                .build();
    }

    private JWTVerifier getProfileRefreshTokenValidator() {
        return JWT.require(getSigningKey(SECRET_KEY))
                .withClaimPresence("email")
                .withClaimPresence("account_id")
                .withClaimPresence("profile_id")
                .acceptExpiresAt(REFRESH_TOKEN_VALIDATION_TIME)
                .withIssuer(ISSUER)
                .build();
    }

    public String generateAccountToken(String email, String accountId) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("account_id", accountId);
        return doGenerateToken(TOKEN_VALIDATION_SECOND, payload);
    }

    public String generateAccountRefreshToken(String email, String accountId) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", email);
        payload.put("account_id", accountId);
        return doGenerateToken(REFRESH_TOKEN_VALIDATION_TIME, payload);
    }

    public String generateProfileToken(String accountEmail, String accountId, String profileId) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", accountEmail);
        payload.put("profile_id", profileId);
        payload.put("account_id", accountId);
        return doGenerateToken(TOKEN_VALIDATION_SECOND, payload);
    }

    public String generateProfileRefreshToken(String accountEmail, String accountId, String profileId) {
        Map<String, String> payload = new HashMap<>();
        payload.put("email", accountEmail);
        payload.put("profile_id", profileId);
        payload.put("account_id", accountId);
        return doGenerateToken(REFRESH_TOKEN_VALIDATION_TIME, payload);
    }

    public String reIssueAccountToken(String refreshToken) {
        DecodedJWT decodedJWT = validateToken(refreshToken, getAccountRefreshTokenValidator());

        String email = decodedJWT.getClaim("email").asString();
        String accountId = decodedJWT.getClaim("account_id").asString();

        return generateAccountToken(email, accountId);
    }

    public String reIssueAccountRefreshToken(String refreshToken) {
        // 아예 invalid 한 경우(refresh 토큰이 아니거나 만료된 경우에는 예외)
        try {
            validateToken(refreshToken, getAccountTokenValidator());
        } catch (InvalidClaimException e) {
            throw new IllegalStateException("토큰이 올바르지 않습니다.");
        } catch (TokenExpiredException e) {
            try {
                DecodedJWT decodedJWT = validateToken(refreshToken, getAccountRefreshTokenValidator());
                String email = decodedJWT.getClaim("email").asString();
                String accountId = decodedJWT.getClaim("account_id").asString();
                return generateAccountRefreshToken(email, accountId);
            } catch (TokenExpiredException e2) {
                throw new IllegalStateException("토큰이 만료되었습니다.");
            }
        }
        return null;
    }

    public String reIssueProfileToken(String refreshToken) {
        DecodedJWT decodedJWT = validateToken(refreshToken, getProfileRefreshTokenValidator());

        String email = decodedJWT.getClaim("email").asString();
        String accountId = decodedJWT.getClaim("account_id").asString();
        String profileId = decodedJWT.getClaim("profile_id").asString();

        return generateProfileToken(email, accountId, profileId);
    }

    public String reIssueProfileRefreshToken(String refreshToken) {
        try {
            validateToken(refreshToken, getProfileTokenValidator());
        } catch (InvalidClaimException e) {
            throw new IllegalStateException("토큰이 올바르지 않습니다.");
        } catch (TokenExpiredException e) {
            try {
                DecodedJWT decodedJWT = validateToken(refreshToken, getProfileRefreshTokenValidator());
                String email = decodedJWT.getClaim("email").asString();
                String accountId = decodedJWT.getClaim("account_id").asString();
                String profileId = decodedJWT.getClaim("profile_id").asString();
                return generateProfileRefreshToken(email, accountId, profileId);
            } catch (TokenExpiredException e2) {
                throw new IllegalStateException("토큰이 만료되었습니다.");
            }
        }

        return null;
    }

    private String doGenerateToken(long expireTime, Map<String, String> payload) {

        return JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .withPayload(payload)
                .withIssuer(ISSUER)
                .sign(getSigningKey(SECRET_KEY));
    }

    private DecodedJWT validateToken(String token, JWTVerifier validator) throws JWTVerificationException {
        return validator.verify(token);
    }

    public boolean isAccountTokenExpired(String token) {
        try {
            validateToken(token, getAccountTokenValidator());
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    public boolean isProfileTokenExpired(String token) {
        try {
            validateToken(token, getProfileTokenValidator());
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccountDetails userDetails;

        if (authentication.getCredentials().equals(Strings.EMPTY)) {
             userDetails = (AccountDetails) accountDetailsService
                     .loadUserByUsername((String) authentication.getPrincipal());
        } else {
            userDetails = (AccountDetails) profileDetailsService
                    .loadUserByUsername(authentication.getCredentials().toString());
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails.getEmail(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
