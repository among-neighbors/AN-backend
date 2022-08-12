package com.knud4.an.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.knud4.an.security.details.AccountDetails;
import com.knud4.an.security.details.AccountDetailsService;
import lombok.RequiredArgsConstructor;
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

    private static final long TOKEN_VALIDATION_SECOND = 1000L * 60 * 120;
    private static final long REFRESH_TOKEN_VALIDATION_TIME = 1000L * 60 * 60 * 48;

    public static final String ACCOUNT_TOKEN_NAME = "account_token";
    public static final String PROFILE_TOKEN_NAME = "profile_token";


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
        DecodedJWT verifiedToken = validateToken(token);
        return verifiedToken.getClaim("email").asString();
    }

    public Long getProfileIdFromToken(String token) {
        DecodedJWT verifiedToken = validateToken(token);
        return verifiedToken.getClaim("profile_id").asLong();
    }

    private JWTVerifier getTokenValidator() {
        return JWT.require(getSigningKey(SECRET_KEY))
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

    public String generateProfileToken(String profileId) {
        Map<String, String> payload = new HashMap<>();
        payload.put("profile_id", profileId);
        return doGenerateToken(TOKEN_VALIDATION_SECOND, payload);
    }

    private String doGenerateToken(long expireTime, Map<String, String> payload) {

        return JWT.create()
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expireTime))
                .withPayload(payload)
                .withIssuer(ISSUER)
                .sign(getSigningKey(SECRET_KEY));
    }

    private DecodedJWT validateToken(String token) throws JWTVerificationException {
        JWTVerifier validator = getTokenValidator();
        return validator.verify(token);
    }

    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT decodedJWT = validateToken(token);
            return false;
        } catch (JWTVerificationException e) {
            return true;
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AccountDetails userDetails = (AccountDetails) accountDetailsService.loadUserByUsername
                ((String) authentication.getPrincipal());


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
