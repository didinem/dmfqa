package org.didinem.listener;

import com.google.common.cache.LoadingCache;
import org.I0Itec.zkclient.IZkChildListener;
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

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@Component
public class RegistryListener implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(RegistryListener.class);

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private LoadingCache<String, String> loadingCache;

    @Autowired
    private ZkClient zkClient;

    private void initListener() {
        String dubboRootPath = dubboProperties.getRoot();
        List<String> interfaceNameList = zkClient.getChildren(dubboRootPath);
        if (CollectionUtils.isNotEmpty(interfaceNameList)) {
            for (String interfaceName : interfaceNameList) {
                String interfaceNode = dubboProperties.getSeperator() + interfaceName;
                String providerPath = StringUtils.join(dubboRootPath, interfaceNode, dubboProperties.getProvider());
                logger.info("start to listen:" + providerPath);
                List<String> providerUrlList = zkClient.getChildren(providerPath);
                String internalClassName = interfaceName.replaceAll("\\.", "\\/");
                if (CollectionUtils.isEmpty(providerUrlList)) {
                    loadingCache.put(internalClassName, DubboProviderStatusEnum.NOT_PROVIDED.getCode());
                    continue;
                }
                loadingCache.put(internalClassName, DubboProviderStatusEnum.PROVIDED.getCode());
                zkClient.subscribeChildChanges(providerPath, new IZkChildListener() {
                    public void handleChildChange(String s, List<String> list) throws Exception {
                        logger.info(s);
                        String[] zkNodes = s.split("/");
                        String qualifiedClassName = zkNodes[2];
                        String internalClassName = qualifiedClassName.replaceAll("\\.", "\\/");
                        String providerdStatus = DubboProviderStatusEnum.PROVIDED.getCode();
                        if (CollectionUtils.isEmpty(list)) {
                            providerdStatus = DubboProviderStatusEnum.NOT_PROVIDED.getCode();
                        }
                        loadingCache.put(internalClassName, providerdStatus);
                    }
                });
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        initListener();
    }
}
