package org.didinem.listener;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhongzhengmin on 2017/3/8.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistryListenerTest {

    @Autowired
    private RegistryListener registryListener;

    @Test
    public void init() {

    }

}
