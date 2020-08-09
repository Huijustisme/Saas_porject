package com.itheima.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.RoleDao;
import com.itheima.domain.system.Role;
import com.itheima.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public PageInfo findByPage(int pageNum, int pageSize,String companyId) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo(roleDao.findAll(companyId));
    }

    @Override
    public List<Role> findAll(String companyId) {
        return roleDao.findAll(companyId);
    }

    @Override
    public void save(Role role) {
        //生成主键
        role.setId(UUID.randomUUID().toString());
        roleDao.save(role);
    }

    @Override
    public void update(Role role) {
        roleDao.update(role);
    }

    @Override
    public Role findById(String id) {
        return roleDao.findById(id);
    }

    @Override
    public void delete(String id) {
        roleDao.delete(id);
    }

    /**
     * 保存角色和模块的关系
     *
     * @param roleid
     * @param moduleIds
     */
    @Override
    public void updateRoleModule(String roleid, String moduleIds) {
        //1.先删除当前角色分配过的所有权限
        roleDao.deleteRoleModuleByRoleId(roleid);

        //2.给当前角色逐一添加当前勾选的权限
        if(!StringUtils.isEmpty(moduleIds)){
            String[] moduleIdArray = moduleIds.split(",");
            for(String moduleId:moduleIdArray){
                roleDao.saveRoleModule(roleid,moduleId);
            }
        }
    }

    /**
     * 查询用户分配过的角色
     *
     * @param id
     * @return
     */
    @Override
    public List<Role> findUserRoleByUserId(String id) {
        return roleDao.findUserRoleByUserId(id);
    }

}
