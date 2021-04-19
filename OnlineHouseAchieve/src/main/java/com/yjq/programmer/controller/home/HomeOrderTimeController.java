package com.yjq.programmer.controller.home;

import com.yjq.programmer.bean.Page;
import com.yjq.programmer.pojo.common.OrderTime;
import com.yjq.programmer.service.common.IOrderTimeService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 22:24
 */

/**
 * 前台预约时间控制器
 */
@Controller
@RequestMapping("/home/order_time")
public class HomeOrderTimeController {

    @Autowired
    private IOrderTimeService orderTimeService;

    /**
     * 用户预约房源列表页面
     * @return
     */
    @GetMapping("/user_order_list")
    public String userOrderList(){
        return "home/order_time/list";
    }

    /**
     * 预约看房时间操作处理
     * @param orderTime
     * @param request
     * @return
     */
    @PostMapping("/order")
    @ResponseBody
    public ResponseVo<Boolean> orderTime(@RequestBody OrderTime orderTime, HttpServletRequest request){
        return orderTimeService.orderTime(orderTime, request);
    }

    /**
     * 前台获取用户预约信息操作处理
     * @param request
     * @param page
     * @return
     */
    @PostMapping("/get_user_order")
    @ResponseBody
    public ResponseVo<Page<List<OrderTime>>> getUserOrderTime(HttpServletRequest request, @RequestBody Page<List<OrderTime>> page){
        return orderTimeService.getUserOrderTime(request, page);
    }

    /**
     * 前台用户取消/删除预约操作处理
     * @param orderTime
     * @return
     */
    @PostMapping("/cancel_order_time")
    @ResponseBody
    public ResponseVo<Boolean> cancelOrderTime(@RequestBody OrderTime orderTime){
        return orderTimeService.cancelOrderTime(orderTime);
    }

}
