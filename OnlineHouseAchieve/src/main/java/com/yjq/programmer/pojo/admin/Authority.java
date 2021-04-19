package com.yjq.programmer.pojo.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yjq.programmer.annotion.ValidateEntity;
/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-26 14:56
 */

/**
 * 权限Authority实体类
 */
@TableName(value = "authority")
public class Authority {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id; //权限id

    @TableField(value = "name")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=64,minLength=1,errorRequiredMsg="权限名称不能为空！",errorMaxLengthMsg="权限名称长度不能大于64！",errorMinLengthMsg="权限名称长度不能小于1！")
    private String name; //权限名称

    @TableField(value = "description")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="权限描述不能为空！",errorMaxLengthMsg="权限描述长度不能大于128！",errorMinLengthMsg="权限描述长度不能小于1！")
    private String description; //权限描述

    @TableField(value = "role_id")
    private Integer roleId; //权限所属角色id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
