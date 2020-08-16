package com.itheima.web.controller.company;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import com.itheima.web.controller.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 企业控制器
 */
@Controller
@RequestMapping("/company")
public class CompanyController extends BaseController {
    //注入业务对象
    //@Autowired
    @Reference//本地调用改为远程调用
    private CompanyService companyService;

/**
 * 查询所有企业数据接口(API)
 *    1）访问路径： http://localhost:8080/company/list.do
 *    2) 方法参数: 无
 *    3) 方法返回值： /WEB-INF/pages/company/company-list.jsp
 *
 *//*
@RequestMapping("/list")
    public String list(HttpServletRequest request){
    List<Company> list = companyService.findAll();
    //数据传递到页面
    *//**
     * 三种方式：
     *  1）HttpServletRequest.setAttribute(key,value)
     *  2）Model/ModelMap.addObject(key,value)
     *  3) ModelAndView
     *//*
    request.setAttribute("list",list);
    //最终应该添加前缀和后缀：/WEB-INF/pages/company/company-list.jsp
    System.out.println(list);
    System.out.println("已存入对象");
    return "company/company-list";
    }*/

    /**
     * 方法的API：
     *    方法URL： http://localhost:8080/company/list.do
     *    方法参数： pageNum=1&pageSize=5
     *    方法返回值： /WEB-INF/pages/company/company-list.jsp
     *
     *    @RequestParam(defaultValue = "1")： 给参数设置默认值，当前参数为NULL使用默认值
     */
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list( //HttpServletRequest request,
                        @RequestParam(defaultValue = "1")Integer pageNum,
                        @RequestParam(defaultValue = "5")Integer pageSize){
        //判断当前用户是否有企业管理权限
        //Subject subject = SecurityUtils.getSubject();
        //subject.checkPermission("企业管理");
        //subject.checkPermission("用户管理");
        //调用业务方法
        PageInfo pageInfo = companyService.findByPage(pageNum,pageSize);
        //把数据存入request域
        request.setAttribute("pageInfo",pageInfo);
        //返回jsp页面
        return "company/company-list";
    }



    /**
     * 跳转新增页面
     *   1）URL：http://localhost:8080/company/toAdd.do
     *   2）无
     *   3）返回：/WEB-INF/pages/company/company-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        System.out.println("即将跳转页面");
        return "company/company-add";
    }



    /**
     * 保存数据（添加/修改）
     *  1）URL： http://localhost:8080/company/edit.do
     *  2）参数： 企业的表单所有数据
     *  3）返回： 重定向回到列表
     */
    @RequestMapping("/edit")
    public String edit(Company company){
        //判断是否存在id值
        if (StringUtils.isEmpty(company.getId())){
            //不存在ID值，为添加
            companyService.save(company);
        }else {
            //存在ID值，为修改
            companyService.update(company);
        }
        return "redirect:/company/list.do";
    }



    /**
     * 进入修改页面
     *  1）URL： http://localhost:8080/company/toUpdate.do
     *  2）参数：id=1
     *  3）返回：/WEB-INF/pages/company/company-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){ //,HttpServletRequest request
        //1.查询一个企业对象
        Company company = companyService.findById(id);
        //2.存入request域
        request.setAttribute("company",company);
        return "company/company-update";
    }

    /*
    根据id删除
     */
    @RequestMapping("/delete")
    public String  delete(String id){
        //2.调用service删除
        companyService.delete(id);
        //重定向到企业列表
        return "redirect:/company/list.do";
    }

    /**
     * 添加企业
     */
    @RequestMapping("/save")
    public String save(Date date){
        //int i =10/0;
        System.out.println(date);
        return "success";
    }

}