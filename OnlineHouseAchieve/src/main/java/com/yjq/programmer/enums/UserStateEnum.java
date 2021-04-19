package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-23 11:52
 */

/**
 * 用户状态枚举类
 */
public enum UserStateEnum {

    NORMAL(1,"正常"),

    FREEZE(2,"冻结"),

    APPLY(3,"申请成为中介"),

    ;

    Integer code;

    String desc;

    UserStateEnum(Integer code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
