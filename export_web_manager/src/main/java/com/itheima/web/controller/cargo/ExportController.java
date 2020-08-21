package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractService;
import com.itheima.service.cargo.ExportProductService;
import com.itheima.service.cargo.ExportService;
import com.itheima.web.controller.BaseController;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 报运控制器
 */
@Controller
@RequestMapping("/cargo/export/")
public class ExportController extends BaseController {
    @Reference
    private ContractService contractService;
    @Reference
    private ExportService exportService;
    @Reference
    private ExportProductService exportProductService;


    /**
     * 分页查询合同管理
     *   1）URL：http://localhost:8080/cargo/export/contractList.do
     *   2）参数：pageNum=1&pageSize=5
     *   3)返回： /WEB-INF/pages/cargo/export/export-contractList.jsp
     */
    @RequestMapping("/contractList")
    public String contractList(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "5") Integer pageSize
                               ){
        //需求：查询本企业状态为1的合同
        ContractExample contractExample = new ContractExample();
        ContractExample.Criteria contractExampleCriteria = contractExample.createCriteria();
        //company_id = 'xxx'
        contractExampleCriteria.andCompanyIdEqualTo(getLoginCompanyId());
        //state = 1
        contractExampleCriteria.andStateEqualTo(1);
        //按照create_time倒序
        contractExample.setOrderByClause("create_time desc");
        PageInfo<Contract> pageInfo = contractService.findByPage(contractExample, pageNum, pageSize);
        //2.存入request域
        request.setAttribute("pageInfo",pageInfo);
        //3.返回页面
        return "cargo/export/export-contractList";
    }

    /**
     * 分页查询报运单
     *   1）URL：http://localhost:8080/cargo/export/list.do
     *   2）参数：pageNum=1&pageSize=5
     *   3)返回： /WEB-INF/pages/cargo/export/export-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        //需求：查询当前企业的报运单
        ExportExample exportExample = new ExportExample();
        ExportExample.Criteria exportExampleCriteria = exportExample.createCriteria();
        //company_id = 'xxx'
        exportExampleCriteria.andCompanyIdEqualTo(getLoginCompanyId());
        //create_time desc
        exportExample.setOrderByClause("create_time desc");
        PageInfo<Export> pageInfo = exportService.findByPage(exportExample, pageNum, pageSize);
        //2.存入request域
        request.setAttribute("pageInfo",pageInfo);
        //3.返回页面
        return "cargo/export/export-list";
    }

    /**
     * 进入新增报运单页面
     *  1）URL：http://localhost:8080/cargo/export/toExport.do
     *  2）参数：勾选的购销合同的ID （n个） id=1&id=2...
     *  3）返回：/WEB-INF/pages/cargo/export/export-toExport.jsp
     */
    @RequestMapping("/toExport")
    public String toExport(String id){ //id: 1,2,3
        request.setAttribute("id",id);
        return "cargo/export/export-toExport";
    }

    /**
     * 保存报运单
     *  1）URL： http://localhost:8080/cargo/export/edit.do
     *  2）参数：报运单表单
     *  3）返回： 重定向/cargo/export/list.do
     */
    @RequestMapping("/edit")
    public String edit(Export export) {
        //企业信息
        export.setCompanyId(getLoginCompanyId());
        export.setCompanyName(getLoginCompanyName());
        //设置创建人
        export.setCreateBy(getLoginUser().getId());
        //设置创建人部门
        export.setCreateDept(getLoginUser().getDeptId());
        if (StringUtils.isEmpty(export.getId())){
            exportService.save(export);
        }
        else {
            exportService.update(export);
        }
        return "redirect:/cargo/export/list.do";
    }
    /**
     * 进入报运单修改
     *  1）URL：http://localhost:8080/cargo/export/toUpdate.do
     *  2）参数：id=555eec51-1f50-451e-8516-13ec0f00cf9e
     *  3)返回：/WEB-INF/pages/cargo/export/export-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.查询报运单
        Export export = exportService.findById(id);
        //2.查询报运单下的商品
        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria exportProductExampleCriteria = exportProductExample.createCriteria();

        //export_id = 'xxx'

        exportProductExampleCriteria.andExportIdEqualTo(id);
        List<ExportProduct> exportProductList = exportProductService.findAll(exportProductExample);

        request.setAttribute("export",export);
        request.setAttribute("eps",exportProductList);
        return "cargo/export/export-update";
    }

    /**
     * 提交报运单
     *  1）URL:http://localhost:8080/cargo/export/submit.do
     *  2）参数:id=555eec51-1f50-451e-8516-13ec0f00cf9e
     *  3)返回：重定向列表
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //把state改为1
        Export export = new Export();
        export.setId(id);
        export.setState(1);
        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 取消报运单
     *  1）URL:http://localhost:8080/cargo/export/cancel.do
     *  2）参数:id=555eec51-1f50-451e-8516-13ec0f00cf9e
     *  3)返回：重定向列表
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //把state改为0
        Export export = new Export();
        export.setId(id);
        export.setState(0);
        exportService.update(export);

        return "redirect:/cargo/export/list.do";
    }

    /**
     * 电子报运：
     *  1）URL:http://localhost:8080/cargo/export/exportE.do
     *  2)参数：id=555eec51-1f50-451e-8516-13ec0f00cf9e
     *  3)返回值：重定向列表
     */
    @RequestMapping("/exportE")
    public String exportE(String id){

        //1. 查询当前报运单和报运商品信息

        //1.1 查询报运单
        Export export = exportService.findById(id);

        //1.2 查询报运商品
        ExportProductExample exportProductExample = new ExportProductExample();
        ExportProductExample.Criteria exportProductExampleCriteria = exportProductExample.createCriteria();

        //export_id = 'xxx'
        exportProductExampleCriteria.andExportIdEqualTo(id);

        List<ExportProduct> exportProducts = exportProductService.findAll(exportProductExample);

        //2. 把报运单和报运商品信息封装ExportVo对象
        ExportVo exportVo = new ExportVo();

        //复制数据到ExportVo
        BeanUtils.copyProperties(export,exportVo);
        //设置exportId（报运单ID, 注意： export_id就是Saas系统和海关系统联系的字段）
        exportVo.setExportId(id);

        if(exportProducts!=null && exportProducts.size()>0){
            for(ExportProduct exportProduct:exportProducts){
                ExportProductVo exportProductVo = new ExportProductVo();

                //复制数据
                BeanUtils.copyProperties(exportProduct,exportProductVo);

                //设置报运单ID
                exportProductVo.setExportId(id);

                //设置报运商品ID
                exportProductVo.setExportProductId(exportProduct.getId());

                exportVo.getProducts().add(exportProductVo);
            }


        }

        //3. 远程调用海关系统的电子报关方法，把ExportVo转换为Json发送过去
        WebClient
                .create("http://localhost:8087/ws/export/user")
                .post(exportVo);


        //4. 远程调用海关系统的查询报关结果方法，返回json格式，转换为ExportResult对象
        ExportResult exportResult = WebClient
                .create("http://localhost:8087/ws/export/user/"+id)
                .get(ExportResult.class);

        //5. 根据报关结果，修改报运单的状态（state字段）为2 ，更新报运商品的税收金额（tax字段）
        exportService.updateExport(exportResult);

        return "redirect:/cargo/export/list.do";
    }
}