package com.itheima.dao.system;



import com.itheima.domain.system.Module;

import java.util.List;

public interface ModuleDao {

    //根据id查询
    Module findById(String moduleId);

    //根据id删除
    void delete(String moduleId);

    //添加
    void save(Module module);

    //更新
    void update(Module module);

    //查询全部
    List<Module> findAll();

    /**
     * 查询当前角色分配过的模块
     * @param roleid
     * @return
     */
    List<Module> findRoleModuleByRoleId(String roleid);

    /**
     * 根据belong查询模块
     * @param i
     * @return
     */
    List<Module> findByBelong(int i);


    /**
     * 查询指定用户分配的权限（模块）
     * @param id
     * @return
     */
    List<Module> findModuleByUserId(String id);
}