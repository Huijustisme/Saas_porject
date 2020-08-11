package com.itheima.web.shiro;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 加盐加密的工具
 */
public class Demo {
    public static void main(String[] args) {
        //1.原密码
        String password = "123";
        //2.盐
        String salt = "lw@export.com";
        //3.加盐加密
        /**
         * 参数一：原密码
         * 参数二：盐
         * 参数三（可选）：加密次数，默认1次
         */
        Md5Hash md5Hash = new Md5Hash(password, salt);

        System.out.println(md5Hash.toString());
    }
}