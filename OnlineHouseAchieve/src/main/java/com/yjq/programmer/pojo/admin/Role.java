package com.yjq.programmer.pojo.admin;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-14 16:57
 */

/**
 * 角色Role实体类
 */
public class Role {

    private Integer id; //角色id

    private String name; //角色名称

    private String description; //角色描述

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
