package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 17:09
 */
/**
 * 预约看房状态枚举类
 */
public enum OrderTimeStateEnum {

    AGENT_AGREE(1, "中介同意"),

    AGENT_REFUSE(2, "中介拒绝"),

    WAIT_REPLY(3, "待回复"),

    ;

    Integer code;

    String desc;

    OrderTimeStateEnum(Integer code,String desc) {
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
