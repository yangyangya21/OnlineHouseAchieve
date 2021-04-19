package com.yjq.programmer.service.common;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 22:27
 */

import com.yjq.programmer.bean.Page;
import com.yjq.programmer.pojo.common.OrderTime;
import com.yjq.programmer.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 预约时间Service接口
 */
public interface IOrderTimeService {

    //前台用户预约看房时间操作处理
    ResponseVo<Boolean> orderTime(OrderTime orderTime, HttpServletRequest request);

    //前台获取用户预约房源列表数据
    ResponseVo<Page<List<OrderTime>>> getUserOrderTime(HttpServletRequest request, Page<List<OrderTime>> page);

    //前台用户取消/删除预约操作处理
    ResponseVo<Boolean> cancelOrderTime(OrderTime orderTime);

    //后台获取预约列表数据操作处理
    Map<String, Object> list(Page page, Integer state, HttpServletRequest request);

    //后台回复预约信息操作处理
    ResponseVo<Boolean> replyOrderTime(OrderTime orderTime);

    //后台删除预约信息操作处理
    ResponseVo<Boolean> deleteOrderTime(String ids);
}
