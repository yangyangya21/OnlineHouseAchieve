package com.yjq.programmer.service.admin.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.admin.AuthorityDao;
import com.yjq.programmer.pojo.admin.Authority;
import com.yjq.programmer.service.admin.IAuthorityService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import com.yjq.programmer.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2021-03-26 15:23
 */
/**
 * 权限Authority接口实现类
 */
@Service
@Transactional
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private AuthorityDao authorityDao;

    @Override
    public Map<String, Object> getAuthorityList(Integer page, Integer rows, String name, Integer roleId) {
        Map<String, Object> ret = new HashMap<>();
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        if(roleId != null && roleId != -1){
            queryWrapper.eq("role_id", roleId);
        }
        if(!CommonUtil.isEmpty(name)){
            queryWrapper.like("name", name);
        }
        Page<Authority> pages = new Page<>(page, rows);
        IPage<Authority> authorityPages= authorityDao.selectPage(pages, queryWrapper);
        ret.put("rows", authorityPages.getRecords());
        ret.put("total", authorityPages.getTotal());
        return ret;
    }

    @Override
    public ResponseVo<Boolean> addAuthority(Authority authority) {
        if(authority == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(authority);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断该角色是否已有该权限
        if(isNameExist(authority, 0L)){
            return ResponseVo.errorByMsg(CodeMsg.AUTHORITY_NAME_EXIST);
        }
        //添加权限信息到数据库中
        if(authorityDao.insert(authority) == 0){
            return ResponseVo.errorByMsg(CodeMsg.AUTHORITY_ADD_ERROR);
        }
        return ResponseVo.successByMsg(true, "成功添加权限信息！");
    }

    @Override
    public ResponseVo<Boolean> editAuthority(Authority authority) {
        if(authority == null || authority.getId() == null){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        //进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(authority);
        if(!validate.getCode().equals(CodeMsg.SUCCESS.getCode())){
            return ResponseVo.errorByMsg(validate);
        }
        //判断该角色是否已有该权限
        if(isNameExist(authority, authority.getId())){
            return ResponseVo.errorByMsg(CodeMsg.AUTHORITY_NAME_EXIST);
        }
        //修改数据库中的权限信息
        authorityDao.updateById(authority);
        return ResponseVo.successByMsg(true, "成功修改权限信息！");
    }

    @Override
    public ResponseVo<Boolean> deleteAuthority(String ids) {
        if(CommonUtil.isEmpty(ids)){
            return ResponseVo.errorByMsg(CodeMsg.DATA_ERROR);
        }
        String[] split = ids.split(",");
        List<String> idsList = Arrays.asList(split);
        //删除数据库中的权限信息
        authorityDao.deleteBatchIds(idsList);
        return ResponseVo.successByMsg(true, "成功删除权限信息！");
    }

    @Override
    public Boolean isNameExist(Authority authority, Long id) {
        QueryWrapper<Authority> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", authority.getName());
        queryWrapper.eq("role_id", authority.getRoleId());
        List<Authority> selectedAuthority = authorityDao.selectList(queryWrapper);
        if(selectedAuthority != null && selectedAuthority.size() > 0) {
            if(selectedAuthority.size() > 1){
                return true; //出现重名
            }
            if(!selectedAuthority.get(0).getId().equals(id)) {
                return true; //出现重名
            }
        }
        return false;//没有重名
    }
}
