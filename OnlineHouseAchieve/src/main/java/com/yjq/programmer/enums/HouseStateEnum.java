package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-28 18:51
 */

/**
 * 房屋状态枚举类
 */
public enum HouseStateEnum {

    WAIT_AUDIT(1,"待审核"),

    WAIT_LEASE(2,"待出租"),

    ALREADY_LEASE(3,"已出租"),

    OFF_SHELVES(4,"已下架"),

    ;

    Integer code;

    String desc;

    HouseStateEnum(Integer code,String desc) {
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
