package com.yjq.programmer.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-16 18:54
 */
/**
 * 公告系统控制类
 */
@RequestMapping("/common/system")
@Controller
public class CommonSystemController {

    /**
     * 无权限页面
     * @return
     */
    @GetMapping("/not_permit")
    public String notPermit(){
        return "/common/system/not_permit";
    }
}
