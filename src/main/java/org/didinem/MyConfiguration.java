package org.didinem;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhongzhengmin on 2017/3/9.
 */
@Configuration
@EnableConfigurationProperties({DubboProperties.class})
public class MyConfiguration {
}
