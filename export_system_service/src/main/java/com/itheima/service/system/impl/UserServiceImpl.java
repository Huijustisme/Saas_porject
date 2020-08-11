package com.itheima.service.system.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.system.ModuleDao;
import com.itheima.dao.system.UserDao;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.User;
import com.itheima.service.system.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public PageInfo findByPage(int pageNum, int pageSize,String companyId) {
        //1.设置分页参数
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo(userDao.findAll(companyId));
    }

    @Override
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    @Override
    public void save(User user) {
        //生成主键
        user.setId(UUID.randomUUID().toString());
        //加盐加密
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), user.getEmail());
        user.setPassword(md5Hash.toString());
        userDao.save(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public boolean delete(String id) {
        //1.判断用户是否绑定了角色
        long count = userDao.findRoleByUserId(id);

        //1.1 绑定了，不能删除
        if(count>0){
            return false;
        }

        //1.2 没绑定，看删除
        userDao.delete(id);
        return true;
    }

    @Override
    public void changeRole(String userid, List<String> roleIds) {

        //1.删除用户分配的角色
        userDao.deleteUserRoleByUserId(userid);

        //2.给用户分配当前勾选的角色
        if(roleIds!=null && roleIds.size()>0){
            for(String roleId:roleIds){
                userDao.saveUserRole(userid,roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }


}
