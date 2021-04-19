package com.yjq.programmer.config;

import com.yjq.programmer.filter.TokenAuthenticationFilter;
import com.yjq.programmer.filter.TokenLoginFilter;
import com.yjq.programmer.handler.MyLogoutSuccessHandler;
import com.yjq.programmer.handler.UnAuthenticationPointHandler;
import com.yjq.programmer.handler.UnAuthorizationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-11 23:29
 */

/**
 * Spring Security 配置类
 */
@Configuration
@EnableWebSecurity //开启SpringSecurity @Secured注解 一个标记注解
@EnableGlobalMethodSecurity(prePostEnabled = true)  //表示访问方法或类在执行之前先判断权限，大多情况下都是使用这个注解
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 基本配置设置
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // exceptionHandling() 方法用来提供异常处理
        http.exceptionHandling()
                // 未认证处理
                .authenticationEntryPoint(new UnAuthenticationPointHandler())
                // 未授权处理
                .accessDeniedHandler(new UnAuthorizationHandler())
                // authorizeRequests() 对指定URL进行自定义要求处理 会走过滤链进行处理
                .and().authorizeRequests()
                .antMatchers("/home/system/index").permitAll()
                .antMatchers("/home/user/login").permitAll()
                .antMatchers("/home/user/register").permitAll()
                .antMatchers("/home/house/renting_list").permitAll()
                .antMatchers("/home/house/get_data").permitAll()
                .antMatchers("/home/house/get_new_house").permitAll()
                .antMatchers("/home/house/info").permitAll()
                .antMatchers("/home/house/get_info_data").permitAll()
                .antMatchers("/home/system/about").permitAll()
                .antMatchers("/home/house/get_renting_house").permitAll()
                .antMatchers("/home/house/get_purchase_house").permitAll()
                // 任何请求都要登录验证
                .anyRequest().authenticated()
                .and().logout()
                // 自定义退出登录成功后逻辑处理
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                // 禁用iframe不能访问的设置
                .and().headers().frameOptions().disable()
                // 关闭csrf保护 因为使用JWT，不再依赖cookie
                .and().csrf().disable()
                // 添加认证的过滤器
                .addFilter(new TokenLoginFilter(authenticationManager()))
                // 添加鉴权的过滤器
                .addFilter(new TokenAuthenticationFilter(authenticationManager()));


    }

    /**
     * 密码加密配置
     * @return
     */
    @Bean
    public PasswordEncoder getPassword(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置哪些请求不拦截  这种方法的配置是不走过滤链的
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/home/house/css/**",
                "/home/house/images/**","/home/house/js/**",
                "/favicon.ico","/common/vue/**","/common/layui/**",
                "/common/h-ui/**","/common/cookie/**","/common/system/not_permit",
                "/admin/login/**","/common/cpacha/generate_cpacha",
                "/admin/user/login","/admin/easyui/**","/admin/images/**",
                "/common/photo/view","/common/upload/upload_photo",
                "/tags/**", "/es_extra/hot_word.txt","/home/chat/css/**",
                "/home/chat/img/**","/home/chat/index_files/**",
                "/home/chat/iocn/**","/home/chat/js/**");
    }


}
