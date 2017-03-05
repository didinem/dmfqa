package org.didinem.analyze;

import org.apache.commons.lang3.StringUtils;
import org.didinem.handle.CacheHandler;
import org.objectweb.asm.Opcodes;

import java.util.List;

/**
 * Created by didinem on 3/5/2017.
 */
public class Finder {

    private CacheHandler cacheHandler;

    public static void main(String[] args) {
        String rootKey = "com/lvtu/service/api/rop/service/ship/ClientShipProductServiceImpl:getCategoryCruiseList:(Lcom/lvmama/vst/api/compship/prod/vo/CompShipProductVo;Ljava/util/Date;Z)Ljava/util/List;";

        Finder find = new Finder();
        find.findDen(rootKey, "");

    }

    public void findDen(String rootKey, String prefix) {
        // 所有的直接依赖
        if (rootKey.equals("com/lvtu/dao/ship/IShipGoodsDao:findRopShipGoodsBaseBySuppGoodsIdList:(Ljava/util/List;)Ljava/util/List;")) {
            int a = 1;
            int b = a;
        }
        try {
            // 此方法调用是否是接口调用
            String opKey = rootKey + ":OPCODE";
            Integer opcode = Integer.valueOf(cacheHandler.get(opKey));
            if (opcode != null && opcode == Opcodes.INVOKEINTERFACE) {
                String[] rootkeyArray = rootKey.split(":");
                String implementsKey = rootkeyArray[0] + ":implements";
                List<String> implementsClassKeys = cacheHandler.getList(implementsKey);
                System.out.println(prefix + rootKey);
                for (Object object : implementsClassKeys) {
                    if (object == null) {
                        continue;
                    }
                    rootkeyArray[0] = String.valueOf(object);
                    String implementsMethodKey = StringUtils.join(rootkeyArray, ":");
                    findDen(implementsMethodKey, prefix);
                }
            } else {
                String dependentKey = rootKey + ":" + "denpendent";
                if (cacheHandler.isExistKey(dependentKey)) {
                    List<String> directDenpendencyMethodList = cacheHandler.getList(dependentKey);
                    System.out.println(prefix + rootKey);
                    for (Object object : directDenpendencyMethodList) {
                        if (object == null) {
                            continue;
                        }
                        String methodKey = String.valueOf(object);
                        findDen(methodKey, prefix + "\t");
                    }
                } else {
                    System.out.println(prefix + rootKey);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
