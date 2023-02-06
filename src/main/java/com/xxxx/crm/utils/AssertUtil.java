package com.xxxx.crm.utils;

import com.xxxx.crm.exceptions.ParamsException;

/**
 * 异常校验类
 */
public class AssertUtil {


    /**
     * @param flag  判断是否存在异常
     * @param msg   异常信息（有异常时才添加异常信息并抛出异常）
     */
    public  static void isTrue(Boolean flag,String msg){
        if(flag){
            throw  new ParamsException(msg);
        }
    }

}
