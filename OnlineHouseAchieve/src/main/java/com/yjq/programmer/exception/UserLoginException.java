package com.yjq.programmer.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-18 11:21
 */

/**
 * 自定义用户登录异常
 */
public class UserLoginException extends AuthenticationException {

    public UserLoginException(String msg) {
        super(msg);
    }
}
