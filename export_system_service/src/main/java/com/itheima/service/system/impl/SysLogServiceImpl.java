package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.SysLogDao;
import com.itheima.domain.system.SysLog;
import com.itheima.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统日志实现类
 */
@Service
public class SysLogServiceImpl implements SysLogService {
    @Autowired
    private SysLogDao sysLogDao;

    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, String companyId) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo(sysLogDao.findAll(companyId));
    }

    @Override
    public void save(SysLog sysLog) {
        sysLogDao.save(sysLog);
    }
}