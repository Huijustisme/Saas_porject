package com.itheima.dao.cargo;

import com.itheima.dao.stat.StatDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * 测试统计Dao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext-dao.xml")
public class StatDaoTest {

    @Autowired
    private StatDao statDao;

    @Test
    public void testInsert(){
        List<Map<String, Object>> list = statDao.getFactoryData("1");
        System.out.println(list);
    }

}
