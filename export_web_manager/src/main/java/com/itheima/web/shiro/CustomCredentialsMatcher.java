package com.itheima.web.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 自定义加盐加密的凭证匹配器
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     * 编写自定义密码匹配逻辑
     * @param token  存放了用户输入的信息（包括用户输入的密码）
     * @param info  存放了数据库的信息（包含数据库的密码）
     * @return 密码是否一致
     *     true： 密码一致
     *     false：密码不一致
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.获取用户输入的密码
        UsernamePasswordToken userToken = (UsernamePasswordToken)token;
        String password = new String(userToken.getPassword());
        //2.加盐加密（算法和数据库加密算法是一致的）
        //2.1 获取用户邮箱
        String email = userToken.getUsername();
        //2.2加盐加密
        Md5Hash md5Hash = new Md5Hash(password, email);
        String encoderPwd = md5Hash.toString();
        //3.获取数据库的密码
        String dbPassword = (String) info.getCredentials();
        //4.把第二步加密的密码 和 数据库的密码匹配
        //5.一致返回true，不一致返回false
        return dbPassword.equals(encoderPwd);
    }
}