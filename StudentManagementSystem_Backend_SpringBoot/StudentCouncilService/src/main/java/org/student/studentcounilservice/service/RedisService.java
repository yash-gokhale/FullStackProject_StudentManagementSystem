package org.student.studentcounilservice.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void update(String key, Object newValue) {
        // Only overwrite if key exists
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().set(key, newValue);
        }
    }

    public void savePermanent(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void deleteAll() {
        assert redisTemplate.getConnectionFactory() != null;
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushDb();
    }

    public Long getSize() {
        assert redisTemplate.getConnectionFactory() != null;
        return redisTemplate.getConnectionFactory().getConnection().serverCommands().dbSize();
    }

    public Long getKeyRemainingTime(String key) {
        return redisTemplate.getExpire(key); // returns seconds
    }
}
