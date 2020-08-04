package com.itheima.web.controller.company;

import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 企业控制器
 */
@Controller
@RequestMapping("/company")
public class CompanyController {
    //注入业务对象
    @Autowired
    private CompanyService companyService;

/**
 * 查询所有企业数据接口(API)
 *    1）访问路径： http://localhost:8080/company/list.do
 *    2) 方法参数: 无
 *    3) 方法返回值： /WEB-INF/pages/company/company-list.jsp
 *
 */
@RequestMapping("/list")
    public String list(HttpServletRequest request){
    List<Company> list = companyService.findAll();
    //数据传递到页面
    /**
     * 三种方式：
     *  1）HttpServletRequest.setAttribute(key,value)
     *  2）Model/ModelMap.addObject(key,value)
     *  3) ModelAndView
     */
    request.setAttribute("list",list);
    //最终应该添加前缀和后缀：/WEB-INF/pages/company/company-list.jsp
    System.out.println(list);
    System.out.println("已存入对象");
    return "company/company-list";
    }

    /**
     * 添加企业
     */
    @RequestMapping("/save")
    public String save(Date date){
        int i =10/0;
        System.out.println(date);
        return "success";
    }
}