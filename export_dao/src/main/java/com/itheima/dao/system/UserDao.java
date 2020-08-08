package com.itheima.dao.system;



import com.itheima.domain.system.User;

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
}