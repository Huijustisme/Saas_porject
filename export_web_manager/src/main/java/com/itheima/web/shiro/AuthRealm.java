package com.itheima.web.shiro;

import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 自定义Realm: 用于编写认证或授权的核心逻辑（查询数据库相关的逻辑）
 */
public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private ModuleService moduleService;

    //授权方法：写授权逻辑
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录用户拥有的权限（买票）

        //获取当前登录用户分配的模块

        //1.获取登录用户
        Subject subject = SecurityUtils.getSubject();
        User loginUser = (User) subject.getPrincipal();
        //2,获取用户分配的模块
        List<Module> moduleList = moduleService.findModuleByUser(loginUser);
        //3.取出模块的name作为shiro的权限字符串（标记）
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        if (moduleList != null && moduleList.size()>0) {
            for (Module module : moduleList) {
                String name = module.getName();
                if (!StringUtils.isEmpty(name)){
                    //往Shiro添加权限字符串
                    info.addStringPermission(name);
                }
            }
        }
        return info;
    }


    //认证方法：写认证逻辑
    //认证方法：写认证逻辑（当Subject调用login方法的时候，必然会执行doGetAuthenticationInfo方法）
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //System.out.println("执行认证方法。。。");
        //1.判断账户是否存在
        //1.1 取出用户输入的账户
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String email = token.getUsername();
        //1.2 查询数据库判断账户
        User loginUser = userService.findByEmail(email);
        if (loginUser==null){
            //用户不存在
            //只要我们retrun null，Shiro自动抛出一个UnknownAccountException异常
            return null;
        }
        //2.判断密码是否正确
        //我们只需要把数据库的密码交给Shiro即可，而Shiro会拿我们数据库的密码和用户输入的密码匹配
        //1）一旦密码匹配成功，不抛出异常，把两个标记存入session域对象，以标记登录成功
        //2）如果密码匹配不成功，抛出一个IncorrectCredentialsException异常
        /**
         * 参数一： pricipal对象，用于存在用户登录成功后的对象
         * 参数二： 数据库的密码（一定不能返回token的密码）
         * 参数三： realm的别名，只有在多个Realm情况下使用*/

        return new SimpleAuthenticationInfo(loginUser,loginUser.getPassword(),"");
    }
}