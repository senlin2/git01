package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {
    User queryUserByName(String userName);

    /**
     * 查询所有营销人员
     * @return
     */
    List<Map<String,Object>> queryAllSales();
}