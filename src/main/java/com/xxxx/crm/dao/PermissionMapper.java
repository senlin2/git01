package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {


    /**
     * 通过角色Id查询对应的权限记录
     * @param roleId
     * @return
     */
    int countPermissionByRoleId(Integer roleId);

    /**
     * 通过角色Id删除权限记录
     * @param roleId
     * @return
     */
    int deletePermissionByRoleId(Integer roleId);

    /**
     * 查询角色拥有的资源Id集合
     * @param roleId
     * @return
     */
    List<Integer> queryRoleHasModuleIdsByRoleId(Integer roleId);

    /**
     * 通过登录用户Id查询当前登录用户拥有的资源列表（查询对应资源的授权码）
     * @param userId
     * @return
     */
    List<String> queryUserHasPermissionByUserId(Integer userId);

    //通过资源Id查询权限记录
    Integer countPermissionByModuleId(Integer id);

    //通过资源Id删除权限记录
    void deletePermissionByModuleId(Integer id);
}