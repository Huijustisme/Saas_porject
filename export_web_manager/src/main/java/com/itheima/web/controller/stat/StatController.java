package com.itheima.web.controller.stat;

import com.itheima.service.stat.StatService;
import com.itheima.web.controller.BaseController;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 */
@Controller
@RequestMapping("/stat")
public class StatController extends BaseController{

    @Reference
    private StatService statService;

    /**
     * 进入统计展示页面
     *  1）URL： http://localhost:8080/stat/toCharts.do
     *  2）参数：chartsType=factory
     *  3）返回值： /WEB-INF/pages/stat/stat-xxx.jsp
     */
    @RequestMapping("/toCharts")
    public String toCharts(String chartsType){
        return "stat/stat-"+chartsType;
    }

    /**
     * 厂家销量方法
     */
    @RequestMapping("/getFactoryData")
    @ResponseBody
    public List<Map<String,Object>> getFactoryData(){
        return statService.getFactoryData(getLoginCompanyId());
    }

    /**
     * 销量前5名
     */
    @RequestMapping("/getSellData")
    @ResponseBody
    public List<Map<String,Object>> getSellData(){
        return statService.getSellData(getLoginCompanyId());
    }

    /**
     * 系统访问人数统计
     */
    @RequestMapping("/getOnlineData")
    @ResponseBody
    public List<Map<String,Object>> getOnlineData(){
        return statService.getOnlineData(getLoginCompanyId());
    }
}
