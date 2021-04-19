package com.yjq.programmer.ws;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.pojo.home.Message;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.MessageUtils;
import com.yjq.programmer.utils.SpringBeanUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-02-24 23:05
 */

/**
 * 服务端WebSocket对象
 */
@Component
@ServerEndpoint(value = "/home/chat")
public class ChatEndpoint {

    //用来存储每一个客户端对象对应的ChatEndpoint对象  ConcurrentHashMap效率高，线程安全，但是key和value都不能为null
    public static Map<Long, ChatEndpoint> onlineUsers = new ConcurrentHashMap<>();

    //声明Session对象，通过该对象可以发送消息给指定的用户
    private Session session;

    //用户的Id
    private Long userId;

    //用户的角色Id
    private Integer roleId;

    //用户的昵称
    private String username;

    //存储某次消息发送给对应普通用户的id集合
    private Set<Long> userIdSet;

    //存储某次消息发送给对应客服用户的id集合
    private Set<Long> agentIdSet;

    //存储某次消息发送给对应普通用户的昵称集合
    private List<String> namesByUser;

    //存储某次消息发送给对应客服用户的昵称集合
    private List<String> namesByAgent;

    private final static String USER_CHAT_REDIS_KEY_TEMPLATE = "user_chat";

    private final static String AGENT_CHAT_REDIS_KEY_TEMPLATE = "agent_chat";

    private Gson gson = new Gson();

    public void setAgentIdSet(Set<Long> agentIdSet){
        this.agentIdSet = agentIdSet;
    }

    public void setUserIdSet(Set<Long> userIdSet){
        this.userIdSet = userIdSet;
    }

