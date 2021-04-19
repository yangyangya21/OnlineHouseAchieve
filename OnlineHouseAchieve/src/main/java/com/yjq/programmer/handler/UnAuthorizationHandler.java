package com.yjq.programmer.handler;

import com.alibaba.fastjson.JSON;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-16 23:54
 */
/**
 * 未授权统一处理handler
 */

public class UnAuthorizationHandler implements AccessDeniedHandler {

    private Logger logger = LoggerFactory.getLogger(UnAuthenticationPointHandler.class);

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        String requestURI = httpServletRequest.getRequestURI();
        logger.info("当前路径{}无权限！", requestURI);
        // 判断是否是ajax请求
        if(CommonUtil.isAjax(httpServletRequest)){
            httpServletResponse.setCharacterEncoding("UTF-8");
            CodeMsg codeMsg = CodeMsg.NO_AUTHORITY;
            httpServletResponse.getWriter().write(JSON.toJSONString(codeMsg));
            return;
        }
        // 判断是否是axios请求
        if(CommonUtil.isAxios(httpServletRequest)){
            httpServletResponse.setCharacterEncoding("UTF-8");
            CodeMsg codeMsg = CodeMsg.NO_AUTHORITY;
            httpServletResponse.getWriter().write(JSON.toJSONString(codeMsg));
            return;
        }
        // 跳转无权限页面
        httpServletResponse.sendRedirect("/common/system/not_permit");
    }
}
