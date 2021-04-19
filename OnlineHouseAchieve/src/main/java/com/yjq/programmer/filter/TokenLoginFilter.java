package com.yjq.programmer.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.exception.UserLoginException;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.JWTUtil;
import com.yjq.programmer.utils.SpringBeanUtil;
import com.yjq.programmer.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 14:48
 */

/**
 * 认证的filter
 */

public class TokenLoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    private Logger logger = LoggerFactory.getLogger(TokenLoginFilter.class);

    public TokenLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 处理登录操作
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String captcha = req.getParameter("cpacha");

        logger.info("接收到用户昵称={}", username);
        logger.info("接收到用户密码={}", password);
        logger.info("接收到用户输入的验证码={}", captcha);

        Authentication authenticate = null;

        //验证验证码是否合法
        CodeMsg codeMsg = validateCaptcha(req, captcha);
        if(codeMsg.getCode() != 0){
            throw new UserLoginException(codeMsg.getMsg());
        }

        //验证用户昵称和密码是否合法
        try{
            authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
        }catch(Exception e){
            if(CodeMsg.USER_NOT_EXIST.getMsg().equals(e.getMessage())){
                throw new UserLoginException(e.getMessage());
            }else if(CodeMsg.USER_FREEZE.getMsg().equals(e.getMessage())){
                throw new UserLoginException(e.getMessage());
            }else {
                throw new UserLoginException(CodeMsg.USERNAME_OR_PASSWORD_ERROR.getMsg());
            }
        }


        //参数1：用户昵称  参数2：密码  参数3：权限
        return authenticate;

    }


    /**
     * 登录成功后操作处理
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
        logger.info("登录成功！");
        User user = (User) auth.getPrincipal();
        //从数据库中查出改用户详细信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        // 因为Filter和Listener加载顺序优先于spring容器初始化实例 所以使用Bean工具类获取实例
        UserDao userDao = SpringBeanUtil.getBean(UserDao.class);
        User selectedUser = userDao.selectOne(queryWrapper);
        //JWT数据封装
        Map<String,String> map = new HashMap<>();
        map.put("username", selectedUser.getUsername());
        map.put("roleId", String.valueOf(selectedUser.getRoleId()));
        map.put("id", String.valueOf(selectedUser.getId()));
        String token = JWTUtil.getToken(map);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSON.toJSONString(ResponseVo.success(token)));

    }

    /**
     * 登录失败后操作处理
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        logger.info("登录失败，失败原因={}", e.getMessage());
        response.setCharacterEncoding("UTF-8");
        CodeMsg codeMsg = CodeMsg.COMMON_ERROR;
        codeMsg.setMsg(e.getMessage());
        response.getWriter().write(JSON.toJSONString(codeMsg));
    }


    /**
     * 验证登录验证码是否输入正确
     * @param request
     * @return
     */
    private CodeMsg validateCaptcha(HttpServletRequest request, String captcha) {
        String type = request.getParameter("type");
        logger.info("当前验证验证码的类别={}",type);
        String correctCaptcha = (String) request.getSession().getAttribute(type + "_user_login");
        logger.info("获取提交的验证码={}",captcha);
        logger.info("获取系统生成的验证码={}",correctCaptcha);
        if(CommonUtil.isEmpty(captcha)){
            return CodeMsg.CPACHA_EMPTY;
        }
        if(CommonUtil.isEmpty(correctCaptcha)){
            return CodeMsg.CPACHA_EXPIRE;
        }
        if(!captcha.toLowerCase().equals(correctCaptcha.toLowerCase())){
            return CodeMsg.CPACHA_ERROR;
        }
        request.getSession().removeAttribute(type + "_user_login");
        return CodeMsg.SUCCESS;
    }


}
