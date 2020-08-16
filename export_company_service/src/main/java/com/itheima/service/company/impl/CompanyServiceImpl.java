package com.itheima.service.company.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.company.CompanyDao;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

/**
 * 企业Service实现
 */
@Service // 必须注意：记得把Spring的@Service改为阿里的@Service
public class CompanyServiceImpl implements CompanyService {
    //注入Dao
    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        //必须给ID赋值
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

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
        //2.查询数据（调用查询全部的方法）
        List<Company> list = companyDao.findAll();
        //3.封装数据
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }
}