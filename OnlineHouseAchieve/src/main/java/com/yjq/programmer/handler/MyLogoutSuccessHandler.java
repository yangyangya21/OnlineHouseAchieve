package com.yjq.programmer.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-21 11:05
 */
/**
 * 自定义成功退出登录后逻辑处理
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(MyLogoutSuccessHandler.class);

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String type = request.getParameter("type");
        logger.info("成功退出登录，当前退出登录type={}", type);
        //清除储存在cookie中的token
        Cookie cookie = new Cookie(type +"_login_token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        //跳转页面
        if("home".equals(type)){
            //如果是前台用户退出登录
            response.sendRedirect("/" + type + "/system/index");
        }else if("admin".equals(type)){
            //如果是后台用户退出登录
            response.sendRedirect("/admin/user/login");
        }

    }
}
