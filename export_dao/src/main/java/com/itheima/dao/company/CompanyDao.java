package com.itheima.dao.company;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;

import java.util.List;
/*
企业dao接口
 */
public interface CompanyDao {
    //查询全部企业
    List<Company> findAll();
    //添加
    void save(Company company);
    //修改
    void update(Company company);
    //根据id查询
    Company findById(String id);
    //删除
    void delete(String id);

}
