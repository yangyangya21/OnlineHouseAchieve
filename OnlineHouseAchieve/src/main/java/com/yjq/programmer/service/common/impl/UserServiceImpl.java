package com.yjq.programmer.service.common.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.admin.AuthorityDao;
import com.yjq.programmer.dao.common.ApplyAgentDao;
import com.yjq.programmer.dao.common.HouseDao;
import com.yjq.programmer.dao.common.OrderTimeDao;
import com.yjq.programmer.dao.common.UserDao;
import com.yjq.programmer.enums.UserRoleEnum;
import com.yjq.programmer.enums.UserStateEnum;
import com.yjq.programmer.exception.UserLoginException;
import com.yjq.programmer.pojo.admin.Authority;
import com.yjq.programmer.pojo.common.ApplyAgent;
import com.yjq.programmer.pojo.common.User;
import com.yjq.programmer.service.common.IUserService;
import com.yjq.programmer.service.home.IChatService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import com.yjq.programmer.vo.ResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-12 21:43
 */

/**
 * 用户service接口实现类
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService , UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private PasswordEncoder pw;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrderTimeDao orderTimeDao;

    @Autowired
    private HouseDao houseDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    private IChatService chatService;

    private Gson gson = new Gson();

    @Autowired
    private ApplyAgentDao applyAgentDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final static String AGENT_CHAT_REDIS_KEY_TEMPLATE = "agent_chat";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过用户名称去数据库中查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User selectedUser = userDao.selectOne(userQueryWrapper);
        if(selectedUser == null){
            throw new UserLoginException(CodeMsg.USER_NOT_EXIST.getMsg());
        }
        //如果用户被冻结
        if(UserStateEnum.FREEZE.getCode().equals(selectedUser.getState())){
            throw new UserLoginException(CodeMsg.USER_FREEZE.getMsg());
        }
        //封装用户信息，用于认证与密码校验
        String password = pw.encode(selectedUser.getPassword());
        logger.info("登录获取数据库中用户密码={}",password);

        //获取当前用户角色所具有的权限
        List<GrantedAuthority> authoritiesList = new ArrayList<>();
        QueryWrapper<Authority> authorityQueryWrapper = new QueryWrapper<>();
        authorityQueryWrapper.eq("role_id", selectedUser.getRoleId());
        List<Authority> selectedAuthoritiesList = authorityDao.selectList(authorityQueryWrapper);
        if(selectedAuthoritiesList != null){
            selectedAuthoritiesList.forEach(e->{
                authoritiesList.add(new SimpleGrantedAuthority(e.getName()));
            });
        }

        return new User(username, password, authoritiesList, selectedUser.getRoleId());
    }

    @Override
    public ResponseVo<Boolean> registerUser(User user, String confirmPassword, String cpacha, HttpServletRequest request){
        if(user == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //判断用户输入的验证码是否为空
        if(CommonUtil.isEmpty(cpacha)){
            return ResponseVo.errorByMsg(CodeMsg.CPACHA_EMPTY);
        }
        //判断系统生成的验证码是否过期
        String correctCpacha = (String) request.getSession().getAttribute("home_user_register");
        if(CommonUtil.isEmpty(correctCpacha)){
            return ResponseVo.errorByMsg(CodeMsg.CPACHA_EXPIRE);
        }
        //判断用户验证码输入是否正确
        if(!correctCpacha.toLowerCase().equals(cpacha.toLowerCase())){
            return ResponseVo.errorByMsg(CodeMsg.CPACHA_ERROR);
        }
        //清除系统生成的验证码
        request.getSession().removeAttribute("home_user_register");
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(user);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断确认密码是否为空
        if(CommonUtil.isEmpty(confirmPassword)){
            return ResponseVo.errorByMsg(CodeMsg.USER_REPASSWORD_EMPTY);
        }
        //判断用户输入的手机号是否符合规范
        CodeMsg validatePhone = CommonUtil.validatePhone(user.getPhone());
        if(!validatePhone.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validatePhone);
        }
        //判断用户两次输入的密码是否一致
        if(!user.getPassword().equals(confirmPassword)){
            return ResponseVo.errorByMsg(CodeMsg.USER_REPASSWORD_ERROR);
        }
        //判断用户昵称是否存在
        if(isValueExist("username", 0L, user.getUsername())){
            return ResponseVo.errorByMsg(CodeMsg.USER_USERNAME_ALREADY_EXIST);
        }
        //判断用户邮箱是否存在
        if(isValueExist("email", 0L, user.getEmail())){
            return ResponseVo.errorByMsg(CodeMsg.USER_EMAIL_ALREADY_EXIST);
        }
        //判断用户手机号码是否存在
        if(isValueExist("phone", 0L, user.getPhone())){
            return ResponseVo.errorByMsg(CodeMsg.USER_PHONE_ALREADY_EXIST);
        }
        //把用户信息添加到数据库中
        if(userDao.insert(user) == 0){
            return ResponseVo.errorByMsg(CodeMsg.REGISTER_ERROR);
        }
        return ResponseVo.successByMsg(true, "恭喜您，注册成功！");
    }

    @Override
    public Boolean isValueExist(String type, Long id, String value) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type, value);
        List<User> selectedUsers = userDao.selectList(queryWrapper);
        if(selectedUsers != null && selectedUsers.size() > 0) {
            if(selectedUsers.size() > 1){
                return true; //出现重名
            }
            if(!selectedUsers.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }

    @Override
    public Map<String, Object> getUserList(Integer page, Integer rows, String username, Integer roleId, Integer state, HttpServletRequest request) {
        Map<String, Object> ret = new HashMap<>();
        User user = getUserInfo(request).getData();
        if(user == null || user.getRoleId() == null || user.getId() == null){
            return ret;
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(roleId != null && roleId != -1){
            queryWrapper.eq("role_id", roleId);
        }
        if(state != null && state != -1){
            queryWrapper.eq("state", state);
        }
        if(!CommonUtil.isEmpty(username)){
            queryWrapper.like("username", username);
        }
        if(!UserRoleEnum.ADMIN.getCode().equals(user.getRoleId())){
            //如果不是管理员角色 只能看到自己的用户信息
            queryWrapper.eq("id", user.getId());
        }
        Page<User> pages = new Page<>(page, rows);
        IPage<User> userPages= userDao.selectPage(pages, queryWrapper);
        ret.put("rows", userPages.getRecords());
        ret.put("total", userPages.getTotal());
        return ret;
    }

    @Override
    public ResponseVo<Boolean> editState(Integer id, Integer state) {
        if(state == null || id == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        User selectedUser = userDao.selectById(id);
        if(selectedUser == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        selectedUser.setState(state);
        userDao.updateById(selectedUser);
        return ResponseVo.successByMsg(true, "成功修改用户状态信息！");
    }

    @Override
    public ResponseVo<Boolean> editRole(Integer id, Integer roleId) {
        if(roleId == null || id == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        User selectedUser = userDao.selectById(id);
        if(selectedUser == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        selectedUser.setRoleId(roleId);
        userDao.updateById(selectedUser);
        return ResponseVo.successByMsg(true, "成功修改用户角色信息！");
    }

    @Override
    public ResponseVo<Boolean> addUser(User user) {
        if(user == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(user);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断用户输入的手机号是否符合规范
        CodeMsg validatePhone = CommonUtil.validatePhone(user.getPhone());
        if(!validatePhone.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validatePhone);
        }
        //判断用户昵称是否存在
        if(isValueExist("username", 0L, user.getUsername())){
            return ResponseVo.errorByMsg(CodeMsg.USER_USERNAME_ALREADY_EXIST);
        }
        //判断用户邮箱是否存在
        if(isValueExist("email", 0L, user.getEmail())){
            return ResponseVo.errorByMsg(CodeMsg.USER_EMAIL_ALREADY_EXIST);
        }
        //判断用户手机号码是否存在
        if(isValueExist("phone", 0L, user.getPhone())){
            return ResponseVo.errorByMsg(CodeMsg.USER_PHONE_ALREADY_EXIST);
        }
        //添加用户信息到数据库中
        if(userDao.insert(user) == 0){
            return ResponseVo.errorByMsg(CodeMsg.USER_ADD_ERROR);
        }
        return ResponseVo.successByMsg(true, "成功添加用户信息！");
    }

    @Override
    public ResponseVo<Boolean> editUser(User user) {
        if(user == null || user.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(user);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断用户输入的手机号是否符合规范
        CodeMsg validatePhone = CommonUtil.validatePhone(user.getPhone());
        if(!validatePhone.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validatePhone);
        }
        //判断用户昵称是否存在
        if(isValueExist("username", user.getId(), user.getUsername())){
            return ResponseVo.errorByMsg(CodeMsg.USER_USERNAME_ALREADY_EXIST);
        }
        //判断用户邮箱是否存在
        if(isValueExist("email", user.getId(), user.getEmail())){
            return ResponseVo.errorByMsg(CodeMsg.USER_EMAIL_ALREADY_EXIST);
        }
        //判断用户手机号码是否存在
        if(isValueExist("phone", user.getId(), user.getPhone())){
            return ResponseVo.errorByMsg(CodeMsg.USER_PHONE_ALREADY_EXIST);
        }
        //修改数据库中的用户信息
        userDao.updateById(user);
        return ResponseVo.successByMsg(true, "成功修改用户信息！");
    }

    @Override
    public ResponseVo<Boolean> deleteUser(String ids) {
        if(CommonUtil.isEmpty(ids)){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] split = ids.split(",");
        List<String> idsList = Arrays.asList(split);
        //删除数据库中的用户信息
        userDao.deleteBatchIds(idsList);
        //删除这些用户对应的申请信息
        applyAgentDao.deleteByUserIdList(idsList);
        //删除这些用户的预约信息
        orderTimeDao.deleteByUserIdList(idsList);
        //删除这些用户发布的房屋信息
        houseDao.deleteByUserIdList(idsList);
        return ResponseVo.successByMsg(true, "成功删除用户信息！");
    }

    @Override
    public List<User> selectByRoleId(Integer roleId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<User> selectedUserList = userDao.selectList(queryWrapper);
        return selectedUserList;
    }

    @Override
    public ResponseVo<Boolean> myCustomerService(HttpServletRequest request) {
        User user = chatService.getUser(request).getData();
        if(user == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        HashOperations<String, String, String> opsForHashByAgentChat = stringRedisTemplate.opsForHash();
        opsForHashByAgentChat.delete(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()));
        List<User> userList = new ArrayList<>();
        opsForHashByAgentChat.put(AGENT_CHAT_REDIS_KEY_TEMPLATE, String.valueOf(user.getId()), gson.toJson(userList));
        return ResponseVo.success(true);
    }

    @Override
    public ResponseVo<User> getUserInfo(HttpServletRequest request) {
        User user = chatService.getUser(request).getData();
        User selectedUser = userDao.selectById(user.getId());
        if(selectedUser == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        return ResponseVo.success(selectedUser);
    }

    @Override
    public ResponseVo<Boolean> updateUserInfo(User user) {
        if(user == null || user.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(user);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断用户输入的手机号是否符合规范
        CodeMsg validatePhone = CommonUtil.validatePhone(user.getPhone());
        if(!validatePhone.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validatePhone);
        }
        //判断用户昵称是否存在
        if(isValueExist("username", user.getId(), user.getUsername())){
            return ResponseVo.errorByMsg(CodeMsg.USER_USERNAME_ALREADY_EXIST);
        }
        //判断用户邮箱是否存在
        if(isValueExist("email", user.getId(), user.getEmail())){
            return ResponseVo.errorByMsg(CodeMsg.USER_EMAIL_ALREADY_EXIST);
        }
        //判断用户手机号码是否存在
        if(isValueExist("phone", user.getId(), user.getPhone())){
            return ResponseVo.errorByMsg(CodeMsg.USER_PHONE_ALREADY_EXIST);
        }
        //修改数据库中的用户信息
        userDao.updateById(user);

        return ResponseVo.successByMsg(true, "成功修改个人信息，请重新登录才会生效！");
    }

    @Override
    public ResponseVo<Boolean> submitApplyAgent(ApplyAgent applyAgent, HttpServletRequest request) {
        if(applyAgent == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        User user = chatService.getUser(request).getData();
        if(user == null || user.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        //判断用户是否已经提交过申请
        QueryWrapper<ApplyAgent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        List<ApplyAgent> applyAgentList = applyAgentDao.selectList(queryWrapper);
        if(applyAgentList.size() >= 1){
            return ResponseVo.errorByMsg(CodeMsg.APPLY_AGENT_ALREADY);
        }
        applyAgent.setUserId(user.getId());
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(applyAgent);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //添加数据库中的申请记录
        if(applyAgentDao.insert(applyAgent) == 0){
            return ResponseVo.errorByMsg(CodeMsg.ADD_APPLY_AGENT_ERROR);
        }
        //修改用户的状态为申请成为中介的状态
        User selectedUser = userDao.selectById(user.getId());
        if(selectedUser != null){
            selectedUser.setState(UserStateEnum.APPLY.getCode());
            userDao.updateById(selectedUser);
        }
        return ResponseVo.successByMsg(true, "成功提交中介申请！");
    }

    @Override
    public ResponseVo<ApplyAgent> getApplyAgentInfo(HttpServletRequest request, Long userId) {
        ApplyAgent applyAgent = new ApplyAgent();
        if(userId == null){
            return ResponseVo.success(applyAgent);
        }
        QueryWrapper<ApplyAgent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        ApplyAgent selectedApplyAgent = applyAgentDao.selectOne(queryWrapper);
        if(selectedApplyAgent == null){
            return ResponseVo.success(applyAgent);
        }
        return ResponseVo.success(selectedApplyAgent);
    }

}