    public void setUser(User user){
        this.roleId = user.getRoleId();
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    /**
     * 连接建立时被调用
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        //将局部的session对象赋值给成员session
        this.session = session;

        //从PathParam获取用户id
        String userId = session.getQueryString();
        String[] split = userId.split("=");
        this.userId = Long.valueOf(split[1]);
        UserDao userDao = SpringBeanUtil.getBean(UserDao.class);
        User user = userDao.selectById(this.userId);
        this.username = user.getUsername();
        this.roleId = user.getRoleId();

        //将当前对象存储到容器中
        onlineUsers.put(this.userId, this);

        //1.获取消息
        String message = MessageUtils.getMessage(true, null, getNames());
        //2.调用方法进行系统消息的推送
        broadcastAllUsers(message);
    }

    /**
     * 系统消息推送
     * @param message
     */
    public void broadcastAllUsers(String message){
        //要将该消息推送给所有的客户端
        try{
            StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
            if(UserRoleEnum.HOUSE_AGENT.getCode().equals(this.roleId)){
                //如果当前用户是中介角色
                //1.先发送中介的信息
                ChatEndpoint agentChatEndpoint = onlineUsers.get(this.userId);
                if(agentChatEndpoint != null){
                    String agentMsg = MessageUtils.getMessage(true, null, namesByUser);
                    agentChatEndpoint.session.getBasicRemote().sendText(agentMsg);
                }
                //2.再发送普通用户或者管理员的信息
                String userMsg = MessageUtils.getMessage(true, null, namesByAgent);
                for(Long id : userIdSet){
                    if(!id.equals(this.userId)){
                        ChatEndpoint userChatEndpoint = onlineUsers.get(id);
                        userChatEndpoint.session.getBasicRemote().sendText(userMsg);
                    }
                }
            }else if(UserRoleEnum.ORDINARY_USERS.getCode().equals(this.roleId) || UserRoleEnum.ADMIN.getCode().equals(this.roleId)){
                //如果当前用户是普通用户或者管理员
                //1.先发送普通用户或者管理员的信息
                ChatEndpoint userChatEndpoint = onlineUsers.get(this.userId);
                if(userChatEndpoint != null){
                    String userMsg = MessageUtils.getMessage(true, null, namesByAgent);
                    userChatEndpoint.session.getBasicRemote().sendText(userMsg);
                }
                //2.再发送中介的信息
                String agentMsg = MessageUtils.getMessage(true, null, namesByUser);
                if(namesByUser.size() == 0){
                    //普通用户或者管理员退出登录
                    HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
                    Map<String, String> agentMap = opsForHash.entries(AGENT_CHAT_REDIS_KEY_TEMPLATE);
                    agentMap.forEach((k,v)->{
                        String value = opsForHash.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, k);
                        List<User> userList = gson.fromJson(value,  new TypeToken<List<User>>() {}.getType());
                        if(userList == null || userList.size() == 0){
                            ChatEndpoint agentChatEndpoint = onlineUsers.get(Long.valueOf(k));
                            try {
                                agentChatEndpoint.session.getBasicRemote().sendText(agentMsg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else{
                    for(Long id : agentIdSet){
                        if(!id.equals(this.userId)){
                            ChatEndpoint agentChatEndpoint = onlineUsers.get(id);
                            agentChatEndpoint.session.getBasicRemote().sendText(agentMsg);
                        }
                    }
                }

            }
        }catch(Exception e){
           // e.printStackTrace();
        }
    }

    /**
     * 获取当前在线用户的所有用户名
     * @return
     */
    public List<String> getNames(){
        namesByUser = new ArrayList<>();
        if(userIdSet == null){
            userIdSet = new HashSet<>();
        }
        namesByAgent = new ArrayList<>();
        if(agentIdSet == null){
            agentIdSet = new HashSet<>();
        }
        //获取StringRedisTemplate的实例
        StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(this.roleId)){
            //如果当前用户是中介角色
            HashOperations<String, String, String> opsForHashByAgentChat = stringRedisTemplate.opsForHash();
            String value = opsForHashByAgentChat.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(this.userId));
            if(!CommonUtil.isEmpty(value)){
                List<User> userList = gson.fromJson(value,  new TypeToken<List<User>>() {}.getType());
                userList.forEach(e->{
                    namesByUser.add(e.getUsername());
                    userIdSet.add(e.getId());
                });
            }
            namesByUser.add(this.username);
            userIdSet.add(this.userId);
            return namesByUser;
        }else if(UserRoleEnum.ORDINARY_USERS.getCode().equals(this.roleId) || UserRoleEnum.ADMIN.getCode().equals(this.roleId)){
            //如果当前用户是普通用户或者管理员
            HashOperations<String, String, String> opsForHashByUserChat = stringRedisTemplate.opsForHash();
            String agentValue = opsForHashByUserChat.get(USER_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(this.userId));
            if(!CommonUtil.isEmpty(agentValue)){
                User agent = gson.fromJson(agentValue, User.class);
                namesByAgent.add(agent.getUsername());
                agentIdSet.add(agent.getId());
            }
            agentIdSet.forEach(e->{
                String userValue = opsForHashByUserChat.get(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(e));
                if(!CommonUtil.isEmpty(userValue)){
                    List<User> userList = gson.fromJson(userValue,  new TypeToken<List<User>>() {}.getType());
                    userList.forEach(x->{
                        namesByUser.add(x.getUsername());
                        userIdSet.add(x.getId());
                    });
                }
            });

            namesByAgent.add(this.username);
            agentIdSet.add(this.userId);
            return namesByAgent;
        }
        return new ArrayList<>();
    }



    /**
     * 接收到客户端发来的消息时被调用
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message){
        try{
            //将message的Json字符串转换成Message对象
            ObjectMapper objectMapper = new ObjectMapper();
            Message msg = objectMapper.readValue(message, Message.class);
            //获取要将数据发送给的用户
            String toName = msg.getToName();
            UserDao userDao = SpringBeanUtil.getBean(UserDao.class);
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", toName);
            User selectedUser = userDao.selectOne(queryWrapper);
            //获取消息数据
            String data = msg.getMessage();
            //获取推送给指定用户的消息格式的数据
            String resultMessage = MessageUtils.getMessage(false, this.username, data);
            //发送数据
            onlineUsers.get(selectedUser.getId()).session.getBasicRemote().sendText(resultMessage);
        }catch(Exception e){
            //e.printStackTrace();
        }


    }

    /**
     * 连接关闭时被调用
     * @param session
     */
    @OnClose
    public void onClose(Session session){
        //从onlineUsers中删除指定的用户
        onlineUsers.remove(this.userId);
        //把退出聊天的用户从redis中移除
        UserDao userDao = SpringBeanUtil.getBean(UserDao.class);
        User user = userDao.selectById(this.userId);
        StringRedisTemplate stringRedisTemplate = SpringBeanUtil.getBean(StringRedisTemplate.class);
        HashOperations<String, String, String> opsForHash = stringRedisTemplate.opsForHash();
        if(UserRoleEnum.HOUSE_AGENT.getCode().equals(this.roleId)){
            //当前用户是中介角色
            opsForHash.delete(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(this.userId));
            Map<String, String> userMap = opsForHash.entries(USER_CHAT_REDIS_KEY_TEMPLATE);
            userMap.forEach((k,v)->{
                User agent = gson.fromJson(v, User.class);
                if(user.getId().equals(agent.getId())){
                    userIdSet.add(Long.valueOf(k));
                    opsForHash.delete(USER_CHAT_REDIS_KEY_TEMPLATE, k);
                }
            });
            agentIdSet.add(user.getId());
        }else if(UserRoleEnum.ADMIN.getCode().equals(this.roleId) || UserRoleEnum.ORDINARY_USERS.getCode().equals(this.roleId)){
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
        //广播一次最新在线用户信息
        String message = MessageUtils.getMessage(true, null, getNames());
        broadcastAllUsers(message);
    }
}
