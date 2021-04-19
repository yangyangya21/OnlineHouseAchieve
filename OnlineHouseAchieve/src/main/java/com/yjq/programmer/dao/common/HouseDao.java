package com.yjq.programmer.dao.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjq.programmer.pojo.common.House;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-30 18:04
 */
/**
 * 房屋House数据访问接口
 */
@Repository
public interface HouseDao extends BaseMapper<House> {

    //后台获取房屋信息列表
    List<House> getHouseList(Map<String, Object> queryMap);

    //后台获取所有房屋总数
    int getAllTotal(Map<String, Object> queryMap);

    //通过用户id删除房屋信息数据
    int deleteByUserIdList(@Param("userIdList") List<String> userIdList);

}
