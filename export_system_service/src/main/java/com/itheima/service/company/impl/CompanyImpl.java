package com.itheima.service.company.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.company.CompanyDao;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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

    /**
     * 添加
     *
     * @param company
     */
    @Override
    public void save(Company company) {
        //必须给ID赋值
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    /**
     * 更新
     *
     * @param company
     */
    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {
        //1.设置分页的参数
        PageHelper.startPage(pageNum,pageSize);
        //2.查询数据（调用查询的全部的方法）
        List<Company> list = companyDao.findAll();
        //3.封装数据
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}