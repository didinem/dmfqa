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

    }

    public String get(String key) {
        return null;
    }

    public void push(String key, String... value) {

    }

    public void push(String key, List<String> list) {

    }


    public List<String> getList(String key) {
        return null;
    }

    public boolean isExistKey(String key) {
        return false;
    }

    public void delete(String key) {

    }
}
