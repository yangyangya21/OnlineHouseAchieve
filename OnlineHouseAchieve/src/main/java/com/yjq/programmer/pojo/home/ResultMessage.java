package com.yjq.programmer.pojo.home;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-02-21 22:43
 */
/**
 * 服务器发送给浏览器的websocket数据
 */
public class ResultMessage {

    private boolean isSystem; //是否是系统消息

    private String fromName; //发送者名称

    private Object message; //消息内容 如果是系统消息，这里是数组

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
