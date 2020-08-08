package com.itheima.service.system.impl;

import com.github.pagehelper.PageInfo;
import com.itheima.service.system.DeptService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 测试类
 */
@RunWith(SpringJUnit4ClassRunner.class)//Spring整合Junit
@ContextConfiguration("classpath*:spring/applicationContext-*.xml")//读取spring配置文件
public class DeptServiceImplTest {
    @Autowired
    private DeptService deptService;

    @Test
    public void testFindByPage(){
        PageInfo pageInfo = deptService.findByPage(1,3,"1");
        System.out.println("总页数："+pageInfo.getPages());
        System.out.println("总记录数："+pageInfo.getTotal());
        System.out.println("当前页码："+pageInfo.getPageNum());
        System.out.println("上一页："+pageInfo.getPrePage());
        System.out.println("下一页："+pageInfo.getNextPage());
        System.out.println("当前页列表："+pageInfo.getList());
    }

}