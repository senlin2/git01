package com.xxxx.crm.service;

import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.ModuleMapper;
import com.xxxx.crm.dao.PermissionMapper;
import com.xxxx.crm.dao.RoleMapper;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Permission;
import com.xxxx.crm.vo.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RoleService extends BaseService<Role,Integer> {
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private ModuleMapper moduleMapper;

    /**
     * 查询所有角色列表
     *
     * @return
     */
    public List<Map<String, Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 添加角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRole(Role role){
//        1.参数校验
//            角色名称  非空 ，名称唯一
        String roleName = role.getRoleName();
        checkPramas(roleName);
//        2,设置参数的默认值
//                是否有效
        role.setIsValid(1);
//                创建时间
        role.setCreateDate(new Date());
//                 修改时间
        role.setUpdateDate(new Date());

//        3,执行添加操作，判断受影响的行数
        AssertUtil.isTrue(roleMapper.insertSelective(role)!=1,"角色添加失败！");
    }

    /**
     * 修改角色
     * @param role
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        //1,参数校验 角色id 非空，且数据存在
        AssertUtil.isTrue(null == role.getId(),"待更新记录不存在！");
        Role temp = roleMapper.selectByPrimaryKey(role.getId());
        AssertUtil.isTrue(null == temp,"待更新记录不存在！");
        //          角色名称  非空，名称唯一
        String roleName = role.getRoleName();
        checkPramas(roleName);
        //2，设置参数的默认值
        role.setUpdateDate(new Date());
//        3，执行更新操作
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)< 1,"修改角色失败！");
    }

    /**
     * 角色删除
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteById(Integer id){
        //判断id是否为空
        AssertUtil.isTrue(id == null,"待删除记录不存在！");
        //判断角色记录是否存在
        Role role = roleMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue( role == null,"待删除记录不存在！");
        //设置删除状态
        role.setIsValid(0);
        role.setUpdateDate(new Date());
        //执行删除操作，判断受影响行数
        AssertUtil.isTrue(roleMapper.updateByPrimaryKeySelective(role)< 1,"角色记录删除失败！");

    }

    /**
     * 参数校验
     *      角色名称  非空 ，名称唯一
     * @param roleName
     */
    private void checkPramas(String roleName) {
        AssertUtil.isTrue(roleName == null,"角色名称不能为空！");
        AssertUtil.isTrue( null!= roleMapper.selectByRoleName(roleName),"角色名称已存在，请重新输入！");
    }

    /**
     * 角色授权
     * @param roleId
     * @param mIds
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addGrant(Integer roleId, Integer[] mIds) {
        /**
         *   授权思路  核心表 t_permission
         *      直接批量添加  不适合 有可能是对角色权限进行更新(权限更新后有可能添加新的菜单 删除原始菜单  甚至情况权限)
         *  推荐: 角色存在原始权限时  先删除原始权限记录  然后批量添加新的角色权限
         */
        //通过角色Id查询对应的权限记录
        int total = permissionMapper.countPermissionByRoleId(roleId);
        //如果权限记录存在，则删除对应的角色拥有的权限记录
        if(total>0){
            //通过角色Id删除权限记录
            AssertUtil.isTrue(permissionMapper.deletePermissionByRoleId(roleId)!=total,"角色授权失败!");
        }

        //如果有权限记录，则添加权限记录
        if(null !=mIds && mIds.length>0){
            //定义permissions集合
            List<Permission> permissions=new ArrayList<Permission>();
            //遍历资源Id数组
            for(Integer mId:mIds){
                Permission permission=new Permission();
                permission.setCreateDate(new Date());
                permission.setModuleId(mId);
                permission.setRoleId(roleId);
                permission.setUpdateDate(new Date());
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mId).getOptValue());
                //将对象设置到集合中
                permissions.add(permission);
            }
            //执行批量添加操作，判断受影响的行数
            AssertUtil.isTrue(permissionMapper.insertBatch(permissions)!=permissions.size(),"角色授权失败!");
        }
    }

}
