package org.didinem.sconf;

import org.I0Itec.zkclient.ZkClient;
import org.didinem.sprops.DubboProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by didinem on 3/9/2017.
 */
@Configuration
public class ZooKeeperConfiguration {

    @Autowired
    private DubboProperties dubboProperties;

    @Bean("zkClient")
    public ZkClient getZkClient() {
        String serverString = dubboProperties.getAddress();
        ZkClient zkClient = new ZkClient(serverString);
        return zkClient;
    }

}
