package com.itheima.service.stat.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.stat.StatDao;
import com.itheima.service.stat.StatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 统计Service实现
 */
@Service
public class StatServiceImpl implements StatService {
    @Autowired
    private StatDao statDao;

    @Override
    public List<Map<String, Object>> getFactoryData(String companyId) {
        return statDao.getFactoryData(companyId);
    }


    /**
     * 销售排行（统计前5名）
     *
     * @param companyId
     */
    @Override
    public List<Map<String, Object>> getSellData(String companyId) {
        return statDao.getFactoryData(companyId);
    }

    /**
     * 系统访问压力图
     *
     * @param companyId
     */
    @Override
    public List<Map<String, Object>> getOnlineData(String companyId) {
        return statDao.getOnlineData(companyId);
    }
}