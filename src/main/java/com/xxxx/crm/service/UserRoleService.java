package com.xxxx.crm.service;

import com.xxxx.crm.dao.UserRoleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;
}
