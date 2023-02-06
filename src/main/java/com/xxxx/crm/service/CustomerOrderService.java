package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerOrderMapper;
import com.xxxx.crm.query.CustomerOrderQuery;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerOrderService extends BaseService<CustomerOrder,Integer> {

    @Resource
    private CustomerOrderMapper customerOrderMapper;

    /**
     * 多条件分页查询客户订单
     * @param customerOrderQuery
     * @return
     */
    public Map<String, Object> queryCustomerOrderByparams(CustomerOrderQuery customerOrderQuery) {
        Map<String,Object> map = new HashMap<>();
        //开启分页
        PageHelper.startPage(customerOrderQuery.getPage(),customerOrderQuery.getLimit());
        //得到对应的分页对象
        PageInfo<CustomerOrder> pageInfo = new PageInfo<>(customerOrderMapper.selectByParams(customerOrderQuery));
        //设置map对象
        map.put("code",0);
        map.put("msg","success");
        map.put("count",pageInfo.getTotal());
        map.put("data",pageInfo.getList());
        return map;
    }

    /**
     * 通过订单Id查询对应的订单记录
     * @param orderId
     * @return
     */
    public Map<String, Object> queryOrderById(Integer orderId) {
        return customerOrderMapper.queryOrderById(orderId);
    }
}
