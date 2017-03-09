package org.didinem.visitor;

import org.apache.commons.lang3.ArrayUtils;
import org.didinem.handle.CacheHandler;
import org.didinem.util.keygen.RedisKeyGenerater;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.EmptyVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 扫描Class信息，收集方法信息用以分析方法依赖关系
 * Created by didinem on 3/4/2017.
 */
@Component("dubboAnalyzeClassVisitor")
public class DubboAnalyzeClassVisitor extends EmptyVisitor {

    @Autowired
    private CacheHandler cacheHandler;

    private static final ThreadLocal<String> classInternalNames = new ThreadLocal<>();

    private List<String> ignoreMethodList;

    @Autowired
    private DubboAnalyzeMethodVisitor dubboAnalyzeMethodVisitor;

    public void setClassInternalName(String classInternalName) {
        this.classInternalNames.set(classInternalName);
    }

    public String getClassInternalName() {
        return classInternalNames.get();
    }

    /**
     * @param i       version - the class version.
     * @param i1      access - the class's access flags (see Opcodes). This parameter also indicates if the class is deprecated.
     * @param s       name - the internal name of the class (see getInternalName).
     * @param s1      signature - the signature of this class. May be null if the class is not a generic one, and does not extend or implement generic classes or interfaces.
     * @param s2      superName - the internal of name of the super class (see getInternalName). For interfaces, the super class is Object. May be null, but only for the Object class.
     * @param strings - the internal names of the class's interfaces (see getInternalName). May be null.
     */
    @Override

    public void visit(int i, int i1, String s, String s1, String s2, String[] strings) {
        setClassInternalName(s);
        // 接口与实现类
        if (ArrayUtils.isNotEmpty(strings)) {
            for (String internalInterfaceName : strings) {
                String interfaceMappingKey = RedisKeyGenerater.generateImplementKey(internalInterfaceName);
                cacheHandler.push(interfaceMappingKey, s);
            }
        }

    }

    /**
     * @param i       access - the method's access flags (see Opcodes). This parameter also indicates if the method is synthetic and/or deprecated.
     * @param s       name - the method's name.
     * @param s1      desc - the method's descriptor (see Type).
     * @param s2      signature - the method's signature. May be null if the method parameters, return type and exceptions do not use generic types.
     * @param strings exceptions - the internal names of the method's exception classes (see getInternalName). May be null.
     * @return
     */
    @Override
    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        // 忽略方法
        if ("<init>".equals(s)) {
            return super.visitMethod(i, s, s1, s2, strings);
        }

        String methodKey = RedisKeyGenerater.generateMethodKey(getClassInternalName(), s, s1);
        cacheHandler.set(methodKey, String.valueOf(i));

        dubboAnalyzeMethodVisitor.setCurrentMethodKey(methodKey);
        return dubboAnalyzeMethodVisitor;
    }

}
