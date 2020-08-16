package com.itheima.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.company.Company;
import com.itheima.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 企业入驻控制器
 */
@Controller
public class ApplyController {
    //远程注入CompanyService
    @Reference
    private CompanyService companyService;

    /**
     * 企业入驻方法：
     *   1）URL： http://localhost:8082/apply.do
     *   2）参数：企业表单
     *   3）返回值： 1或0
     */
    @RequestMapping("/apply")
    @ResponseBody
    public String apply(Company company){
        try {
            //设置状态为0，未审核
            company.setState(0);
            companyService.save(company);

            return "1";
        }catch(Exception e){
            e.printStackTrace();
            return "0";
        }
    }
}