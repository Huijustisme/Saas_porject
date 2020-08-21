package com.itheima.service.cargo.impl;

import com.itheima.dao.cargo.*;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ExportService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.*;

/**
 * 报运单的业务实现
 */
@Service // 注意：必须是dubbo的注解
public class ExportServiceImpl implements ExportService{
    @Autowired
    private ExportDao exportDao;//报运Dao
    @Autowired
    private ContractDao contractDao;//合同Dao
    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public PageInfo<Export> findByPage(ExportExample exportExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(exportDao.selectByExample(exportExample));
    }

    /**
     * 根据报运结果更新报运信息
     *
     * @param exportResult
     */
    @Override
    public void updateExport(ExportResult exportResult) {
        //1.更新报运单状态
        if(exportResult.getState()==2){
            Export export = new Export();
            export.setId(exportResult.getExportId());
            export.setState(2);
            exportDao.updateByPrimaryKeySelective(export);


            //2.更新报运商品税收
            Set<ExportProductResult> products = exportResult.getProducts();
            if(products!=null && products.size()>0){
                for(ExportProductResult exportProductResult:products){
                    ExportProduct exportProduct = new ExportProduct();
                    exportProduct.setId(exportProductResult.getExportProductId());
                    exportProduct.setTax(exportProductResult.getTax());
                    exportProductDao.updateByPrimaryKeySelective(exportProduct);
                }
            }
        }


    }

    @Override
    public List<Export> findAll(ExportExample exportExample) {
        return exportDao.selectByExample(exportExample);
    }

    @Override
    public Export findById(String id) {
        return exportDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Export export) {
        export.setId(UUID.randomUUID().toString());
        //定义货物数量 及 附件数量
        Integer proNum = 0;
        Integer extNum = 0;
        //1. 设置报运单的状态为0
        export.setState(0);
        //设置时间
        export.setInputDate(new Date());
        export.setCreateTime(new Date());
        //切割合同ID
        String contractIds = export.getContractIds();
        String[] contractIdArray = contractIds.split(",");
        //根据合同ID查询合同对象
        String contractNoStr="";
        for (String contractId : contractIdArray) {
            Contract contract = contractDao.selectByPrimaryKey(contractId);
            contractNoStr += contract.getContractNo()+"";
            //计算报运单的货物数量 及 附件数量
            proNum += contract.getProNum();
            extNum += contract.getExtNum();
        }
        //去掉空格
        contractNoStr = contractNoStr.trim();
        //设置合同号
        export.setCustomerContract(contractNoStr);
        //设置货物和附件的数量
        export.setProNum(proNum);
        export.setExtNum(extNum);
        //2. 往报运单表添加一条记录
        exportDao.insertSelective(export);
        //3. 修改报运单下的所有购销合同的状态（改为2）
        //4. 把报运单下的所有购销合同的货物 拷贝到 报运商品表 下
        //5. 把报运单下的所有购销合同的附件  拷贝到 报运商品附件表 下
        exportDao.insertSelective(export);

    }

    @Override
    public void update(Export export) {
        //更新报运单
        exportDao.updateByPrimaryKeySelective(export);

        //更新报运商品数据
        List<ExportProduct> exportProducts = export.getExportProducts();
        if(exportProducts!=null && exportProducts.size()>0){

            for(ExportProduct exportProduct:exportProducts){
                exportProductDao.updateByPrimaryKeySelective(exportProduct);
            }
        }
    }

    @Override
    public void delete(String id) {
        exportDao.deleteByPrimaryKey(id);
    }
}
