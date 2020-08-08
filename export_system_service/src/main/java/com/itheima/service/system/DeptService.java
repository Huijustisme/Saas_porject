package com.itheima.service.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;

import java.util.List;

/**
 * 部门Service接口
 */
public interface DeptService {
    /**
     * 分页查询
     */
    public PageInfo findByPage(Integer pageNum,Integer pageSize,String companyId);

    /**
     * 查询所有部门
     * @param companyId 公司的ID
     * @return
     */
    List<Dept> findAll(String companyId);


    /**
     * 新增
     * @param dept
     */
    void save(Dept dept);

    /**
     * 修改
     * @param dept
     */
    void update(Dept dept);

    /**
     * 根据id查部门
     * @param id
     * @return
     */
    Dept findById(String id);

    /**
     * 删除部门
     * @param id
     * @return
     */
    boolean delete(String id);
}
