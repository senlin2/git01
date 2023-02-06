package com.xxxx.crm.controller;

import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.CustomerReprQuery;
import com.xxxx.crm.service.CustomerReprieveService;
import com.xxxx.crm.vo.CustomerReprieve;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("customer_rep")
public class CustomerReprieveController extends BaseController {
    @Resource
    private CustomerReprieveService customerReprieveService;


    @RequestMapping("list")
    @ResponseBody
    private Map<String,Object> list(CustomerReprQuery customerReprQuery){
        return customerReprieveService.queryByParamsForTable(customerReprQuery);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveCustomerRepr(CustomerReprieve customerReprieve){
        customerReprieveService.saveCustomerRepr(customerReprieve);
        return  success("暂缓记录添加成功!");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateCustomerRepr(CustomerReprieve customerReprieve){
        customerReprieveService.updateCustomerRepr(customerReprieve);
        return  success("暂缓记录更新成功!");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCustomerRepr(Integer id){
        customerReprieveService.deleteCustomerRepr(id);
        return  success("暂缓记录删除成功!");
    }
}
