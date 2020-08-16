package com.itheima.service.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 企业管理服务提供者
 */
public class CompanyProvider {
    public static void main(String[] args) throws IOException {
        //加载配置文件
        ClassPathXmlApplicationContext cxt =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        //启动
        cxt.start();
        //阻塞
        System.in.read();
    }
}