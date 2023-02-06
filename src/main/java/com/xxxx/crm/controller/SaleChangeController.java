package com.xxxx.crm.controller;

import com.xxxx.crm.annoation.RequiredPermission;
import com.xxxx.crm.base.BaseController;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.query.SaleChanceQuery;
import com.xxxx.crm.service.SaleChanceService;
import com.xxxx.crm.utils.CookieUtil;
import com.xxxx.crm.utils.LoginUserUtil;
import com.xxxx.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChangeController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;

    @RequiredPermission(code = "101001")
    @GetMapping("list")
    @ResponseBody
    public Map<String,Object> querySaleChangeByParams(SaleChanceQuery saleChanceQuery,HttpServletRequest request,Integer flag){
        if(null !=flag && flag==1){
            // 分配给指定用户的机会数据
            saleChanceQuery.setAssignMan(LoginUserUtil.releaseUserIdFromCookie(request));
        }
        return saleChanceService.querySaleChangeByParams(saleChanceQuery);
    }

    /**
     * 进入营销机会管理页面
     * @return
     */
    @RequiredPermission(code = "1010")
    @RequestMapping("index")
    public String index(){
        return "saleChance/sale_chance";
    }

    /**
     * 添加营销机会
     * @param saleChance
     * @param request
     * @return
     */
    @RequiredPermission(code = "101002")
    @PostMapping("add")
    @ResponseBody
    public ResultInfo addSaleChance(SaleChance saleChance, HttpServletRequest request){
        String userName = CookieUtil.getCookieValue(request, "userName");
        saleChance.setCreateMan(userName);
        saleChanceService.addSaleChance(saleChance);

        return success("营销机会数据添加成功！");

    }

    /**
     * 跳转到 add_update.ftl页面
     * @param saleChanceId
     * @param request
     * @return
     */
    @RequestMapping("toSaleChancePage")
    public String toSaleChancePage(Integer saleChanceId,HttpServletRequest request){
        if (saleChanceId != null){
            SaleChance saleChance =saleChanceService.selectByPrimaryKey(saleChanceId);
            request.setAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 更新营销机会 101004
     * @param saleChance
     * @return
     */
    @RequiredPermission(code = "101004")
    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success("营销机会数据更新成功");

    }

    /**
     * 删除营销机会 101003
     * @param ids
     * @return
     */
    @RequiredPermission(code = "101003")
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        saleChanceService.deleteSaleChance(ids);
        return success("机会数据删除成功!");
    }

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success("开发状态更新成功");
    }
}
