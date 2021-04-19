package com.yjq.programmer.dao.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjq.programmer.pojo.common.ApplyAgent;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-17 12:40
 */
/**
 * 申请中介ApplyAgent数据访问接口
 */
@Repository
public interface ApplyAgentDao extends BaseMapper<ApplyAgent> {

    //通过用户id删除申请信息
    int deleteByUserIdList(@Param("userIdList") List<String> userIdList);
}
