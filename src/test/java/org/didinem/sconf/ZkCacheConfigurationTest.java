package org.didinem.sconf;

import com.google.common.cache.LoadingCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

/**
 * Created by zhongzhengmin on 2017/3/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ZkCacheConfigurationTest {

    @Autowired
    private LoadingCache<String, String> loadingCache;

    @Test
    public void getLoadingCache() throws ExecutionException {
        String interfaceName = "com/lvmama/vst/api/biz/service/VstDistrictService";
        String str = loadingCache.get(interfaceName);
        System.out.println(str);
    }

}
