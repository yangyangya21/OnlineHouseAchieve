package com.yjq.programmer.controller.admin;

import com.yjq.programmer.pojo.common.ApplyAgent;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.common.IUserService;
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
 * @create 2021-03-21 11:26
 */

/**
 * 后台用户控制器
 */
@Controller
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 后台管理系统登录页面
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "admin/user/login";
    }

    /**
     * 后台用户列表页面
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/list')")
    @GetMapping("/list")
    public String list(){
        return "admin/user/list";
    }

    /**
     * 后台用户列表信息获取
     * @param page
     * @param rows
     * @param username
     * @param roleId
     * @param state
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/list')")
    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Integer page, Integer rows, String username, Integer roleId, Integer state, HttpServletRequest request){
        return userService.getUserList(page, rows, username, roleId, state, request);
    }

    /**
     * 后台修改用户状态信息操作处理
     * @param state
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/edit_state')")
    @PostMapping("/edit_state")
    @ResponseBody
    public ResponseVo<Boolean> editState(Integer state, Integer id){
        return userService.editState(id, state);
    }


    /**
     * 后台修改用户角色信息操作处理
     * @param roleId
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/edit_role')")
    @PostMapping("/edit_role")
    @ResponseBody
    public ResponseVo<Boolean> editRole(Integer roleId, Integer id){
        return userService.editRole(id, roleId);
    }


    /**
     * 后台添加用户信息操作处理
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/add')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo<Boolean> add(User user){
        return userService.addUser(user);
    }

    /**
     * 后台修改用户信息操作处理
     * @param user
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/edit')")
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo<Boolean> edit(User user){
        return userService.editUser(user);
    }

    /**
     * 后台删除用户信息操作处理
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo<Boolean> delete(String ids){
        return userService.deleteUser(ids);
    }

    /**
     * 后台查看中介申请信息操作处理
     * @param request
     * @param userId
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/user/get_apply_info')")
    @PostMapping("/get_apply_info")
    @ResponseBody
    public ResponseVo<ApplyAgent> getApplyAgentInfo(HttpServletRequest request, Long userId){
        return userService.getApplyAgentInfo(request, userId);
    }

}
