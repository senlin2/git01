package com.xxxx.crm.controller;

import com.xxxx.crm.service.UserRoleService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class UserRoleController {
    @Resource
    private UserRoleService userRoleService;
}
