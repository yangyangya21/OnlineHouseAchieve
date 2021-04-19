package com.yjq.programmer.service.admin;

import com.yjq.programmer.pojo.admin.Authority;
import com.yjq.programmer.vo.ResponseVo;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-26 15:23
 */

/**
 * 权限Authority接口
 */
public interface IAuthorityService {

    //后台获取权限信息列表
    Map<String, Object> getAuthorityList(Integer page, Integer rows, String name, Integer roleId);

    //后台添加权限信息
    ResponseVo<Boolean> addAuthority(Authority authority);

    //后台修改权限信息
    ResponseVo<Boolean> editAuthority(Authority authority);

    //后台删除权限信息
    ResponseVo<Boolean> deleteAuthority(String ids);

    //判断某角色是否已存在某权限
    Boolean isNameExist(Authority authority, Long id);

}
