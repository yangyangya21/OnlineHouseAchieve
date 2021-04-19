package com.yjq.programmer.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-22 13:20
 */

/**
 * 后台系统控制器
 */
@Controller
@RequestMapping("/admin/system")
public class SystemController {

    /**
     * 后台系统首页
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "admin/system/index";
    }


    /**
     * 后台系统欢迎页面
     * @return
     */
    @GetMapping("/welcome")
    public String welcome(){
        return "admin/system/welcome";
    }
}
