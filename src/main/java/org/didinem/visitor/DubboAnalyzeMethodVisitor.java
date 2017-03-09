package org.didinem.visitor;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.didinem.handle.CacheHandler;
import org.didinem.util.keygen.RedisKeyGenerater;
import org.objectweb.asm.commons.EmptyVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by didinem on 3/4/2017.
 */
@Component("dubboAnalyzeMethodVisitor")
public class DubboAnalyzeMethodVisitor extends EmptyVisitor {

    private static final ThreadLocal<String> currentMethodKeys = new ThreadLocal<>();

    private static final ThreadLocal<List<String>> dependentMethodLists = new ThreadLocal<>();

    @Autowired
    private CacheHandler cacheHandler;

    public String getCurrentMethodKey() {
        return currentMethodKeys.get();
    }

    public void setCurrentMethodKey(String currentMethodKey) {
        this.currentMethodKeys.set(currentMethodKey);
    }

    public void setDependentMethodList(List<String> currentDependentMethodList) {
        dependentMethodLists.set(currentDependentMethodList);
    }

    public List<String> getDependentMethodList() {
        if (dependentMethodLists.get() == null) {
            List<String> depdentMethodList = Lists.newArrayList();
            setDependentMethodList(depdentMethodList);
            return depdentMethodList;
        }
        return dependentMethodLists.get();
    }

    public boolean isLvmama(String methodName) {
        boolean b = false;
        if (methodName.startsWith("com/lvtu") || methodName.startsWith("com/lvmama")) {
            b = true;
        }
        return b;
    }

    /**
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
            if (!dependentMethodKey.equals(getCurrentMethodKey())) {
                getDependentMethodList().add(dependentMethodKey);
            }
            // 调用方式
            String opcodeKey = RedisKeyGenerater.generateInvokeTypeKey(s, s1, s2);
            cacheHandler.set(opcodeKey, String.valueOf(i));
        }
    }


    @Override
    public void visitEnd() {
        String denpendentKey = RedisKeyGenerater.generateDenpendentKey(getCurrentMethodKey());
        if (CollectionUtils.isNotEmpty(getDependentMethodList())) {
            String[] str = new String[getDependentMethodList().size()];
            cacheHandler.push(denpendentKey, getDependentMethodList().toArray(str));
            // 清除前一个方法的依赖
            dependentMethodLists.remove();
            System.out.println("====================================================================");
        }
        super.visitEnd();
    }

}
