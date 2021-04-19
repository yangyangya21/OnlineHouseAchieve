package com.yjq.programmer.pojo.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yjq.programmer.annotion.ValidateEntity;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 17:02
 */

/**
 * 预约时间OrderTime实体类
 */
@TableName(value = "order_time")
public class OrderTime {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id; //预约看房id

    @TableField(value = "order_time")
    @ValidateEntity(required=true,errorRequiredMsg="预约看房时间不能为空！")
    private Date orderTime; //预约时间

    @TableField(value = "user_id")
    private Long userId; //看房用户id

    @TableField(exist = false)
    private User user; //看房用户

    @TableField(value = "state")
    private Integer state; //预约状态

    @TableField(value = "agent_reply")
    @ValidateEntity(requiredMaxLength=true,maxLength=128,errorMaxLengthMsg="中介回复的长度不能大于128！")
    private String agentReply; //中介回复

    @TableField(value = "create_time")
    private Date createTime; //创建时间

    @TableField(value = "house_id")
    private Long houseId; //看房对应的房屋id

    @TableField(exist = false)
    private House house; //看房对应分房屋

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAgentReply() {
        return agentReply;
    }

    public void setAgentReply(String agentReply) {
        this.agentReply = agentReply;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getHouseId() {
        return houseId;
    }

    public void setHouseId(Long houseId) {
        this.houseId = houseId;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }
}
