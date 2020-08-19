package com.itheima.dao;

import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.FactoryDao;
import com.itheima.domain.cargo.ContractProductVo;
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
public class ContractProductDaoTest {

    @Autowired
    private ContractProductDao contractProductDao;


    @Test
    public void testFindByShipTime(){
        List<ContractProductVo> list = contractProductDao.findByShipTime("2015-01", "1");
        System.out.println(list);
    }

}