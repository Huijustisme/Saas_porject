package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.domain.cargo.Factory;
import com.itheima.domain.cargo.FactoryExample;
import com.itheima.service.cargo.ExtCproductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 附件控制器
 */
@Controller
@RequestMapping("/cargo/extCproduct")
public class ExtCproductController extends BaseController {
    @Reference(timeout = 1000000)
    private ExtCproductService extCproductService;//附件Service
    @Reference
    private FactoryService factoryService;//厂家Service

    /**
     * 分页查询
     *   1）URL：http://localhost:8080/cargo/extCproduct/list.do
     *   2）参数：pageNum=1&pageSize=5&contractId=1&contractProductId=1
     *   3)返回： /WEB-INF/pages/cargo/extc/extc-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1")Integer pageNum,
                       @RequestParam(defaultValue = "5")Integer pageSize,
                       String contractId,
                       String contractProductId){
        //1.查询生产附件的厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        //ctype='附件'
        factoryExampleCriteria.andCtypeEqualTo("附件");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //2.查询指定货物下的附件
        ExtCproductExample extCproductExample = new ExtCproductExample();
        ExtCproductExample.Criteria extCproductExampleCriteria = extCproductExample.createCriteria();
        //contract_product_id='xx
        extCproductExampleCriteria.andContractProductIdEqualTo(contractProductId);
        PageInfo<ExtCproduct> pageInfo = extCproductService.findByPage(extCproductExample, pageNum, pageSize);
        //3.存入request域数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        //必须把附件添加所需要的合同ID和货物ID传递到表单的隐藏域，以便保存附件的时候关联合同与货物
        request.setAttribute("contractId",contractId);
        request.setAttribute("contractProductId",contractProductId);
        //3.返回页面
        return "cargo/extc/extc-list";
    }

    //注入文件上传工具类
    @Autowired
    private FileUploadUtil fileUploadUtil;
    /**
     * 保存数据（添加/修改）
     *   1）URL：http://localhost:8080/cargo/extCproduct/edit.do
     *   2）参数：附件表单数据
     *   3）返回：重定向回到列表
     *
     *  注意：Controller的 productPhoto属性名称 必须和页面的form的file的name名称一致
     */
    @RequestMapping("/edit")
    public String edit(ExtCproduct extCproduct, MultipartFile productPhoto) throws Exception {
        //获取登录企业信息
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        extCproduct.setCompanyId(companyId);
        extCproduct.setCompanyName(companyName);

        //判断是否有ID
        if(StringUtils.isEmpty(extCproduct.getId())){
            if(productPhoto!=null) {
                //处理上传附件图片（上传到七牛云）
                String imgURL = "http://" + fileUploadUtil.upload(productPhoto); // http://qf3evwai0.hn-bkt.clouddn.com/FqV1HtSFrCXfcBxW2RjFGo2Y7t_U
                //把七牛的文件路径保存到数据库中
                extCproduct.setProductImage(imgURL);
            }
            //添加
            extCproductService.save(extCproduct);
        }else{
            //修改
            extCproductService.update(extCproduct);
        }

        return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
    }

    /**
     * 进入修改页面
     *  1）URL：http://localhost:8080/cargo/extCproduct/toUpdate.do
     *  2)参数：id=1df16b89-97dd-45c9-b2c4-93d7543b85f3
     *  3)返回：/WEB-INF/pages/cargo/extc/extc-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.根据id查询附件
        ExtCproduct extCproduct = extCproductService.findById(id);

        //2.查询类型为'附件'的厂家
        FactoryExample factoryExample = new FactoryExample();

        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        //ctype='附件'
        factoryExampleCriteria.andCtypeEqualTo("附件");

        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //3.存入request
        request.setAttribute("extCproduct",extCproduct);
        request.setAttribute("factoryList",factoryList);

        request.setAttribute("contractId",extCproduct.getContractId());
        request.setAttribute("contractProductId",extCproduct.getContractProductId());

        return "cargo/extc/extc-update";
    }

    /**
     * 删除附件
     *  1）URL：http://localhost:8080/cargo/extCproduct/delete.do
     *  2）参数：id=1&contractId=1&contractProductId=1
     *  3）返回：重定向回列表
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId,String contractProductId){
        //调用业务
        extCproductService.delete(id);
        return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
    }
}