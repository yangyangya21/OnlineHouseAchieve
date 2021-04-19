package com.yjq.programmer.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yjq.programmer.constant.RuntimeConstant;
import com.yjq.programmer.dao.admin.AuthorityDao;
import com.yjq.programmer.pojo.admin.Authority;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.JWTUtil;
import com.yjq.programmer.utils.SpringBeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 0:37
 */

/**
 * 鉴权的filter
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        logger.info("进入权限验证的URL={}", requestURI);
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        //判断当前url是否需要登录验证
        for(String uri : RuntimeConstant.userNotNeedConfirmUrl){
            if(uri.equals(requestURI)){
                chain.doFilter(request, response); //放行
                return;
            }
        }

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        logger.info("进入token获取并验证...");
        // 获取Token字符串，token 置于 header 里
        /*String token = request.getHeader("login_token");*/
        String requestURI = request.getRequestURI();
        String type = "";
        if(requestURI.contains("/home/")){
            type = "home";
        }else if(requestURI.contains("/admin/")){
            type = "admin";
        }
        logger.info("当前token验证类别={}", type);
        // 获取Token字符串，token 置于 cookie 里
        Cookie[] cookies = request.getCookies();
        String token = "";
        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if ((type+"_login_token").equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        logger.info("获取到的token={}", token);
        if(CommonUtil.isEmpty(token)){
            return null;
        }

        String username = "";
        String roleId = "";
        String id = "";
        Collection<SimpleGrantedAuthority> authoritiesList = new ArrayList<>();
        try{
            DecodedJWT decodedJWT = JWTUtil.verifyToken(token);
            username = decodedJWT.getClaim("username").asString();
            roleId = decodedJWT.getClaim("roleId").asString();
            id = decodedJWT.getClaim("id").asString();
            request.setAttribute("username", username);
            request.setAttribute("roleId", roleId);
            request.setAttribute("id", id);

            //查询当前用户角色所具有的权限
            QueryWrapper<Authority> authorityQueryWrapper = new QueryWrapper<>();
            authorityQueryWrapper.eq("role_id", roleId);
            // 因为Filter和Listener加载顺序优先于spring容器初始化实例 所以使用Bean工具类获取实例
            AuthorityDao authorityDao = SpringBeanUtil.getBean(AuthorityDao.class);
            List<Authority> selectedAuthoritiesList = authorityDao.selectList(authorityQueryWrapper);
            if(selectedAuthoritiesList != null){
                selectedAuthoritiesList.forEach(e->{
                    authoritiesList.add(new SimpleGrantedAuthority(e.getName()));
                });
            }

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return new UsernamePasswordAuthenticationToken(username, token, authoritiesList);
    }



}
