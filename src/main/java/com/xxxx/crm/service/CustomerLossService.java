package com.xxxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxxx.crm.base.BaseService;
import com.xxxx.crm.dao.CustomerLossMapper;
import com.xxxx.crm.dao.CustomerMapper;
import com.xxxx.crm.query.CustomerLossQuery;
import com.xxxx.crm.utils.AssertUtil;
import com.xxxx.crm.vo.Customer;
import com.xxxx.crm.vo.CustomerLoss;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerLossService extends BaseService<CustomerLoss,Integer> {

    @Resource
    private CustomerLossMapper customerLossMapper;

    public void updateCustomerLossStateById(Integer id, String lossReason) {
        CustomerLoss customerLoss =selectByPrimaryKey(id);
        AssertUtil.isTrue(null==customerLoss,"待流失的客户记录不存在!");
        customerLoss.setState(1);// 确认流失
        customerLoss.setLossReason(lossReason);
        customerLoss.setConfirmLossTime(new Date());
        customerLoss.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(customerLoss)<1,"确认流失失败!");
    }


}
