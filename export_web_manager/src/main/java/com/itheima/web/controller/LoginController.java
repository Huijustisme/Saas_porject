package com.itheima.web.controller;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;

@Controller
public class LoginController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    /**
     * 登录方法
     *   1）URL：http://localhost:8080/login.do
     *   2）参数：无
     *   3）返回： /WEB-INF/pages/home/main.jsp
     *
     */
    @RequestMapping("/login")
    public String login(String email,String password){
        //1.非空判断
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)){
            request.setAttribute("error","账户和密码不能为空！");
            //跳转回电登录页面
            return "forward:/login.jsp";
        }

        //2.根据Email查询用户是否存在
        User loginUser = userService.findByEmail(email);
        //3.1 不存在，则提示"账户不存在"
        if (loginUser == null) {
            request.setAttribute("error","账户不存在！");
            //跳转回电登录页面
            return "forward:/login.jsp";
        }
        //3.2 存在，则继续判断密码是正确
        if (!loginUser.getPassword().equals(password)){
            //4.1 密码不正确，则提示"密码错误"
            request.setAttribute("error","密码错误！");
            //跳转回电登录页面
            return "forward:/login.jsp";
        }
        //4.2 密码正确，代表登录成功，把用户数据存在session域，然后再跳转到主页
        session.setAttribute("loginUser",loginUser);
        //调用业务层，根据不同用户级别查询各自的权限（菜单）
        List<Module> moduleList = moduleService.findModuleByUser(loginUser);
        //去除重复模块对象（利用HashSet去重，前提是：覆盖Module的hashCode和equals方法）
        removeDuplicate(moduleList);
        //存入session
        session.setAttribute("menus",moduleList);
        //跳转到主页`
        return "home/main";
    }

    /**
     * 去重方法
     * @param arlList
     */
    public static void removeDuplicate(List arlList)
    {
        //把List集合元素放入HashSet的过程，进入去重操作
        HashSet h = new HashSet(arlList);
        //清空List集合
        arlList.clear();
        //把Set元素存入List集合
        arlList.addAll(h);
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


    /**
     * 登录注销
     *  1）URL:http://localhost:8080/logout.do
     *  2)参数：无
     *  3）返回：返回到登录页面
     */
    @RequestMapping("/logout")
    public String logout(){
        //删除session数据
        session.removeAttribute("loginUser");
        return "redirect:/login.jsp";
    }
}