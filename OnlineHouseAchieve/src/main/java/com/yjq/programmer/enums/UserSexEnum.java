package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-14 13:07
 */

/**
 * 用户性别枚举类
 */
public enum UserSexEnum {

    MALE(1,"男"),

    FEMALE(2,"女"),

    UNKNOWN(3,"未知"),

            ;

    Integer code;

    String desc;

    UserSexEnum(Integer code,String desc) {
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
