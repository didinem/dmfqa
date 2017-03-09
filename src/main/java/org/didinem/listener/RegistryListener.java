package org.didinem.listener;

import com.alibaba.dubbo.common.URL;
import com.google.common.cache.LoadingCache;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.didinem.sconf.DubboProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@Component
public class RegistryListener implements InitializingBean {

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private LoadingCache<String, String> loadingCache;

    public RegistryListener() {
    }

    private void init() {
        String serverString = dubboProperties.getAddress();
        ZkClient zkClient = new ZkClient(serverString);
        String dubboRootPath = "/dubbo";
        List<String> pathList = zkClient.getChildren(dubboRootPath);
        for (String path : pathList) {
            String providerPath = dubboRootPath + "/" + path + "/providers";
            System.out.println("start to listen:" + providerPath);
            zkClient.subscribeChildChanges(providerPath, new IZkChildListener() {
                public void handleChildChange(String s, List<String> list) throws Exception {
                    System.out.println(s);
                    System.out.println("=================================================");
                    for (String string : list) {
                        System.out.println("url is " + string);
                        if (StringUtils.isNotEmpty(string)) {
                            System.out.println("url is " + string);
                            URL url = URL.valueOf(URL.decode(string));
                            Map<String, String> map = url.toMap();
                            for (String key : map.keySet()) {
                                System.out.println(String.format("Key is %s and value is %s", key, map.get(key)));
                            }
                        }
                    }
                }
            });
        }


    }


    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }
}
