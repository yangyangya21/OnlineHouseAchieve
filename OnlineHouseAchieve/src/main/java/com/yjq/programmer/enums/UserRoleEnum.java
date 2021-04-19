package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-30 19:20
 */

/**
 * 用户角色枚举类
 */
public enum UserRoleEnum {

    ORDINARY_USERS(1, "普通用户"),

    HOUSE_AGENT(2,"房屋中介"),

    ADMIN(3,"管理员"),

    ;

    Integer code;

    String desc;

    UserRoleEnum(Integer code,String desc) {
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
