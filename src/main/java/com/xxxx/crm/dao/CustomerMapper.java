package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.query.CustomerQuery;
import com.xxxx.crm.vo.Customer;

import java.util.List;

public interface CustomerMapper extends BaseMapper<Customer,Integer> {

    List<Customer> selectByParams(CustomerQuery customerQuery);
    //通过客户名称查询客户对象
    Customer queryCustomerByName(String name);

    List<Customer> queryLossCustomers();

    int updateCustomerStateByIds(List<Integer> lossCusIds);
}