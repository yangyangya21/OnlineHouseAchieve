package com.yjq.programmer.dao.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjq.programmer.pojo.common.OrderTime;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 22:20
 */

/**
 * 预订时间OrderTime数据访问接口
 */
@Repository
public interface OrderTimeDao  extends BaseMapper<OrderTime> {

    //前台获取用户预约房源信息列表
    List<OrderTime> getOrderTimeList(Map<String, Object> queryMap);

    //前台获取用户预约房源信息的总数
    int getAllTotal(Map<String, Object> queryMap);

    //通过房屋id列表删除预约信息数据
    int deleteByHouseIdList(@Param("houseIdList") List<String> houseIdList);

    //通过用户id列表删除预约信息数据
    int deleteByUserIdList(@Param("userIdList") List<String> userIdList);
}
