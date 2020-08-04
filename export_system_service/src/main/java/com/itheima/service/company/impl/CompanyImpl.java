package com.itheima.service.company.impl;

import com.itheima.dao.company.CompanyDao;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业Service实现
 */
@Service
public class CompanyImpl implements CompanyService {
    @Autowired
    private CompanyDao companyDao;

    /**
     * 查询所有
     */
    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }
}