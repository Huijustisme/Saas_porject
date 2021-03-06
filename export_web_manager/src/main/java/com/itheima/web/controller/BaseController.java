package com.itheima.web.controller;

import com.itheima.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 基础Controller，用于抽取Controller类的公共代码
 */
public class BaseController {
    //统一定义公共变量
    //protected: 在同包或者子类下使用
    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    @Autowired
    protected HttpSession session;

    /**
     * 获取登录企业ID方法
     */
    protected String getLoginCompanyId(){
        return getLoginUser().getCompanyId();
    }

    /**
     * 获取登录企业名称方法
     */
    protected String getLoginCompanyName(){
        return getLoginUser().getCompanyName();
    }

    /**
     * 获取当前登录用户
     */
    protected User getLoginUser(){
        return (User)session.getAttribute("loginUser");
    }
}