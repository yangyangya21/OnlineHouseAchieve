package com.yjq.programmer.service.home;

import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.vo.ResponseVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-11 14:23
 */

/**
 * 聊天Service接口
 */
public interface IChatService {

    //前台获取聊天的用户信息
    ResponseVo<User> getUser(HttpServletRequest request);

    //前台用户退出聊天操作处理
    ResponseVo<Boolean> logout(HttpServletRequest request);

    //前台进入我的客服页面前操作处理
    String chatIndex(HttpServletRequest request);

}
