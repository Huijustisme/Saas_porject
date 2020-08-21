package com.itheima.dao.stat;

import java.util.List;
import java.util.Map;

/**
 * 统计分析接口
 */
public interface StatDao {
    /**
     * 厂家销售统计
     */
    public List<Map<String,Object>> getFactoryData(String companyId);

    /**
     * 统计产品销量前5名
     */
    public List<Map<String,Object>> getSellData(String companyId);

    /**
     * 系统访问压力图
     */
    List<Map<String,Object>>getOnlineData(String companyId);
}
