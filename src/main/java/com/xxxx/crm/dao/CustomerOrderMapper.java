package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.CustomerOrder;

import java.util.Map;

public interface CustomerOrderMapper extends BaseMapper<CustomerOrder,Integer> {

    //通过订单Id查询对应的订单记录
    Map<String, Object> queryOrderById(Integer orderId);

    //通过客户id查询最后一次客户订单
    CustomerOrder queryLastCustomerOrderByCusId(Integer id);
}