package org.didinem.web.rest;

import com.google.common.cache.LoadingCache;
import org.didinem.analyze.Finder;
import org.didinem.handle.CacheHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * Created by didinem on 3/5/2017.
 */
@RestController
public class TestController {

    @Autowired
    private Finder finder;

    @Autowired
    private CacheHandler cacheHandler;

    @Autowired
    private LoadingCache<String, String> loadingCache;

    @RequestMapping("/test")
    public String test() {
        String rootKey = "com/lvtu/service/api/rop/service/ship/ClientShipProductServiceImpl:getCategoryCruiseList:(Lcom/lvmama/vst/api/compship/prod/vo/CompShipProductVo;Ljava/util/Date;Z)Ljava/util/List;";
        String prefix = "";
        finder.findDen(rootKey, prefix);
        return "success";
    }

    @RequestMapping("/test2")
    public String test2() {
        cacheHandler.set("qwe", "aaa");
        return "success";
    }

    @RequestMapping("/test3")
    public String test3(String interfaceName) {
        String result = "-1";
        String internalClassName = interfaceName.replaceAll("\\.", "\\/");
        try {
            result = loadingCache.get(internalClassName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

}
