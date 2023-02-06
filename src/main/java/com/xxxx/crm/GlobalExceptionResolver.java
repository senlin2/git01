package com.xxxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxxx.crm.base.ResultInfo;
import com.xxxx.crm.exceptions.AuthException;
import com.xxxx.crm.exceptions.NoLoginException;
import com.xxxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    /**
     * 全局异常
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        if (ex instanceof NoLoginException){//登录异常
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        }
        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","异常异常，请重试...");
        //判断HandlerMethod
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            ResponseBody requestBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            if (requestBody == null){
                //返回视图
                if (ex instanceof ParamsException){//参数异常
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                } else if (ex instanceof AuthException) {//认证异常
                    ParamsException p = (ParamsException) ex;
                    modelAndView.addObject("code",p.getCode());
                    modelAndView.addObject("msg",p.getMsg());
                }
                return modelAndView;
            }else {
                //返回json数据
                ResultInfo resultInfo=new ResultInfo();
                resultInfo.setCode(300);
                resultInfo.setMsg("系统错误,请稍后再试...");
                if (ex instanceof ParamsException){//参数异常
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                } else if (ex instanceof AuthException) {//认证异常
                    ParamsException p = (ParamsException) ex;
                    resultInfo.setCode(p.getCode());
                    resultInfo.setMsg(p.getMsg());
                }
                response.setContentType("application/json;charset=utf-8");
                response.setCharacterEncoding("utf-8");
                PrintWriter out =null;
                try {
                    out=response.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null){
                        out.close();
                    }
                }
                return null;

            }
        }
        return modelAndView;
    }
}
