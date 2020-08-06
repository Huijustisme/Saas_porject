package com.itheima.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    /**
     * 登录方法
     *   1）URL：http://localhost:8080/login.do
     *   2）参数：无
     *   3）返回： /WEB-INF/pages/home/main.jsp
     *
     */
    @RequestMapping("/login")
    public String login(){
        //转发页面
        return "home/main";
    }

    /**
     * 内容区域请求页面
     *   1）URL：http://localhost:8080/home.do
     *   2）参数：无
     *   3）返回： /WEB-INF/pages/home/home.jsp
     */
    @RequestMapping("/home")
    public String home(){
        //转发页面
        return "home/home";
    }


}