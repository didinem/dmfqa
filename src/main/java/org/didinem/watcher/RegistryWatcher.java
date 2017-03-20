package org.didinem.watcher;

import com.google.common.cache.LoadingCache;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.didinem.constant.DubboProviderStatusEnum;
import org.didinem.sprops.DubboProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@Component
public class RegistryWatcher implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(RegistryWatcher.class);

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private LoadingCache<String, String> loadingCache;

    @Autowired
    private Set<String> providerSet;

    @Autowired
    private ZkClient zkClient;

    private void initListener() {
        String dubboRootPath = dubboProperties.getRoot();
        List<String> childrenList = zkClient.getChildren(dubboRootPath);
        if (CollectionUtils.isNotEmpty(childrenList)) {
            for (String qualifiedClassName : childrenList) {
                String interfaceNode = dubboProperties.getSeperator() + qualifiedClassName;
                String providerPath = StringUtils.join(dubboRootPath, interfaceNode, dubboProperties.getProvider());
                logger.info("start to listen:" + providerPath);
                List<String> providerUrlList = zkClient.getChildren(providerPath);
                if (CollectionUtils.isEmpty(providerUrlList)) {
                    loadingCache.put(qualifiedClassName, DubboProviderStatusEnum.NOT_PROVIDED.getCode());
                    continue;
                }
                loadingCache.put(qualifiedClassName, DubboProviderStatusEnum.PROVIDED.getCode());
                // 初始化本地服务提供者集合
                providerSet.add(qualifiedClassName);
                zkClient.subscribeChildChanges(providerPath, (s, list) -> {
                    logger.info(s);
                    String[] zkNodes = s.split("/");
                    String providerStatus = DubboProviderStatusEnum.PROVIDED.getCode();
                    if (CollectionUtils.isEmpty(list)) {
                        providerStatus = DubboProviderStatusEnum.NOT_PROVIDED.getCode();
                    }
                    loadingCache.put(zkNodes[2], providerStatus);
                });
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initListener();
    }
}
