package com.xxxx.crm.dao;

import com.xxxx.crm.base.BaseMapper;
import com.xxxx.crm.vo.CustomerReprieve;

import java.util.List;

public interface CustomerReprieveMapper extends BaseMapper<CustomerReprieve,Integer> {

    //通过LossId查询CustomerReprieve
    List<CustomerReprieve> selectCustomerRepByLossId(Integer lossId);
}