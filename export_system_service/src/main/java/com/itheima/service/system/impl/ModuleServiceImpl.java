package com.itheima.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.ModuleDao;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleDao moduleDao;

    @Override
    public PageInfo findByPage(int pageNum, int pageSize) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo(moduleDao.findAll());
    }

    @Override
    public List<Module> findAll() {
        return moduleDao.findAll();
    }

    @Override
    public void save(Module module) {
        //生成主键
        module.setId(UUID.randomUUID().toString());
        moduleDao.save(module);
    }

    @Override
    public void update(Module module) {
        moduleDao.update(module);
    }

    @Override
    public Module findById(String id) {
        return moduleDao.findById(id);
    }

    @Override
    public void delete(String id) {
        moduleDao.delete(id);
    }

    /**
     * 查询当前角色分配过的模块
     *
     * @param roleid
     * @return
     */
    @Override
    public List<Module> findRoleModuleByRoleId(String roleid) {
        return moduleDao.findRoleModuleByRoleId(roleid);
    }

    /**
     * 根据不同用户级别查询权限
     *
     * @param loginUser
     * @return
     */
    @Override
    public List<Module> findModuleByUser(User loginUser) {
        //获取用户级别
        Integer degree = loginUser.getDegree();
        if (degree != null) {
            //1.如果用户级别为0，Saas管理员，则查询belong=0的模块
            if (degree==0){
                return moduleDao.findByBelong(0);
            }
            //2.如果用户级别为1，企业管理员，则查询belong=1的模块
            else if(degree==1){
                return moduleDao.findByBelong(1);
            }
            //3.如果用户级别为2,3,4，则根据RBAC表单查询当前用户拥有的模块
            else {
                return moduleDao.findModuleByUserId(loginUser.getId());
            }
        }
        return null;
    }

}
