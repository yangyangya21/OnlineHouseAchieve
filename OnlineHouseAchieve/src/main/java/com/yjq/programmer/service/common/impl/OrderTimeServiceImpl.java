package com.yjq.programmer.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.bean.Page;
import com.yjq.programmer.dao.common.OrderTimeDao;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.enums.OrderTimeStateEnum;
import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.pojo.common.OrderTime;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.common.IOrderTimeService;
import com.yjq.programmer.service.common.IUserService;
import com.yjq.programmer.service.home.IChatService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-13 22:28
 */
/**
 * 预约时间service接口实现类
 */
@Service
@Transactional
public class OrderTimeServiceImpl implements IOrderTimeService {

    @Autowired
    private OrderTimeDao orderTimeDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private IChatService chatService;

    @Autowired
    private IUserService userService;

    @Override
    public ResponseVo<Boolean> orderTime(OrderTime orderTime, HttpServletRequest request) {
        if(orderTime == null || orderTime.getHouseId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(orderTime);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //获取当前登录用户的信息
        User user = chatService.getUser(request).getData();
        if(user == null || user.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        //判断用户选择的预约时间是否合法
        if(orderTime.getOrderTime().getTime() < (new Date()).getTime()){
            return ResponseVo.errorByMsg(CodeMsg.TIME_NOT_LEGAL);
        }
        //判断该用户是否已预约改房子
        QueryWrapper<OrderTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        queryWrapper.eq("house_id", orderTime.getHouseId());
        OrderTime selectedOrderTime = orderTimeDao.selectOne(queryWrapper);
        if(selectedOrderTime != null){
            //用户修改预约看房时间
            selectedOrderTime.setOrderTime(orderTime.getOrderTime());
            selectedOrderTime.setState(OrderTimeStateEnum.WAIT_REPLY.getCode());
            orderTimeDao.updateById(selectedOrderTime);
            return ResponseVo.successByMsg(true, "成功修改预约看房时间，请等待中介回应！");
        }
        //用户添加预约看房时间
        //封装数据
        orderTime.setUserId(user.getId());
        orderTime.setCreateTime(new Date());
        orderTime.setState(OrderTimeStateEnum.WAIT_REPLY.getCode());
        if(orderTimeDao.insert(orderTime) == 0){
            return ResponseVo.errorByMsg(CodeMsg.ADD_ORDER_TIME_ERROR);
        }

        return ResponseVo.successByMsg(true, "成功预约看房时间，请等待中介回应！");
    }

    @Override
    public ResponseVo<Page<List<OrderTime>>> getUserOrderTime(HttpServletRequest request, Page<List<OrderTime>> page) {
        User user = chatService.getUser(request).getData();
        if(user == null || user.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        User selectedUser = userDao.selectById(user.getId());
        if(selectedUser == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        if(page.getPage() == null || page.getPage() < 1){
            page.setPage(1);
        }
        //每页显示两个
        page.setRows(2);
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", selectedUser.getId());
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        List<OrderTime> orderTimeList = orderTimeDao.getOrderTimeList(queryMap);
        orderTimeList.forEach(e->{
            if(e.getHouse() == null){
                orderTimeList.remove(e);
            }
        });
        //获取当前用户所有的
        Integer total = orderTimeDao.getAllTotal(queryMap);
        page.setTotalCount(total);
        if(orderTimeList == null){
            page.setData(new ArrayList<>());
            return ResponseVo.success(page);
        }
        page.setData(orderTimeList);
        return ResponseVo.success(page);
    }

    @Override
    public ResponseVo<Boolean> cancelOrderTime(OrderTime orderTime) {
        if(orderTime == null || orderTime.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        orderTimeDao.deleteById(orderTime.getId());
        return ResponseVo.successByMsg(true, "成功取消/删除预约！");
    }

    @Override
    public Map<String, Object> list(Page page, Integer state, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>();
        Map<String, Object> queryMap = new HashMap<>();
        User user = userService.getUserInfo(request).getData();
        //当前没有登录信息，返回空Map
        if(user == null){
            return ret;
        }
        //如果搜索状态不为空
        if(state != null && state != -1){
            queryMap.put("state", state);
        }
        //分页设置
        queryMap.put("offset", page.getOffset());
        queryMap.put("pageSize", page.getRows());
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(user.getRoleId())){
            //如果是中介角色  只能查看自己的预约
            queryMap.put("houseUserId", user.getId());
            ret.put("rows", orderTimeDao.getOrderTimeList(queryMap));
            ret.put("total", orderTimeDao.getAllTotal(queryMap));
        }else if(UserRoleEnum.ADMIN.getCode().equals(user.getRoleId())){
            //如果是管理员角色  可以查看所有的预约
            ret.put("rows", orderTimeDao.getOrderTimeList(queryMap));
            ret.put("total", orderTimeDao.getAllTotal(queryMap));
        }
        return ret;
    }

    @Override
    public ResponseVo<Boolean> replyOrderTime(OrderTime orderTime) {
        if(orderTime == null || orderTime.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        OrderTime selectedOrderTime = orderTimeDao.selectById(orderTime.getId());
        // 判断该预约看房信息是否存在
        if(selectedOrderTime == null){
            return ResponseVo.errorByMsg(CodeMsg.ORDER_TIME_NOT_EXIST);
        }
        selectedOrderTime.setState(orderTime.getState());
        selectedOrderTime.setAgentReply(orderTime.getAgentReply());
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(selectedOrderTime);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //修改数据库中的数据
        orderTimeDao.updateById(selectedOrderTime);
        return ResponseVo.successByMsg(true, "回复成功！");
    }

    @Override
    public ResponseVo<Boolean> deleteOrderTime(String ids) {
        if(CommonUtil.isEmpty(ids)){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] split = ids.split(",");
        List<String> idsList = Arrays.asList(split);
        //删除数据库中的的预约信息
        orderTimeDao.deleteBatchIds(idsList);
        return ResponseVo.successByMsg(true, "成功删除预约信息！");
    }
}
