package com.yjq.programmer.controller.admin;

import com.yjq.programmer.pojo.admin.Authority;
import com.yjq.programmer.service.admin.IAuthorityService;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-26 15:21
 */

/**
 * 后台权限控制器
 */
@Controller
@RequestMapping("/admin/authority")
public class AuthorityController {

    @Autowired
    private IAuthorityService authorityService;

    /**
     * 后台权限列表页面
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/authority/list')")
    @GetMapping("/list")
    public String list(){
        return "admin/authority/list";
    }

    /**
     * 后台获取权限列表信息
     * @param page
     * @param rows
     * @param name
     * @param roleId
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/authority/list')")
    @PostMapping("/list")
    @ResponseBody
    public Map<String, Object> list(Integer page, Integer rows, String name, Integer roleId){
        return authorityService.getAuthorityList(page, rows, name, roleId);
    }

    /**
     * 后台添加权限信息操作处理
     * @param authority
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/authority/add')")
    @PostMapping("/add")
    @ResponseBody
    public ResponseVo<Boolean> add(Authority authority){
        return authorityService.addAuthority(authority);
    }

    /**
     * 后台修改权限信息操作处理
     * @param authority
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/authority/edit')")
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo<Boolean> edit(Authority authority){
        return authorityService.editAuthority(authority);
    }

    /**
     * 后台删除权限信息操作处理
     * @param ids
     * @return
     */
    @PreAuthorize("hasAuthority('/admin/authority/delete')")
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo<Boolean> delete(String ids){
        return authorityService.deleteAuthority(ids);
    }

}
