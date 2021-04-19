package com.yjq.programmer.controller.home;

import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.home.IChatService;
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
 * @create 2021-04-10 20:13
 */
@Controller
@RequestMapping("/home/chat")
public class HomeChatController {

    @Autowired
    private IChatService chatService;

    /**
     * 跳转到聊天首页页面
     * @param request
     * @return
     */
    @GetMapping("/index")
    public String index(HttpServletRequest request){
        return chatService.chatIndex(request);
    }

    /**
     * 获取聊天的用户信息
     * @param request
     * @return
     */
    @PostMapping("/get_user")
    @ResponseBody
    public ResponseVo<User> getUser(HttpServletRequest request){
       return chatService.getUser(request);
    }

    /**
     * 退出聊天操作处理
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public ResponseVo<Boolean> logout(HttpServletRequest request){
        return chatService.logout(request);
    }
}
