package org.didinem.visitor;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.didinem.handle.CacheHandler;
import org.didinem.util.keygen.RedisKeyGenerater;
import org.objectweb.asm.commons.EmptyVisitor;

import java.util.List;

/**
 * Created by didinem on 3/4/2017.
 */
public class DubboAnalyzeMethodVisitor extends EmptyVisitor {

    private String currentMethodKey;

    private boolean isInterface;

    private CacheHandler cacheHandler;

    public DubboAnalyzeMethodVisitor(String currentMethodKey) {
        this.currentMethodKey = currentMethodKey;
    }

    public DubboAnalyzeMethodVisitor(String currentMethodKey, boolean isInterface) {
        this.currentMethodKey = currentMethodKey;
        this.isInterface = isInterface;
    }

    /**
     * 直接依赖的方法
     */
    private List<String> dependentMethodList = Lists.newArrayList();

    public String getCurrentMethodKey() {
        return currentMethodKey;
    }

    public void setCurrentMethodKey(String currentMethodKey) {
        this.currentMethodKey = currentMethodKey;
    }

    public boolean isLvmama(String methodName) {
        boolean b = false;
        if (methodName.startsWith("com/lvtu") || methodName.startsWith("com/lvmama")) {
            b = true;
        }
        return b;
    }

    /**
     *
     * @param i  opcode - the opcode of the type instruction to be visited. This opcode is either INVOKEVIRTUAL, INVOKESPECIAL, INVOKESTATIC, INVOKEINTERFACE or INVOKEDYNAMIC.
     * @param s  the internal name of the method's owner class (see getInternalName) or Opcodes.INVOKEDYNAMIC_OWNER.
     * @param s1 the method's name.
     * @param s2 the method's descriptor (see Type).
     */
    @Override
    public void visitMethodInsn(int i, String s, String s1, String s2) {
        // FIXME 责任链
        if (isLvmama(s)) {

            String dependentMethodKey = RedisKeyGenerater.generateMethodKey(s, s1, s2);
            // 去除递归调用
            if (!dependentMethodKey.equals(currentMethodKey)) {
                dependentMethodList.add(dependentMethodKey);
            }
            // 调用方式
            String opcodeKey = RedisKeyGenerater.generateInvokeTypeKey(s, s1, s2);
            System.out.println("\t" + opcodeKey + ": " + i);
            cacheHandler.set(opcodeKey, String.valueOf(i));
        }
    }


    @Override
    public void visitEnd() {
        String denpendentKey = RedisKeyGenerater.generateDenpendentKey(currentMethodKey);
        if (CollectionUtils.isNotEmpty(dependentMethodList)) {
            System.out.println("=======================================" + denpendentKey);
            cacheHandler.push(denpendentKey, dependentMethodList);
        }
        super.visitEnd();
    }

}
