package org.didinem.sconf;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.didinem.constant.DubboProviderStatusEnum;
import org.didinem.sprops.DubboProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Created by zhongzhengmin on 2017/3/10.
 */
@Configuration
public class ZkCacheConfiguration {

    @Bean("loadingCache")
    public LoadingCache<String, String> getLoadingCache(ZkClient zkClient, DubboProperties dubboProperties) {
        LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String s) throws Exception {
                String interfaceName = s.replaceAll("/", "\\.");
                String interfaceNode = dubboProperties.getSeperator() + interfaceName;
                String providerPath = StringUtils.join(dubboProperties.getRoot(), interfaceNode, dubboProperties.getProvider());
                List<String> providerUrlList = zkClient.getChildren(providerPath);
                if (CollectionUtils.isEmpty(providerUrlList)) {
                    return DubboProviderStatusEnum.NOT_PROVIDED.getCode();
                }
                return DubboProviderStatusEnum.PROVIDED.getCode();
            }
        });
        return loadingCache;
    }

}
