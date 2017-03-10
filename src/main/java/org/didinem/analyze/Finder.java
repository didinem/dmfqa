package org.didinem.analyze;

import org.apache.commons.lang3.StringUtils;
import org.didinem.handle.CacheHandler;
import org.didinem.util.RedisKeyGenerater;
import org.objectweb.asm.Opcodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by didinem on 3/5/2017.
 */
@Component
public class Finder {

    @Autowired
    private CacheHandler cacheHandler;

    public void findDen(String methodKey, String prefix) {
        // 所有的直接依赖
        try {
            // 此方法调用是否是接口调用
            // TODO 多个接口group
            String invokeTypeKey = RedisKeyGenerater.generateInvokeTypeKey(methodKey);
            if (cacheHandler.isExistKey(invokeTypeKey) && isInvokeInterface(invokeTypeKey)) {
                String[] rootkeyArray = methodKey.split(":");
                String implementsKey = RedisKeyGenerater.generateImplementKey(rootkeyArray[0]);
                List<Object> implementsClassKeys = cacheHandler.getList(implementsKey);
                System.out.println(prefix + methodKey);
                for (Object object : implementsClassKeys) {
                    if (object == null) {
                        continue;
                    }
                    rootkeyArray[0] = String.valueOf(object);
                    String implementsMethodKey = StringUtils.join(rootkeyArray, ":");
                    findDen(implementsMethodKey, prefix);
                }
            } else {
                String dependentKey = RedisKeyGenerater.generateDenpendentKey(methodKey);
                if (cacheHandler.isExistKey(dependentKey)) {
                    List<Object> directDenpendencyMethodList = cacheHandler.getList(dependentKey);
                    System.out.println(prefix + methodKey);
                    for (Object object : directDenpendencyMethodList) {
                        if (object == null) {
                            continue;
                        }
                        String directDenpendencyMethodKey = String.valueOf(object);
                        findDen(directDenpendencyMethodKey, prefix + "\t");
                    }
                } else {
                    System.out.println(prefix + methodKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isInvokeInterface(String invokeTypeKey) {
        String strOpcode = cacheHandler.get(invokeTypeKey);
        if (StringUtils.isNumeric(strOpcode)) {
            Integer opcode = Integer.valueOf(strOpcode);
            return opcode == Opcodes.INVOKEINTERFACE;
        }
        return false;
    }



}
