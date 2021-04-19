package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-17 9:37
 */
/**
 * 房屋出售种类枚举类
 */
public enum HouseCategoryEnum {

    HOUSE_RENTING(1,"租房"),

    HOUSE_PURCHASE(2,"购房"),

    ;

    Integer code;

    String desc;

    HouseCategoryEnum(Integer code,String desc) {
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
