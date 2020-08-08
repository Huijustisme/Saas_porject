package com.itheima.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.DeptDao;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 部门Service实现
 */
@Service
public class DeptServiceImpl implements DeptService {
    @Autowired
    private DeptDao deptDao;

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param companyId
     */
    @Override
    public PageInfo findByPage(Integer pageNum, Integer pageSize, String companyId) {
        //设置分页参数
        PageHelper.startPage(pageNum,pageSize);
        //查询数据
        List<Dept> list = deptDao.findAll(companyId);
        return new PageInfo(list);
    }

    /**
     * 查询所有部门
     * @param companyId 公司的ID
     * @return
     */
    @Override
    public List<Dept> findAll(String companyId) {
        return deptDao.findAll(companyId);
    }

    /**
     * 新增
     * @param dept
     */
    @Override
    public void save(Dept dept) {
        dept.setId(UUID.randomUUID().toString());
        deptDao.save(dept);
    }

    /**
     * 修改
     * @param dept
     */
    @Override
    public void update(Dept dept) {
        deptDao.update(dept);
    }

    /**
     * 根据id查部门
     * @param id
     * @return
     */
    @Override
    public Dept findById(String id) {
        return deptDao.findById(id);
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    public boolean delete(String id) {
        //1）如果删除的部门，有子部门，不能直接，提示用户“该部门存在其他关联，不能删除”
        //1.1 查询该部门是否存在子部门
        long count = deptDao.findDeptByParentId(id);
        if (count>0) {
            //有子部门
            return false;
        }

        //2）如果删除的部门，没有子部门，直接删除，提示用户“删除成功”
        deptDao.delete(id);
        return true;
    }
}