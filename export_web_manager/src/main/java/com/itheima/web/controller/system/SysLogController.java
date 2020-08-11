package com.itheima.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.itheima.service.system.SysLogService;
import com.itheima.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 日志控制器
 */
@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {
    @Autowired
    private SysLogService sysLogService;


    /**
     * 分页查询
     *   1）URL：http://localhost:8080/system/log/list.do
     *   2）参数：pageNum=1&pageSize=5
     *   3)返回： /WEB-INF/pages/system/log/log-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        //当前登录企业的ID
        String companyId = getLoginCompanyId();

        //1.查询日志数据
        PageInfo pageInfo = sysLogService.findByPage(pageNum,pageSize,companyId);

        //2.存入request域
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "system/log/log-list";
    }
}