package com.itheima.dao.system;



import com.itheima.domain.system.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface RoleDao {

    //根据id查询
    Role findById(String id);

    //查询全部
    List<Role> findAll(String companyId);

	//根据id删除
    void delete(String id);

	//添加
    void save(Role role);

	//更新
    void update(Role role);

    /**
     * 删除角色分配过的权限
     * @param roleid
     */
    void deleteRoleModuleByRoleId(String roleid);

    /**
     * 给角色添加权限
     * @param roleid
     * @param moduleId
     * 注意：在MyBatis中如果有多个参数的情况，需要添加@Param注解定义参数别名
     */
    void saveRoleModule(@Param("roleid")String roleid, @Param("moduleId")String moduleId);

    /**
     * 查询用户分配的角色
     * @param id
     * @return
     */
    List<Role> findUserRoleByUserId(String id);
}