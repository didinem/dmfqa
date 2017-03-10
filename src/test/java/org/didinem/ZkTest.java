package org.didinem;

import org.I0Itec.zkclient.ZkClient;
import org.apache.commons.lang3.StringUtils;
import org.didinem.sprops.DubboProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by zhongzhengmin on 2017/3/10.
 */
public class ZkTest {

    @Autowired
    private DubboProperties dubboProperties;

    @Autowired
    private ZkClient zkClient;

    public void test1() {
        String internalClassName = "com/lvmama/vst/api/biz/service/VstDistrictService";
        String interfaceName = internalClassName.replaceAll("/", "\\.");
        String providerPath = dubboProperties.getSeperator() + StringUtils.joinWith(dubboProperties.getSeperator(), dubboProperties.getRoot(), interfaceName, dubboProperties.getProvider());
        List<String> providerUrlList = zkClient.getChildren(providerPath);
        for (String string : providerUrlList) {
            System.out.println(string);
        }
    }

}
