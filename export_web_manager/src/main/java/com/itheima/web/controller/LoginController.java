package com.itheima.web.controller;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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

       /* //2.根据Email查询用户是否存在
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
        return "home/main";*/

        //使用Shiro的登录认证逻辑
        //1.获取并封装用户登录数据（账户和密码）
        //UsernamePasswordToken: 用于封装用户登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(email, password);
        //2.调用Shiro的登录方法
        //2.1 获取Subject对象
        Subject subject = SecurityUtils.getSubject();

        try {
            subject.login(token);
            //3.1 如果登录方法没有异常，代表登录成功

            //注意：Shiro底层在登录成功后，会自动在session域存入两个数据，这个两个数据用于标记用户为登录状态（给authc过滤器识别的）

            //虽然我们不需要为Shiro的认证提供session标记，但是我们也需要在业务中用到session数据，例如：页面显示登录用户名，在业务中需要使用登录用户
            //从Subject对象中获取登录用户对象(主体): getPrincipal()
            User loginUser = (User) subject.getPrincipal();
            session.setAttribute("loginUser",loginUser);
            //调用业务层，根据不同用户级别查询各自的权限（菜单）
            List<Module> moduleList = moduleService.findModuleByUser(loginUser);
            //去除重复模块对象（利用HashSet去重，前提是：覆盖Module的hashCode和equals方法）
            removeDuplicate(moduleList);
            //存入session
            session.setAttribute("menus",moduleList);
            //跳到主页
            return "home/main";
        } catch (UnknownAccountException e) {
            //UnknownAccountException:代表账户不存在
            //3.2 如果登录方法有异常代表登录失败
            request.setAttribute("error","Shiro-账户不存在");
            //跳转回电登陆页面
            return "forward:/login.jsp";
        } catch (IncorrectCredentialsException e){
            //IncorrectCredentialsException:代表密码错误
            //3.2 如果登录方法有异常代表登录失败
            request.setAttribute("error","Shiro-密码输入有误！");
            //跳转回电登陆页面
            return "forward:/login.jsp";
        } catch (Exception e){
            //IncorrectCredentialsException:代表密码错误
            //3.2 如果登录方法有异常代表登录失败
            request.setAttribute("error","Shiro-其他错误");
            //跳转回电登陆页面
            return "forward:/login.jsp";
        }
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
        //shiro注销方法
        Subject subject = SecurityUtils.getSubject();
        subject.logout();//底层清空shiro的对应session数据

        return "redirect:/login.jsp";
    }
}