package com.yjq.programmer.controller.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 15:14
 */

/**
 * 前台系统控制器
 */
@Controller
@RequestMapping("/home/system")
public class HomeSystemController {


    /**
     * 前台系统首页页面
     * @return
     */
    @GetMapping("/index")
    public String index(){
        return "home/system/index";
    }

    /**
     * 前台系统关于我们的页面
     * @return
     */
    @GetMapping("/about")
    public String about(){
        return "home/system/about";
    }

}
