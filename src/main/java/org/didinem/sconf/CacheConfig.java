package org.didinem.sconf;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@Configuration
public class CacheConfig {

    @Bean("dubboProviderCache")
    public LoadingCache getDubboProviderCache() {
        LoadingCache<String, String> builtinCache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                return null;
            }
        });
        return builtinCache;
    }

}
