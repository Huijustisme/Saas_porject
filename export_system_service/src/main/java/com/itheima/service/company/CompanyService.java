package com.itheima.service.company;

import com.itheima.domain.company.Company;

import java.util.List;

/**
 * 企业Service接口
 */
public interface CompanyService {

    /**
     * 查询所有
     */
    public List<Company> findAll();
}
