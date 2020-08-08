package com.itheima.dao.system;

import com.itheima.domain.system.Dept;

import java.util.List;

/**
 * 部门Dao接口
 */
public interface DeptDao {
    /**
     * 查询所有部门（针对某企业来查询）
     */
    public List<Dept> findAll(String companyId);

    /**
     * 根据ID查询部门（目的：是为了封装Dept对象中的parent对象）
     */
    public Dept findById(String id);

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
     * 查询部门的子部门数量
     * @param id
     * @return
     */
    long findDeptByParentId(String id);

    /**
     * 删除部门
     * @param id
     */
    void delete(String id);
}
