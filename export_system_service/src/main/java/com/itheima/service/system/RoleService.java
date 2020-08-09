package com.itheima.service.system;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Role;

import java.util.List;

/**
 * 角色service
 */
public interface RoleService {

    /**
     * 分页查询角色
     */
    public PageInfo findByPage(int pageNum, int pageSize, String companyId);

    /**
     * 查询所有角色
     * @param companyId
     * @return
     */
    List<Role> findAll(String companyId);

    /**
     * 添加
     * @param role
     */
    void save(Role role);

    /**
     * 修改
     * @param role
     */
    void update(Role role);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Role findById(String id);

    /**
     * 删除角色
     * @param id
     * @return
     */
    void delete(String id);

    /**
     * 保存角色和模块的关系
     * @param roleid
     * @param moduleIds
     */
    void updateRoleModule(String roleid, String moduleIds);

    /**
     * 查询用户分配过的角色
     * @param id
     * @return
     */
    List<Role> findUserRoleByUserId(String id);
}
