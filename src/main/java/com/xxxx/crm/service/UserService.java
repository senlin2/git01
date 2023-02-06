package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.UserMapper;
import com.xxxx.crm.dao.UserRoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.utils.Md5Util;
import com.xxxx.crm.utils.PhoneUtil;
import com.xxxx.crm.utils.UserIDBase64;
import com.xxxx.crm.vo.User;
import com.xxxx.crm.model.UserModel;
import com.xxxx.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserService extends BaseService<User,Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public UserModel userLogin(String userName, String userPwd){
        checkLoginParams(userName,userPwd);
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user==null,"用户姓名不存在！");
        cheeckUserPwd(userPwd,user.getUserPwd());
        return bulidUserInfo(user);

    }

    /**
     * 修改用户密码
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String repeatPwd){
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(null == user,"待更新记录不存在！");
        checkPasswordParam(userId,oldPwd,newPwd,repeatPwd);
        user.setUserPwd(Md5Util.encode(newPwd));
        AssertUtil.isTrue(updateByPrimaryKeySelective(user)<1,"用户密码更新失败!");
    }

    /**
     * 修改密码的参数校验
     * @param userId
     * @param oldPwd
     * @param newPwd
     * @param repeatPwd
     */
    private void checkPasswordParam(Integer userId, String oldPwd, String newPwd, String repeatPwd) {
        AssertUtil.isTrue(null== userId ,"用户不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"请输入原始密码!");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"请输入新密码!");
        AssertUtil.isTrue(StringUtils.isBlank(repeatPwd),"请输入确认密码!");
        AssertUtil.isTrue(!(userMapper.selectByPrimaryKey(userId).getUserPwd().equals(Md5Util.encode(oldPwd))),"原始密码不正确!");
        AssertUtil.isTrue(!(newPwd.equals(repeatPwd)),"新密码输入不一致!");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码与原始密码不能相同!");
    }

    /**
     *result
     * @param user
     */
    private UserModel bulidUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        System.out.println("userIdStr:"+UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 检查密码
     * @param userPwd
     * @param pwd
     */
    private void cheeckUserPwd(String userPwd, String pwd) {
        userPwd = Md5Util.encode(userPwd);
        System.out.println("输入密码："+userPwd+"，"+"正确密码："+pwd);
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户密码不正确！");
    }

    /**
     * 参数判断
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户姓名不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空！");
    }

    /**
     * 查询所有销售人员
     * @return
     */
    public List<Map<String,Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 添加用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        //1，参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),null);
        //2，设置参数的默认值
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //3,设置默认密码
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user) < 1,"用户添加失败！");

         //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());

    }



    /**
     * 更新用户
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        //1,判断用户ID是否为空，且数据存在
        AssertUtil.isTrue(null == user.getId(),"待更新记录不存在");
        //2,通过ID查询数据
        AssertUtil.isTrue(null == userMapper.selectByPrimaryKey(user.getId()),"待更新记录不存在");
        //3，参数校验
        checkUserParams(user.getUserName(),user.getEmail(),user.getPhone(),user.getId());
        //4，设置参数的默认值
        user.setUpdateDate(new Date());
        //5,执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) != 1,"用户更新失败！");
        //用户角色关联
        relationUserRole(user.getId(),user.getRoleIds());
    }

    /**
     * 添加用户参数校验
     * @param userName
     * @param email
     * @param phone
     */
    private void checkUserParams(String userName, String email, String phone,Integer userId) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        User temp = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(userId)),"用户名已存在，请重新输入！");
        AssertUtil.isTrue(StringUtils.isBlank(email),"用户邮箱不能为空！");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"用户手机号不能为空！");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号格式不正确！");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(Integer[] ids){
        AssertUtil.isTrue(null==ids||ids.length==0,"请选择待删除记录!");
        AssertUtil.isTrue(deleteBatch(ids)!=ids.length,"记录删除失败!");
    }

    /**
     * 用户删除
     * @param ids
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIds(Integer[] ids){
        //判断ids是否为空，长度是否大于0
        AssertUtil.isTrue(ids == null || ids.length == 0,"待删除记录不存在！");
        //执行删除操作，判断受影响行数
        AssertUtil.isTrue(userMapper.deleteBatch(ids)!= ids.length,"用户记录删除失败！");
        //遍历用户id的数组
        for (Integer userId : ids) {
            //通过用户id查询对应的用户角色记录
            Integer count = userRoleMapper.countUserRoleByUserId(userId);
            System.out.println(count);
            //判断用户记录是否存在
            if (count > 0){
                //通过用户id删除对应的用户角色记录
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId) != count,"删除用户失败！");
            }
        }

    }
    /**
     * 用户角色关联
     * @param userId
     * @param roleIds
     */
    private void relationUserRole(Integer userId, String roleIds) {
        //通过用户id查询角色记录
        Integer count = userRoleMapper.countUserRoleByUserId(userId);
        //判断角色记录是否存在
        if (count>0){
            //如果角色记录存在，则删除该用户对应的角色记录
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!= count,"用户角色分配失败！");
        }
        //判断角色id是否存在，若果存在，则添加该用户对应的角色记录
        if (!StringUtils.isBlank(roleIds)){
            //将用户角色设置到集合中，执行批量添加
            List<UserRole> userRoleList = new ArrayList<>();
            //将用户id字符串转换成数组
            String[] roleIdArrays = roleIds.split(",");
            for (String roleId : roleIdArrays) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setUserId(userId);
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                //设置到集合中
                userRoleList.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoleList) != userRoleList.size(),
                    "用户角色添加失败！");
        }

    }
}
