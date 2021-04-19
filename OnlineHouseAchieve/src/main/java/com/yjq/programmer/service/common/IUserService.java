package com.yjq.programmer.service.common;

import com.yjq.programmer.pojo.common.ApplyAgent;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 21:43
 */
/**
 * 用户Service接口
 */
public interface IUserService {

    //前台用户注册操作处理
    ResponseVo<Boolean> registerUser(User user, String confirmPassword, String cpacha, HttpServletRequest request);

    //判断用户实体某个字段的值是否存在  参数1：字段名称  参数2：用户实体id  参数3：进行判断的值
    Boolean isValueExist(String type, Long id, String value);

    //后台获取用户列表信息
    Map<String, Object> getUserList(Integer page, Integer rows, String username, Integer roleId, Integer state, HttpServletRequest request);

    //后台修改用户订单状态操作处理
    ResponseVo<Boolean> editState(Integer id, Integer state);

    //后台修改用户角色信息操作处理
    ResponseVo<Boolean> editRole(Integer id, Integer roleId);

    //后台添加用户操作处理
    ResponseVo<Boolean> addUser(User user);

    //后台修改用户操作处理
    ResponseVo<Boolean> editUser(User user);

    //后台删除用户操作处理
    ResponseVo<Boolean> deleteUser(String ids);

    //根据角色id获取用户
    List<User> selectByRoleId(Integer roleId);

    //前台点击我的客服时的操作处理
    ResponseVo<Boolean> myCustomerService(HttpServletRequest request);

    //前台获取用户的个人信息
    ResponseVo<User> getUserInfo(HttpServletRequest request);

    //前台修改用户个人信息操作处理
    ResponseVo<Boolean> updateUserInfo(User user);

    //前台提交中介申请操作处理
    ResponseVo<Boolean> submitApplyAgent(ApplyAgent applyAgent, HttpServletRequest request);

    //后台查看中介申请信息操作处理
    ResponseVo<ApplyAgent> getApplyAgentInfo(HttpServletRequest request, Long userId);

}
