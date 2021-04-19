package com.yjq.programmer.pojo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yjq.programmer.annotion.ValidateEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 15:00
 */

/**
 * 用户User实体类
 */
@TableName(value = "user")
public class User implements UserDetails {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id; //用户id

    @TableField(value = "username")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="用户昵称不能为空！",errorMaxLengthMsg="用户昵称长度不能大于8！",errorMinLengthMsg="用户昵称长度不能小于1！")
    private String username; //用户昵称

    @TableField(value = "password")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="用户密码不能为空！",errorMaxLengthMsg="用户密码长度不能大于16！",errorMinLengthMsg="用户密码长度不能小于6！")
    private String password; //用户密码

    @TableField(value = "sex")
    private Integer sex; //用户性别：1：男  2：女  3：未知

    @TableField(value = "email")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="用户电子邮箱不能为空！",errorMaxLengthMsg="用户电子邮箱长度不能大于128！",errorMinLengthMsg="用户电子邮箱长度不能小于1！")
    private String email; //用户邮箱

    @TableField(value = "role_id")
    private Integer roleId; //用户所属角色Id

    @TableField(value = "head_pic")
    private String headPic; //用户头像

    @TableField(value = "phone")
    private String phone; //用户手机号码

    @TableField(value = "state")
    private Integer state; //用户状态 1：正常 2：冻结 3：申请成为中介

    @TableField(exist = false)
    private List<GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(List<GrantedAuthority> authorityList) {
        this.authorities = authorityList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public User(){

    }

    public User(String username, String password, List<GrantedAuthority> authorities, Integer roleId) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.roleId = roleId;
    }
}
