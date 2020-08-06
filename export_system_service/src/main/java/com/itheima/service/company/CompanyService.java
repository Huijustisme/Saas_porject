package com.itheima.service.company;

import com.github.pagehelper.PageInfo;
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

    /**
     * 添加
     */
    public void save(Company company);

    /**
     * 更新
     */
    public void update(Company company);

    //根据id查询
    Company findById(String id);

    //删除
    void delete(String id);

    //分页查询企业
    public PageInfo<Company> findByPage(int pageNum, int pageSize);
}
