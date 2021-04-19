package com.yjq.programmer.handler;

import com.alibaba.fastjson.JSON;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 0:10
 */

/**
 * 未认证统一处理handler
 */
public class UnAuthenticationPointHandler implements AuthenticationEntryPoint {

    private Logger logger = LoggerFactory.getLogger(UnAuthenticationPointHandler.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        logger.info("当前路径{}认证不通过！", requestURI);
        // 判断是否是ajax请求
        if(CommonUtil.isAjax(request)){
            response.setCharacterEncoding("UTF-8");
            CodeMsg codeMsg = CodeMsg.USER_SESSION_EXPIRED;
            response.getWriter().write(JSON.toJSONString(codeMsg));
            return;
        }
        // 判断是否是axios请求
        if(CommonUtil.isAxios(request)){
            response.setCharacterEncoding("UTF-8");
            CodeMsg codeMsg = CodeMsg.USER_SESSION_EXPIRED;
            response.getWriter().write(JSON.toJSONString(codeMsg));
            return;
        }
        //跳转未登录页面
        if(requestURI.contains("/home/")){
            response.sendRedirect("/home/user/login");
        }else if(requestURI.contains("/admin/")){
            response.sendRedirect("/admin/user/login");
        }

    }
}
