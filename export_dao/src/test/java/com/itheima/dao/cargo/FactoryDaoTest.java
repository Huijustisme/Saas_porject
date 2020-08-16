package com.itheima.dao.cargo;

import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 测试MyBatis逆向工程产生的Dao方法
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")

public class FactoryDaoTest {
    @Autowired
    private FactoryDao factoryDao;

    /**
     * 普通插入（全表）
     */
    @Test
    public void testInsert(){
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("测试1号工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.insert(factory);
    }

    /**
     * 选择性插入（非空值才插入） -- 推荐使用
     */
    @Test
    public void testInsertSelective(){
        Factory factory = new Factory();
        factory.setId(UUID.randomUUID().toString());
        factory.setFactoryName("测试2号工厂");
        factory.setCreateTime(new Date());
        factory.setUpdateTime(new Date());
        factoryDao.insertSelective(factory);
    }

    /**
     * 全表更新（不管空NULL值与否，都会更新）
     */
    @Test
    public void testUpdateByPrimaryKey(){
        Factory factory = new Factory();
        factory.setId("d55ef6f7-97bb-4e5a-a1d6-93accb7034f4");
        factory.setFactoryName("测试1号工厂666");
        factoryDao.updateByPrimaryKey(factory);
    }

    /**
     * 选择性更新（只有非NULL值才会更新） -- 推荐使用
     */
    @Test
    public void testUpdateByPrimaryKeySelective(){
        Factory factory = new Factory();
        factory.setId("d55ef6f7-97bb-4e5a-a1d6-93accb7034f4");
        factory.setFactoryName("测试1号工厂666");
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    /**
     * 删除
     */
    @Test
    public void tesetDeleteByPrimaryKey(){
        factoryDao.deleteByPrimaryKey("d55ef6f7-97bb-4e5a-a1d6-93accb7034f4");
    }


    /**
     * 条件查询
     *    selectByExample可以执行
     *       1）每个字段独立条件查询
     *       2）多个条件组合查询（and..)
     *       3）模糊查询（like）
     *       4）非空（is null / is not null）
     *       5）区间（between and...）
     *       ....
     */
    @Test
    public void testSelectByExample(){
        //把需要查询的条件封装成功FactoryExample
        FactoryExample factoryExample = new FactoryExample();

        //获取Criteria对象
        FactoryExample.Criteria criteria = factoryExample.createCriteria();

        //需求：ctype='附件'
        criteria.andCtypeEqualTo("附件");// ctype='附件'
        //需求：full_name like '%%'
        criteria.andFullNameLike("%祁县%");

        //设置排序规则
        //order by phone desc
        factoryExample.setOrderByClause("phone desc");

        List<Factory> factoryList = factoryDao.selectByExample(factoryExample);
        for(Factory factory:factoryList){
            System.out.println(factory);
        }

    }
}