package com.knud4.an.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Redis 서버 동작 보조 클래스
 * {@link com.knud4.an.config.RedisConfig#redisTemplate(RedisConnectionFactory) RedisTemplate}
 *  을 바탕으로 Redis 서버 동작
 * @see com.knud4.an.config.RedisConfig
 */
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    public void expire(String key, long seconds) {
        redisTemplate.expire(key, Duration.ofSeconds(seconds));
    }
    public void del(String key) {
        redisTemplate.delete(key);
    }
}
