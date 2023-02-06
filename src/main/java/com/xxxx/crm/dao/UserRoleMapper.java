package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {

    /**
     * 通过用户id查询对应的用户角色记录
     * @param userId
     * @return
     */
    Integer countUserRoleByUserId(Integer userId);

    /**
     * 通过用户id删除对应的用户角色记录
     * @param userId
     * @return
     */
    Integer deleteUserRoleByUserId(Integer userId);
}