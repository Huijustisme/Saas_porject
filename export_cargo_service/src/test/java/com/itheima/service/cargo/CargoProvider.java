package com.itheima.service.cargo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * 货运服务启动类
 */
public class CargoProvider {

    public static void main(String[] args) throws IOException {
        //1.加载spring配置
        ClassPathXmlApplicationContext cxt =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");

        //2.启动
        cxt.start();

        //3.程序阻塞
        System.in.read();
    }

}