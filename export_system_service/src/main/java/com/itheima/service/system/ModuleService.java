package com.itheima.service.system;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Module;

import java.util.List;

/**
 * 模块service
 */
public interface ModuleService {

    /**
     * 分页查询模块
     */
    public PageInfo findByPage(int pageNum, int pageSize);

    /**
     * 查询所有模块
     * @return
     */
    List<Module> findAll();

    /**
     * 添加
     * @param module
     */
    void save(Module module);

    /**
     * 修改
     * @param module
     */
    void update(Module module);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Module findById(String id);

    /**
     * 删除模块
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 查询当前角色分配过的模块
     * @param roleid
     * @return
     */
    List<Module> findRoleModuleByRoleId(String roleid);
}
