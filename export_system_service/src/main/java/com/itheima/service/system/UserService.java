package com.itheima.service.system;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.User;

import java.util.List;

/**
 * 用户service
 */
public interface UserService {

    /**
     * 分页查询用户
     */
    public PageInfo findByPage(int pageNum, int pageSize, String companyId);

    /**
     * 查询所有用户
     * @param companyId
     * @return
     */
    List<User> findAll(String companyId);

    /**
     * 添加
     * @param user
     */
    void save(User user);

    /**
     * 修改
     * @param user
     */
    void update(User user);

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    User findById(String id);

    /**
     * 删除用户
     * @param id
     * @return
     */
    boolean delete(String id);

    /**
     * 保存 用户和角色的关系
     * @param userid
     * @param roleIds
     */
    void changeRole(String userid, List<String> roleIds);
}
