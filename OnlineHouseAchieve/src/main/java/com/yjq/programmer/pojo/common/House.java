package com.yjq.programmer.pojo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yjq.programmer.annotion.ValidateEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-28 16:18
 */
/**
 * 房屋House实体类
 */
@Document(indexName = "online_house_achieve", type = "house")
@TableName(value = "house")
public class House {

    @TableId(value = "id",type = IdType.AUTO)
    @Id
    private Long id; //房屋id

    @TableField(value = "cover_photo")
    private String coverPhoto; //封面图片

    @TableField(value = "money")
    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="房屋租金不能为空！",errorMaxValueMsg="房屋租金金额不能大于99999999.99！",errorMinValueMsg="房屋租金金额不能小于0.00！")
    private BigDecimal money; //租金(元/月)

    @TableField(value = "house_type")
    @Field(type = FieldType.Text, analyzer ="ik_max_word")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="房屋户型不能为空！",errorMaxLengthMsg="房屋户型长度不能大于16！",errorMinLengthMsg="房屋户型长度不能小于1！")
    private String houseType; //户型

    @TableField(value = "area")
    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=999999.99,minValue=0.00,errorRequiredMsg="房屋面积不能为空！",errorMaxValueMsg="房屋面积大小不能大于999999.99！",errorMinValueMsg="房屋面积大小不能小于0.00！")
    private BigDecimal area; //面积

    @TableField(value = "orientation")
    @Field(type = FieldType.Keyword)
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=4,minLength=1,errorRequiredMsg="房屋朝向不能为空！",errorMaxLengthMsg="房屋朝向长度不能大于4！",errorMinLengthMsg="房屋朝向长度不能小于1！")
    private String orientation; //朝向

    @TableField(value = "floor")
    @Field(type = FieldType.Text, analyzer ="ik_max_word")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="房屋楼层不能为空！",errorMaxLengthMsg="房屋楼层长度不能大于16！",errorMinLengthMsg="房屋楼层长度不能小于1！")
    private String floor; //楼层

    @TableField(value = "renovation")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="房屋装修不能为空！",errorMaxLengthMsg="房屋装修长度不能大于32！",errorMinLengthMsg="房屋装修长度不能小于1！")
    private String renovation; //装修

    @TableField(value = "category")
    private Integer category; //房屋出售种类

    @TableField(value = "location")
    @Field(type = FieldType.Text, analyzer ="ik_max_word")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=64,minLength=1,errorRequiredMsg="房屋位置不能为空！",errorMaxLengthMsg="房屋位置长度不能大于64！",errorMinLengthMsg="房屋位置长度不能小于1！")
    private String location; //房屋位置

    @TableField(value = "info")
    @Field(type = FieldType.Text, analyzer ="ik_max_word")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=64,minLength=1,errorRequiredMsg="房屋简介不能为空！",errorMaxLengthMsg="房屋简介长度不能大于64！",errorMinLengthMsg="房屋简介长度不能小于1！")
    private String info; //房屋简介

    @TableField(value = "details")
    @Field(type = FieldType.Text, analyzer ="ik_max_word")
    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=1024,minLength=1,errorRequiredMsg="房屋详情不能为空！",errorMaxLengthMsg="房屋详情长度不能大于1024！",errorMinLengthMsg="房屋详情长度不能小于1！")
    private String details; //房屋详情

    @TableField(value = "user_id")
    private Long userId; //房屋所属中介id

    @TableField(exist = false)
    @Field(type = FieldType.Object)
    private User user; //房屋所属中介

    @TableField(value = "create_time")
    private Date createTime; //创建时间

    @TableField(value = "update_time")
    @Field(type = FieldType.Date)
    private Date updateTime; //更新时间

    @TableField(value = "state")
    private Integer state; //房屋状态

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getHouseType() {
        return houseType;
    }

    public void setHouseType(String houseType) {
        this.houseType = houseType;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRenovation() {
        return renovation;
    }

    public void setRenovation(String renovation) {
        this.renovation = renovation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
