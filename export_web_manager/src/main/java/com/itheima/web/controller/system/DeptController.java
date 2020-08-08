package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.domain.system.Dept;
import com.itheima.service.system.DeptService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@Controller
@RequestMapping("/system/dept")
public class DeptController extends BaseController {
    @Autowired
    private DeptService deptService;


    /**
     * 分页查询
     *   1）URL：http://localhost:8080/system/dept/list.do
     *   2）参数：pageNum=1&pageSize=5
     *   3)返回： /WEB-INF/pages/system/dept/dept-list.jsp
     */
    @RequestMapping("/list")
    public String list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize,
            HttpServletRequest request
    ){
        //当前登录企业的id
        //String companyId = "1";
        String companyId = getLoginCompanyId();
        //查询部门数据
        PageInfo pageInfo = deptService.findByPage(pageNum, pageSize, companyId);
        //存入request域
        request.setAttribute("pageInfo",pageInfo);
        //返回页面
        return "system/dept/dept-list";
    }

    /**
     * 进入添加页面
     *  1）URL：http://localhost:8080/system/dept/toAdd.do
     *  2）参数：无
     *  3）返回：/WEB-INF/pages/system/dept/dept-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(
           // HttpServletRequest request
    ){
        //String companyId = "1";
        String companyId = getLoginCompanyId();
        //查询当前企业的所有部门，为了展示下来列表
        List<Dept> list = deptService.findAll(companyId);
        //存入request
        request.setAttribute("deptList",list);
        return "system/dept/dept-add";
    }

    /**
     * 保存数据（添加/修改）
     *   1）URL：http://localhost:8080/system/dept/edit.do
     *   2）参数：部门表单数据
     *   3）返回：重定向回到列表
     */
    @RequestMapping("/edit")
    public String edit(Dept dept){
        //获取登录企业信息
        //String companyId = "1";
        String companyId = getLoginCompanyId();
        //String companyName = "传播智客教育股份有限公司";
        String companyName = getLoginCompanyName();

        dept.setCompanyId(companyId);
        dept.setCompanyName(companyName);

        //判断是否有ID
        if (StringUtils.isEmpty(dept.getId())){
            //添加
            deptService.save(dept);
        }else {
            //修改
            deptService.update(dept);
        }
        return "redirect:/system/dept/list.do";//重定向返回查询分页页面
    }

    /**
     * 进入修改页面
     *  1）URL：http://localhost:8080/system/dept/toUpdate.do
     *  2)参数：id=1f35c554-3777-419b-b607-f88473068edb
     *  3)返回：/WEB-INF/pages/system/dept/dept-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(
            String id
            //HttpServletRequest request
    ){
        //String companyId = "1";
        String companyId = getLoginCompanyId();
        //根据ID查询部门
        Dept dept = deptService.findById(id);
        //查询当前企业的所有部门
        List<Dept> list = deptService.findAll(companyId);
        //存入request域
        request.setAttribute("dept",dept);
        request.setAttribute("deptList",list);
        return "system/dept/dept-update";
    }

    /**
     * 删除部门
     *  1）URL：http://localhost:8080/system/dept/delete.do
     *  2）参数：id=1
     *  3）返回：{"flag":true/false,"errorMsg":"xxxxx"}
     */
    @RequestMapping("/delete")
    @ResponseBody//把java对象转换为json字符串
    public Map<String,Object>delete(String id){
        Map<String,Object> map = new HashMap<>();
        boolean flag = deptService.delete(id);
        if (flag) {
            //成功
            map.put("flag",true);
        }else {
            //失败
            map.put("flag",false);
            map.put("errorMsg","部门存在关联数据，无法删除");
        }
        return map;
    }

}