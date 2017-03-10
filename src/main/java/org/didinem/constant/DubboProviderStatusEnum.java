package org.didinem.constant;

/**
 * Created by zhongzhengmin on 2017/3/10.
 */
public enum DubboProviderStatusEnum {

    PROVIDED("1"), NOT_PROVIDED("0");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    DubboProviderStatusEnum(String code) {
        this.code = code;
    }
}
