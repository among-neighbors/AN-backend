package com.knud4.an.utils.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Set;

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

    public void rPush(String key, List<String> values) {
        redisTemplate.opsForList().rightPushAll(key, values);
    }

    public List<Object> lRange(String key, int start, int end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public void sAdd(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    public void sRem(String key, Object value) {
        redisTemplate.opsForSet().remove(key, value);
    }

    public Boolean sIsMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public Boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

}
