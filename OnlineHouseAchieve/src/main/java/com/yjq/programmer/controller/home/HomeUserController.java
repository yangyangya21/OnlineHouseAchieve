package com.yjq.programmer.controller.home;

import com.yjq.programmer.pojo.common.ApplyAgent;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.common.IUserService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 17:18
 */

/**
 * 前台用户控制器
 */
@Controller
@RequestMapping("/home/user")
public class HomeUserController {

    @Autowired
    private IUserService userService;

    /**
     * 前台用户登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "/home/user/login";
    }

    /**
     * 前台用户信息页面
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "/home/user/index";
    }


    /**
     * 前台用户注册页面
     * @return
     */
    @GetMapping("/register")
    public String register(){
        return "/home/user/register";
    }

    /**
     * 前台申请成为中介页面
     * @return
     */
    @GetMapping("/apply_agent_index")
    public String applyAgentIndex(){
        return "/home/user/apply_agent";
    }


    /**
     * 前台用户注册操作处理
     * @param user
     * @param confirmPassword
     * @param cpacha
     * @param request
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public ResponseVo<Boolean> register(User user, String confirmPassword, String cpacha, HttpServletRequest request){
        return userService.registerUser(user, confirmPassword, cpacha, request);
    }

    /**
     * 进入客服页面前操作处理
     * @param request
     * @return
     */
    @PostMapping("/my_customer_service")
    @ResponseBody
    public ResponseVo<Boolean> myCustomerService(HttpServletRequest request){
        return userService.myCustomerService(request);
    }

    /**
     * 前台获取用户的个人信息
     * @param request
     * @return
     */
    @PostMapping("/get_user_info")
    @ResponseBody
    public ResponseVo<User> getUserInfo(HttpServletRequest request){
        return userService.getUserInfo(request);
    }

    /**
     * 前台用户修改个人信息操作处理
     * @param user
     * @return
     */
    @PostMapping("/update_user_info")
    @ResponseBody
    public ResponseVo<Boolean> updateUserInfo(User user){
        return userService.updateUserInfo(user);
    }

    /**
     * 前台用户申请成为中介操作处理
     * @param applyAgent
     * @param request
     * @return
     */
    @PostMapping("/submit_apply_agent")
    @ResponseBody
    public ResponseVo<Boolean> submitApplyAgent(ApplyAgent applyAgent, HttpServletRequest request){
        return userService.submitApplyAgent(applyAgent, request);
    }

}
