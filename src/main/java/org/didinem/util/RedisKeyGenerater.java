package org.didinem.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 生成redis的key
 * 方法key由the internal name of the class、the method's name、the method's descriptor这三部分组成，是一个方法的唯一标识
 * 调用方法方式key由方法key和OPCODE后缀，这两部分组成
 * 方法直接依赖的方法key由方法key和DENPENDENT后缀，这两部分组成
 * 接口与实现类关系key由the internal names of the class's interface和IMPLEMENTS后缀，这两部分组成
 *
 * Created by didinem on 3/5/2017.
 */
public class RedisKeyGenerater {

    private static final String SEPERATOR = ":";

    private static final String INVOKETYPE_SUFFIX = "OPCODE";

    private static final String DENPENDENT_SUFFIX = "DENPENDENT";

    private static final String IMPLEMENTS_SUFFIX = "IMPLEMENTS";

    /**
     * 生成方法key
     * @param classInternalName the internal name of the class
     * @param methodName        the method's name
     * @param methodDescriptor  the method's descriptor
     * @return 方法key
     */
    public static String generateMethodKey(String classInternalName, String methodName, String methodDescriptor) {
        return StringUtils.joinWith(SEPERATOR, classInternalName, methodName, methodDescriptor);
    }

    /**
     * 生成调用方法方式key
     * @param classInternalName the internal name of the class
     * @param methodName        the method's name
     * @param methodDescriptor  the method's descriptor
     * @return 调用方法方式key
     */
    public static String generateInvokeTypeKey(String classInternalName, String methodName, String methodDescriptor) {
        String methodKey = generateMethodKey(classInternalName, methodName, methodDescriptor);
        return generateInvokeTypeKey(methodKey);
    }

    /**
     * 生成调用方法方式key
     * @param methodKey        方法key
     * @return 调用方法方式key
     */
    public static String generateInvokeTypeKey(String methodKey) {
        return StringUtils.joinWith(SEPERATOR, methodKey, INVOKETYPE_SUFFIX);
    }

    /**
     * 生成方法直接依赖的方法key
     * @param classInternalName the internal name of the class
     * @param methodName        the method's name
     * @param methodDescriptor  the method's descriptor
     * @return 方法直接依赖的方法key
     */
    public static String generateDenpendentKey(String classInternalName, String methodName, String methodDescriptor) {
        String methodKey = generateMethodKey(classInternalName, methodName, methodDescriptor);
        return generateDenpendentKey(methodKey);
    }

    /**
     * 生成方法直接依赖的方法key
     * @param methodKey 方法key
     * @return 方法直接依赖的方法key
     */
    public static String generateDenpendentKey(String methodKey) {
        return StringUtils.joinWith(SEPERATOR, methodKey, DENPENDENT_SUFFIX);
    }

    /**
     * 生成接口与实现类关系key
     * @param classInternalName the internal name of the class
     * @return 接口与实现类关系key
     */
    public static String generateImplementKey(String classInternalName) {
        return StringUtils.joinWith(SEPERATOR, classInternalName, IMPLEMENTS_SUFFIX);
    }

}
