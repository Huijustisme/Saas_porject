package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.domain.system.Module;
import com.itheima.domain.system.Role;
import com.itheima.service.system.DeptService;
import com.itheima.service.system.ModuleService;
import com.itheima.service.system.RoleService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色控制器
 */
@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {
    //注入业务对象
    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;

    /**
     * 查询所有角色数据接口(API)
     *    1）访问路径： http://localhost:8080/system/role/list.do
     *    2) 方法参数: pageNum=1&pageSize=5
     *    3) 方法返回值： /WEB-INF/pages/system/role/role-list.jsp
     *
     *  @RequestParam:
     *     name：改变参数的接收名称
     *     defaultValue: 给指定参数设置默认值
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "5") int pageSize){

        //当前登录的角色ID
        String companyId = getLoginCompanyId();

        //调用分页方法
        PageInfo pageInfo = roleService.findByPage(pageNum,pageSize,companyId);

        request.setAttribute("pageInfo",pageInfo);

        return "system/role/role-list";
    }

    /**
     * 进入角色添加
     *    1）1）访问路径： http://localhost:8080/system/role/toAdd.do
     *    2) 方法参数: 无
     *    3) 方法返回值： /WEB-INF/pages/system/role/role-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){

        //String companyId = getLoginCompanyId();
        //提前查询当前企业的所有部门，以便展示部门列表
        //List<Dept> deptList = deptService.findAll(companyId);
        ///request.setAttribute("deptList",deptList);

        return "system/role/role-add";
    }
    /**
     * 保存数据
     *    1）访问路径： http://localhost:8080/system/role/edit.do
     *    2) 方法参数: Role对象
     *    3) 方法返回值： 重定向列表
     */
    @RequestMapping("/edit")
    public String edit(Role role){
        //模拟当前登录企业
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        //手动赋值企业信息
        role.setCompanyId(companyId);
        role.setCompanyName(companyName);

        //判断id是否为空
        if(StringUtils.isEmpty(role.getId())){
            //新增
            roleService.save(role);
        }else{
            //修改
            roleService.update(role);
        }

        return "redirect:/system/role/list.do";
    }

    /**
     * 进入修改页面
     *    1）访问路径： http://localhost:8080/system/role/toUpdate.do
     *    2) 方法参数: id=1
     *    3) 方法返回值： /WEB-INF/pages/system/role/role-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        String companyId = getLoginCompanyId();

        //1.根据id查询角色
        Role role = roleService.findById(id);
        request.setAttribute("role",role);

        //2.查询所有角色作为上级角色
        //String companyId = getLoginCompanyId();
        //提前查询当前企业的所有部门，以便展示部门列表
        //List<Dept> deptList = deptService.findAll(companyId);
        //request.setAttribute("deptList",deptList);

        return "system/role/role-update";
    }

    /**
     * 删除角色
     *   1）访问路径： http://localhost:8080/system/role/delete.do
     *   2) 方法参数: id=1
     *   3) 方法返回值： {flag:false,message:'错误信息'}
     *
     *   @ResponseBody：把Java对象转换为json字符串，返回给前端。方法返回值上或方法上
     *   @RequestBody： 接收页面的json字符串，转换成Java对象。方法参数上
     */
    @RequestMapping("/delete")
    @ResponseBody//把Java对象转换为Json字符串
    public Map<String,Object> delete(String id){
        //Map<String,Object> map = new HashMap<>();

        //boolean flag = roleService.delete(id);

        //if(flag){
        //    //成功
        //    map.put("flag",true);
        //}else{
        //    //失败
        //    map.put("flag",false);
        //    map.put("errorMsg","角色存在关联数据，无法删除");
        //}

        //return map;
        return null;
    }

    /**
     * 进入角色分配权限页面
     *  1）URL：http://localhost:8080/system/role/roleModule.do
     *  2）参数：roleid=4028a1c34ec2e5c8014ec2ebf8430001
     *  3）返回：/WEB-INF/pages/system/role/role-module.jsp
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid){
        //1.查询当前角色
        Role role = roleService.findById(roleid);
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    @Autowired
    private ModuleService moduleService;
    /**
     * 加载模块树状结构
     *  1）URL： http://localhost:8080/system/role/getZtreeNodes.do
     *  2）参数： roleid=1
     *  3）返回： [ {id:1,name:'',pId:1,open:true,checked:true},{} ]
     *
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody//转换为json字符串
    public List<Map<String,Object>> getZtreeNodes(String roleid){
        //List<Map<String,Object>>： 封装树的所有节点数据
        List<Map<String,Object>> list = new ArrayList<>();
        //查询所有模块列表
        List<Module> moduleList = moduleService.findAll();
        //查询当前角色分配过的模块列表
        List<Module> roleModuleList = moduleService.findRoleModuleByRoleId(roleid);
        //把模块数据存入Map中
        if (moduleList!=null&&moduleList.size()>0){
            for (Module module : moduleList) {
                //使用一个Map封装树的一个节点数据
                Map<String,Object> map = new HashMap<>();
                //id
                map.put("id",module.getId());
                //name
                map.put("name",module.getName());
                //pId
                map.put("pId",module.getParentId());
                //open
                map.put("open",true);
                //让哪些分配过角色的模块添上复选框勾选
                for (Module module2 : roleModuleList) {
                    if (module.getId().equals(module2.getId())){
                        map.put("checked",true);
                    }
                }
                //把Map放入List中
                list.add(map);
            }
        }
         return list;
    }

    /**
     * 保存角色和模块的关系
     *  1）URL：http://localhost:8080/system/role/updateRoleModule.do
     *  2）参数：roleid=1&moduleIds=1,2,3
     *  3）返回：重定向到列表
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleid,String moduleIds){

        //调用service方法
        roleService.updateRoleModule(roleid,moduleIds);

        return "redirect:/system/role/list.do";
    }
}