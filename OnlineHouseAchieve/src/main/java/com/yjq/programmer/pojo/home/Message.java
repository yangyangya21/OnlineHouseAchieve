package com.yjq.programmer.pojo.home;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-02-21 22:39
 */

/**
 * 浏览器发送给服务器的websocket数据
 */
public class Message {

    private String toName; //接收者姓名

    private String message; //消息内容

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
