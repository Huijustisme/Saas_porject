package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.SysLog;

/**
 * 系统日志接口
 */
public interface SysLogService {
    /**
     * 分页查询
     */
    public PageInfo findByPage(Integer pageNum, Integer pageSize, String companyId);


    /**
     * 插入日志
     */
    public void save(SysLog sysLog);
}
