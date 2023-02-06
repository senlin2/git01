package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.vo.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 通过登录用户Id查询当前登录用户拥有的资源列表（查询对应资源的授权码）
     * @param userId
     * @return
     */
    public List<String> queryUserHasPermissionByUserId(Integer userId) {
        return permissionMapper.queryUserHasPermissionByUserId(userId);
    }
}
