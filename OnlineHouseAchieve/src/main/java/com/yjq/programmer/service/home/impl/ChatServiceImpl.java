package com.yjq.programmer.service.home.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.home.IChatService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.JWTUtil;
import com.yjq.programmer.utils.MessageUtils;
import com.yjq.programmer.vo.ResponseVo;
import com.yjq.programmer.ws.ChatEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-04-11 14:23
 */
/**
 * 聊天service接口实现类
 */
@Service
@Transactional
public class ChatServiceImpl implements IChatService {

    @Autowired
    private ChatEndpoint chatEndpoint;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String USER_CHAT_REDIS_KEY_TEMPLATE = "user_chat";

    private final static String AGENT_CHAT_REDIS_KEY_TEMPLATE = "agent_chat";

    private Gson gson = new Gson();

    @Autowired
    private UserDao userDao;

    @Override
    public ResponseVo<User> getUser(HttpServletRequest request) {
        User user = new User();
        Cookie[] cookies = request.getCookies();
        String token = "";

        if(cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies) {
                if (request.getRequestURI().contains("/home/") && ("home_login_token").equals(cookie.getName())){
                    token = cookie.getValue();
                }
                if (request.getRequestURI().contains("/admin/") && ("admin_login_token").equals(cookie.getName())){
                    token = cookie.getValue();
                }
            }
        }
        try{
            DecodedJWT decodedJWT = JWTUtil.verifyToken(token);
            user.setUsername(decodedJWT.getClaim("username").asString());
            user.setId(Long.valueOf(decodedJWT.getClaim("id").asString()));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }

        return ResponseVo.success(user);
    }

    @Override
    public ResponseVo<Boolean> logout(HttpServletRequest request) {
        User userJWT = getUser(request).getData();
        if(userJWT == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        User user = userDao.selectById(userJWT.getId());
        if(user == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        Set<Long> agentIdSet = new HashSet<>();
        Set<Long> userIdSet = new HashSet<>();
        //把退出聊天的用户从在线用户中删除
        ChatEndpoint.onlineUsers.remove(user.getId());
        //把退出聊天的用户从redis中移除
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(user.getRoleId())){
            //当前用户是中介角色
            opsForHash.delete(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
            Map<String, String> userMap = opsForHash.entries(USER_CHAT_REDIS_KEY_TEMPLATE);
            userMap.forEach((k,v)->{
                User agent = gson.fromJson(v, User.class);
                if(user.getId().equals(agent.getId())){
                    userIdSet.add(Long.valueOf(k));
                    opsForHash.delete(USER_CHAT_REDIS_KEY_TEMPLATE, k);
                }
            });
            agentIdSet.add(user.getId());
        }else if(UserRoleEnum.ADMIN.getCode().equals(user.getRoleId()) || UserRoleEnum.ORDINARY_USERS.getCode().equals(user.getRoleId())){
            //当前用户是管理员角色或者是普通用户角色
            //取出中介信息
            String agentValue = opsForHash.get(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
            if(!CommonUtil.isEmpty(agentValue)){
                User agent = gson.fromJson(agentValue, User.class);
                agentIdSet.add(agent.getId());
                String userValue = opsForHash.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(agent.getId()));
                if(!CommonUtil.isEmpty(userValue)){
                    List<User> selectedUserList = gson.fromJson(userValue, new TypeToken<List<User>>() {}.getType());
                    List<User> userList = new ArrayList<>();
                    selectedUserList.forEach(e->{
                        if(!user.getId().equals(e.getId())){
                            userList.add(e);
                        }
                    });
                    opsForHash.put(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(agent.getId()), gson.toJson(userList));
                }
            }
            opsForHash.delete(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
        }
        chatEndpoint.setAgentIdSet(agentIdSet);
        chatEndpoint.setUserIdSet(userIdSet);
        chatEndpoint.setUser(user);
        //再广播一次最新在线用户信息
        //1.获取消息
        String message = MessageUtils.getMessage(true, null, chatEndpoint.getNames());
        //2.调用方法进行系统消息的推送
        chatEndpoint.broadcastAllUsers(message);

        return ResponseVo.success(true);
    }

    @Override
    public String chatIndex(HttpServletRequest request) {
        User userJWT = getUser(request).getData();
        if(userJWT == null){
            return "home/user/login";
        }
        User user = userDao.selectById(userJWT.getId());
        if(user == null){
            return "home/user/login";
        }
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(user.getRoleId())){
            String value = opsForHash.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
            if(CommonUtil.isEmpty(value)){
                //客服已经退出或者自己已无聊天session  跳转到登录页面
                return "common/system/not_chat_session";
            }
        }else{
            String value = opsForHash.get(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
            if(CommonUtil.isEmpty(value)){
                //客服已经退出或者自己已无聊天session  跳转到登录页面
                return "common/system/not_chat_session";
            }
        }
        return "home/chat/index";
    }
}
