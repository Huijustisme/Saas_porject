package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Role;
import com.itheima.domain.system.User;
import com.itheima.service.system.DeptService;
import com.itheima.service.system.RoleService;
import com.itheima.service.system.UserService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    //注入业务对象
    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;

    /**
     * 查询所有用户数据接口(API)
     *    1）访问路径： http://localhost:8080/system/user/list.do
     *    2) 方法参数: pageNum=1&pageSize=5
     *    3) 方法返回值： /WEB-INF/pages/system/user/user-list.jsp
     *
     *  @RequestParam:
     *     name：改变参数的接收名称
     *     defaultValue: 给指定参数设置默认值
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){

        //当前登录的企业ID
        String companyId = getLoginCompanyId();

        //调用分页方法
        PageInfo pageInfo = userService.findByPage(pageNum,pageSize,companyId);

        request.setAttribute("pageInfo",pageInfo);

        return "system/user/user-list";
    }

    /**
     * 进入用户添加
     *    1）1）访问路径： http://localhost:8080/system/user/toAdd.do
     *    2) 方法参数: 无
     *    3) 方法返回值： /WEB-INF/pages/system/user/user-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){

        String companyId = getLoginCompanyId();
        //提前查询当前企业的所有部门，以便展示部门列表
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);

        return "system/user/user-add";
    }
    /**
     * 保存数据
     *    1）访问路径： http://localhost:8080/system/user/edit.do
     *    2) 方法参数: User对象
     *    3) 方法返回值： 重定向列表
     */
    @RequestMapping("/edit")
    public String edit(User user){
        //模拟当前登录企业
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        //手动赋值企业信息
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);

        //判断id是否为空
        if(StringUtils.isEmpty(user.getId())){
            //新增
            userService.save(user);
        }else{
            //修改
            userService.update(user);
        }

        return "redirect:/system/user/list.do";
    }

    /**
     * 进入修改页面
     *    1）访问路径： http://localhost:8080/system/user/toUpdate.do
     *    2) 方法参数: id=1
     *    3) 方法返回值： /WEB-INF/pages/system/user/user-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){

        //1.根据id查询用户
        User user = userService.findById(id);
        request.setAttribute("user",user);

        //2.查询所有用户作为上级用户
        String companyId = getLoginCompanyId();
        //提前查询当前企业的所有部门，以便展示部门列表
        List<Dept> deptList = deptService.findAll(companyId);
        request.setAttribute("deptList",deptList);

        return "system/user/user-update";
    }

    /**
     * 删除用户
     *   1）访问路径： http://localhost:8080/system/user/delete.do
     *   2) 方法参数: id=1
     *   3) 方法返回值： {flag:false,message:'错误信息'}
     *
     *   @ResponseBody：把Java对象转换为json字符串，返回给前端。方法返回值上或方法上
     *   @RequestBody： 接收页面的json字符串，转换成Java对象。方法参数上
     */
    @RequestMapping("/delete")
    @ResponseBody//把Java对象转换为Json字符串
    public Map<String,Object> delete(String id){
        Map<String,Object> map = new HashMap<>();

        boolean flag = userService.delete(id);

        if(flag){
            //成功
            map.put("flag",true);
        }else{
            //失败
            map.put("flag",false);
            map.put("errorMsg","用户存在关联数据，无法删除");
        }

        return map;
    }

    @Autowired
    private RoleService roleService;
    /**
     * 进入用户分配角色页面
     *  1）URL： http://localhost:8080/system/user/roleList.do
     *  2)参数：id=002108e2-9a10-4510-9683-8d8fd1d374ef
     *  3）返回：/WEB-INF/pages/system/user/user-role.jsp
     */
    @RequestMapping("/roleList")
    public String roleList(String id){
        //1.查询当前企业的所有角色
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        //2.查询当前用户分配过的角色
        List<Role> userRoleList = roleService.findUserRoleByUserId(id);

        request.setAttribute("roleList",roleList);
        request.setAttribute("userRoleList",userRoleList);

        //3.查询当前用户
        User user = userService.findById(id);
        request.setAttribute("user",user);

        return "system/user/user-role";
    }

    /**
     * 保存用户和角色的关系
     *  1）URL: http://localhost:8080/system/user//changeRole.do
     *  2)参数：userid=1&roleIds=1&roleIds=2&roleIds=3....
     *  3）返回：重定向到列表
     *
     *     接收多个同名的参数的情况：
     *      1）String： 把所有参数值以逗号分隔拼接成一个字符串
     *      2）String[]: 使用数组分别接收每个参数值
     *      3）List<String>: 不能直接使用集合接收，必须加上@RequestParam注解接收
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userid,@RequestParam("roleIds") List<String> roleIds){

        //调用业务
        userService.changeRole(userid,roleIds);

        return "redirect:/system/user/list.do";
    }
}