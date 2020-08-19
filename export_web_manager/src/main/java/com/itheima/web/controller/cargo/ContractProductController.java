package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.service.cargo.FactoryService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.utils.FileUploadUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 货物控制器
 */
@Controller
@RequestMapping("/cargo/contractProduct")
public class ContractProductController extends BaseController {
    @Reference  //货物Service
    private ContractProductService contractProductService;
    @Reference  //厂家Service
    private FactoryService factoryService;

    /**
     * 分页查询
     *   1）URL：http://localhost:8080/cargo/contractProduct/list.do
     *   2）参数：pageNum=1&pageSize=5&contractId=6f6ad40d-ada9-470d-ab6f-ad8bfe97dd91
     *   3)返回： /WEB-INF/pages/cargo/product/product-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize,
                       String contractId
                       ) {
        //1.查询生产货物的厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        //ctype='货物'
        factoryExampleCriteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);

        //2.查询指定合同下的货物
        ContractProductExample contractProductExample = new ContractProductExample();
        ContractProductExample.Criteria contractProductExampleCriteria =
                contractProductExample.createCriteria();
                //contract_id='xxx'
        contractProductExampleCriteria.andContractIdEqualTo(contractId);
        PageInfo<ContractProduct> pageInfo =
                contractProductService.findByPage(contractProductExample,pageNum,pageSize);
        //3.存入request域数据
        request.setAttribute("factoryList",factoryList);
        request.setAttribute("pageInfo",pageInfo);
        //必须携带一个contractId，给添加货物表单的隐藏域赋值，以便新增货物的时候可以绑定该合同
        request.setAttribute("contractId",contractId);
        //3.返回页面
        return "cargo/product/product-list";
    }


    //注入文件上传工具类
    @Autowired
    private FileUploadUtil fileUploadUtil;

    /**
     * 保存数据（添加/修改）
     *   1）URL：http://localhost:8080/cargo/contractProduct/edit.do
     *   2）参数：货物表单数据
     *   3）返回：重定向回到列表
     *
     *  注意：Controller的 productPhoto属性名称 必须和页面的form的file的name名称一致
     */
    @RequestMapping("/edit")
    public String edit(ContractProduct contractProduct, MultipartFile productPhoto) throws Exception {
        //获取登录企业信息
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        contractProduct.setCompanyId(companyId);
        contractProduct.setCompanyName(companyName);

        //判断是否有ID
        if (StringUtils.isEmpty(contractProduct.getId())){
            if(productPhoto!=null) {
                //处理上传货物图片（上传到七牛云）
                String imgURL = "http://" + fileUploadUtil.upload(productPhoto); // http://qf3evwai0.hn-bkt.clouddn.com/FqV1HtSFrCXfcBxW2RjFGo2Y7t_U
                //把七牛的文件路径保存到数据库中
                contractProduct.setProductImage(imgURL);
            }

            //添加
            contractProductService.save(contractProduct);
        }
        else {
            //修改
            contractProductService.update(contractProduct);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractProduct.getContractId();
    }



    /**
     * 进入修改页面
     *  1）URL：http://localhost:8080/cargo/contractProduct/toUpdate.do
     *  2)参数：id=1df16b89-97dd-45c9-b2c4-93d7543b85f3
     *  3)返回：/WEB-INF/pages/cargo/product/product-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        //1.根据id查询货物
        ContractProduct contractProduct = contractProductService.findById(id);
        //2.查询类型为'货物'的厂家
        FactoryExample factoryExample = new FactoryExample();
        FactoryExample.Criteria factoryExampleCriteria = factoryExample.createCriteria();
        //ctype='货物
        factoryExampleCriteria.andCtypeEqualTo("货物");
        List<Factory> factoryList = factoryService.findAll(factoryExample);
        //3.存入request
        request.setAttribute("contractProduct",contractProduct);
        request.setAttribute("factoryList",factoryList);
        return "cargo/product/product-update";
    }

    /**
     * 删除货物
     *  1）URL：http://localhost:8080/cargo/contractProduct/delete.do
     *  2）参数：id=df5b50a9-2174-4572-9ada-9909b38d5392&contractId=6f6ad40d-ada9-470d-ab6f-ad8bfe97dd91
     *  3）返回：重定向回列表
     */
    @RequestMapping("/delete")
    public String delete(String id,String contractId){
        //调用业务
        contractProductService.delete(id);
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

    /**
     * 批量导入货物
     *  1）URL： http://localhost:8080/cargo/contractProduct/toImport.do
     *  2)参数：contractId=1bba3c2d-bcf5-46c8-831a-a6caf2ef0ebb
     *  3）返回：/WEB-INF/pages/cargo/product/product-import.jsp
     */
    @RequestMapping("/toImport")
    public String toImport(String contractId){
        request.setAttribute("contractId",contractId);
        return "cargo/product/product-import";
    }

    /**
     * 批量导入
     *  1）URL：http://localhost:8080/cargo/contractProduct/import.do
     *  2）参数：contractId=1
     *  3）返回：重定向到货物列表
     */
    @RequestMapping("/import")
    public String productImport(String contractId,MultipartFile file) throws Exception {
        //file名称和表单name一致的

        //1.获取文件后缀
        String fileName = file.getOriginalFilename();
        //截取后缀
        String extName = fileName.substring(fileName.lastIndexOf("."));//.xls 或 .xlsx
        //2.判断属于哪种excel版本
        Workbook workbook = null;
        if (extName.equals(".xls")){
            //低版本
            //3.读取工作簿
            workbook = new HSSFWorkbook(file.getInputStream());
        }
        else {
            //高版本
            //3.读取工作簿
            workbook = new XSSFWorkbook(file.getInputStream());
        }
        //4.读取工作表
        Sheet sheet = workbook.getSheetAt(0);
        //5.读取行（遍历行）
        //5.1 获取有内容的行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        //5.2 从下标1的行开始遍历
        for (int i = 1; i < rowNum; i++) {
            //取出行
            Row row = sheet.getRow(i);
            //6.把每列的数据封装到一个ContractProduct对象中
            ContractProduct product = new ContractProduct();
            //厂家名称
            if (row.getCell(1) != null) {
                product.setFactoryName(row.getCell(1).getStringCellValue());
            }
            //货号
            //货号
            if(row.getCell(2)!=null){
                product.setProductNo(row.getCell(2).getStringCellValue());
            }

            //数量
            if(row.getCell(3)!=null){
                product.setCnumber((int)row.getCell(3).getNumericCellValue());
            }

            //包装单位
            if(row.getCell(4)!=null){
                product.setPackingUnit(row.getCell(4).getStringCellValue());
            }

            //装率
            if(row.getCell(5)!=null){
                product.setLoadingRate(row.getCell(5).getNumericCellValue()+"");
            }

            //箱数
            if(row.getCell(6)!=null){
                product.setBoxNum((int)row.getCell(6).getNumericCellValue());
            }

            //单价
            if(row.getCell(7)!=null){
                product.setPrice(row.getCell(7).getNumericCellValue());
            }

            //货物描述
            if(row.getCell(8)!=null){
                product.setProductDesc(row.getCell(8).getStringCellValue());
            }

            //要求
            if(row.getCell(9)!=null){
                product.setProductRequest(row.getCell(9).getStringCellValue());
            }

            //补充其他数据
            //1.合同ID
            product.setContractId(contractId);
            //2.根据工厂名称查询工厂
            Factory factory = factoryService.findByFactoryName(product.getFactoryName());
            product.setFactoryId(factory.getId());
            //3.设置公司信息
            product.setCompanyId(getLoginCompanyId());
            product.setCompanyName(getLoginCompanyName());
            //7.把ContractProduct对象，调用Service保存方法
            contractProductService.save(product);
        }
        return "redirect:/cargo/contractProduct/list.do?contractId="+contractId;
    }

}