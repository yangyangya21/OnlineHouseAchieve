package com.yjq.programmer.form;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-03 18:40
 */

/**
 * 前台房屋搜索表单
 */
public class HouseSearchForm {

    private Boolean isMoneyOrder; //是否按租金排序

    private Boolean isUpdateTimeOrder; //是否按最后一次更新时间排序

    private String money; //通过租金搜索

    private String orientation; //通过朝向搜索

    private String area; //通过面积搜索

    private String context; //通过输入内容进行搜索

    private String category; //通过出售方式搜索

    public Boolean getIsMoneyOrder() {
        return isMoneyOrder;
    }

    public void setIsMoneyOrder(Boolean isMoneyOrder) {
        this.isMoneyOrder = isMoneyOrder;
    }

    public Boolean getIsUpdateTimeOrder() {
        return isUpdateTimeOrder;
    }

    public void setIsUpdateTimeOrder(Boolean isUpdateTimeOrder) {
        this.isUpdateTimeOrder = isUpdateTimeOrder;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
