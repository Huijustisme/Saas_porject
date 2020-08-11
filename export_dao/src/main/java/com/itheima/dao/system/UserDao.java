package com.itheima.dao.system;



import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface UserDao {

	//根据企业id查询全部
	List<User> findAll(String companyId);

	//根据id查询
    User findById(String userId);

	//根据id删除
	void delete(String userId);

	//保存
	void save(User user);

	//更新
	void update(User user);

    /**
     * 用户绑定角色的记录数
     * @param id
     * @return
     */
    long findRoleByUserId(String id);

    /**
     * 查询用户关联部门的数量
     * @param id
     * @return
     */
    long findUserByDeptId(String id);

    /**
     * 删除有用户分配的角色
     * @param userid
     */
    void deleteUserRoleByUserId(String userid);

    /**
     * 给用户分配角色
     * @param userid
     * @param roleId
     */
    void saveUserRole(@Param("userid") String userid, @Param("roleId") String roleId);

    /**
     * 通过用户账号查询用户对象
     * @param email
     * @return
     */
    User findByEmail(String email);


}