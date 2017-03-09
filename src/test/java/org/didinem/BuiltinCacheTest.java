package org.didinem;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BuiltinCacheTest {

    @Autowired
    private LoadingCache<String, String> cache;

    @Test
    public void cacheTest() throws ExecutionException {
        cache.put("aaa", "bbb");
        System.out.println(cache.get("aaa"));
    }

}
