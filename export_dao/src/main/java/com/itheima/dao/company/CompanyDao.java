package com.itheima.dao.company;

import com.itheima.domain.company.Company;

import java.util.List;
/*
企业dao接口
 */
public interface CompanyDao {
    //查询全部企业
    List<Company> findAll();
}
