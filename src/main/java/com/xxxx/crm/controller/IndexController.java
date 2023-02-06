package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
//import com.xxxx.crm.service.UserService;
//import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.service.PermissionService;
import com.xxxx.crm.service.UserService;
import com.xxxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;
    @Resource
    private PermissionService permissionService;
    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }


    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }

    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        request.setAttribute("user",userService.selectByPrimaryKey(userId));

        //通过登录用户Id查询当前登录用户拥有的资源列表（查询对应资源的授权码）
        List<String> permissions = permissionService.queryUserHasPermissionByUserId(userId);
        request.getSession().setAttribute("permissions",permissions);
        return "main";
    }
}
