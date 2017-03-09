package org.didinem.handle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by didinem on 3/5/2017.
 */
@Component
public class RedisHandler implements CacheHandler {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value) {
        if (isExistKey(key)) {
            delete(key);
        }
        System.out.println(key);
        redisTemplate.opsForValue().set(key, value);
    }

    public String get(String key) {
        return String.valueOf(redisTemplate.opsForValue().get(key));
    }

    public void push(String key, String... value) {
        if (isExistKey(key)) {
            delete(key);
        }
        System.out.println(key);
        redisTemplate.opsForList().rightPushAll(key, value);
    }

    public void push(String key, List<String> list) {
        if (isExistKey(key)) {
            delete(key);
        }
        System.out.println(key);
        redisTemplate.opsForList().rightPushAll(key, list);
    }


    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public boolean isExistKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
        System.out.println("delete key:" + key);
    }
}
