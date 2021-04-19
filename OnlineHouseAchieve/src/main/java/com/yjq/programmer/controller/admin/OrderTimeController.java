package com.yjq.programmer.controller.admin;

import com.yjq.programmer.bean.Page;
import com.yjq.programmer.pojo.common.OrderTime;
import com.yjq.programmer.service.common.IOrderTimeService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-18 0:28
 */
/**
 * 后台预约管理控制器
 */
@Controller
@RequestMapping("/admin/order_time")
public class OrderTimeController {

    @Autowired
    private IOrderTimeService orderTimeService;

    /**
     * 后台预约管理列表页面
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/order_time/list')")
    @GetMapping("/list")
    public String list(){
        return "admin/order_time/list";
    }

    /**
     * 后台预约管理列表数据获取
     * @param page
     * @param state
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/order_time/list')")
    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Page page, Integer state, HttpServletRequest request){
        return orderTimeService.list(page, state, request);
    }

    /**
     * 后台回复预约信息操作处理
     * @param orderTime
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/order_time/reply_order_time')")
    @PostMapping("/reply_order_time")
    @ResponseBody
    public ResponseVo<Boolean> replyOrderTime(OrderTime orderTime){
        return orderTimeService.replyOrderTime(orderTime);
    }

    /**
     * 后台删除预约信息操作处理
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/order_time/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo<Boolean> deleteOrderTime(String ids){
        return orderTimeService.deleteOrderTime(ids);
    }
}
