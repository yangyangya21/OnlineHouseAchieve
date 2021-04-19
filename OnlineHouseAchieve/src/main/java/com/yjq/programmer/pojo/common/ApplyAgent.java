package com.yjq.programmer.pojo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yjq.programmer.annotion.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-17 11:13
 */

/**
 * 申请中介ApplyAgent实体类
 */
@TableName(value = "apply_agent")
public class ApplyAgent {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id; //申请id

    @TableField(value = "age")
    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=100,minValue=1,errorRequiredMsg="年龄不能为空！",errorMaxValueMsg="年龄不能大于100！",errorMinValueMsg="年龄不能小于1！")
    private Integer age; //年龄

    @TableField(value = "real_name")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="真实姓名不能为空！",errorMaxLengthMsg="真实姓名长度不能大于16！",errorMinLengthMsg="真实姓名长度不能小于1！")
    private String realName; //真实姓名

    @TableField(value = "id_card")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="身份证号不能为空！",errorMaxLengthMsg="身份证号长度不能大于32！",errorMinLengthMsg="身份证号长度不能小于1！")
    private String idCard; //身份证号码

    @TableField(value = "qq")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="QQ号不能为空！",errorMaxLengthMsg="QQ号长度不能大于32！",errorMinLengthMsg="QQ号长度不能小于1！")
    private String QQ; //QQ号码

    @TableField(value = "head_pic")
    private String headPic; //本人头像

    @TableField(value = "user_id")
    private Long userId; //申请所属用户id

    @TableField(exist = false)
    private User user; //申请所属用户

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }
}
