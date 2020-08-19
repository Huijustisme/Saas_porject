package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ContractProductDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.*;
import com.itheima.service.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 货物实现
 */
@Service // 注意：阿里的@Service注解
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    /**
     * 分页查询
     *
     * @param ContractProductExample 分页查询的参数
     * @param pageNum                当前页
     * @param pageSize               页大小
     * @return
     */
    @Override
    public PageInfo<ContractProduct> findByPage(ContractProductExample ContractProductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(contractProductDao.selectByExample(ContractProductExample));
    }

    /**
     * 查询所有
     *
     * @param ContractProductExample
     */
    @Override
    public List<ContractProduct> findAll(ContractProductExample ContractProductExample) {
        return contractProductDao.selectByExample(ContractProductExample);
    }

    /**
     * 根据id查询
     *
     * @param id
     */
    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param contractProduct
     */
    @Override
    public void save(ContractProduct contractProduct) {
        //===必须记得给ID赋值====
         contractProduct.setId(UUID.randomUUID().toString());
        //设置创建数据和更新数据
        contractProduct.setCreateTime(new Date());
        contractProduct.setUpdateTime(new Date());

         //1.计算货物的总价（总价=单价*数量）
        Double amount = 0d;
        if (contractProduct.getPrice()!=null&&contractProduct.getCnumber()!=null){
            amount = contractProduct.getPrice()*contractProduct.getCnumber();
            contractProduct.setAmount(amount);
        }
        //2.往货物表插入一条记录
         contractProductDao.insertSelective(contractProduct);
        //3. 计算合同表的货物数量（pro_num）+1
        //3.1 查询当前合同，原货物数量
        Contract contact = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //3.2 计算新货物数量
        contact.setProNum(contact.getProNum()+1);
        //4. 计算合同表的总价（合同总价=原合同总价+当前货物总价）
        if (contact.getTotalAmount()!=null){
            contact.setTotalAmount(contact.getTotalAmount()+amount);
        }else {
            contact.setTotalAmount(amount);
        }
        //5. 把计算结果更新到合同表中
        contractDao.updateByPrimaryKeySelective(contact);


    }

    /**
     * 修改
     *
     * @param contractProduct
     */
    @Override
    public void update(ContractProduct contractProduct) {
        //1. 查询原货物总价
        //1.1 根据id查询货物对象
        ContractProduct dbProduct = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        //1.2 取出原货物总价
        Double oldAmount = dbProduct.getAmount();

        //2. 计算新货物的总价（总价=单价*数量）
        Double newAmount = 0d;
        if(contractProduct.getPrice()!=null && contractProduct.getCnumber()!=null){
            newAmount = contractProduct.getPrice() * contractProduct.getCnumber();
            contractProduct.setAmount(newAmount);
        }

        //3. 更新货物表的一条记录
        contractProductDao.updateByPrimaryKeySelective(contractProduct);

        //4. 计算合同表的总价（新合同总价=原合同总价-原货物总价+新货物总价）
        //4.1 根据合同ID查询合同对象
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - oldAmount + newAmount );

        //5. 把计算结果更新到合同表中
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除部门
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //先查询货物对象
        ContractProduct dbProduct = contractProductDao.selectByPrimaryKey(id);

        //1. 删除货物的一条记录
        contractProductDao.deleteByPrimaryKey(id);

        //2. 查询该货物下的附件记录
        ExtCproductExample extCproductExample = new ExtCproductExample();

        ExtCproductExample.Criteria extCproductExampleCriteria = extCproductExample.createCriteria();
        //contract_product_id = 'xxx'
        extCproductExampleCriteria.andContractProductIdEqualTo(id);

        List<ExtCproduct> extCproductList = extCproductDao.selectByExample(extCproductExample);

        //3. 删除该货物下的所有附件记录
        //定义附件总价和
        Double extTotalAmount = 0d;
        if(extCproductList!=null && extCproductList.size()>0){
            for(ExtCproduct extCproduct:extCproductList){
                //计算附件总价和
                extTotalAmount+=extCproduct.getAmount();
                //删除附件
                extCproductDao.deleteByPrimaryKey(extCproduct.getId());
            }
        }

        //4. 计算合同表的货物数量-1
        //4.1 查询当前合同对象
        Contract contract = contractDao.selectByPrimaryKey(dbProduct.getContractId());
        //4.2 更新合同表的货物数量-1
        contract.setProNum(contract.getProNum()-1);

        //5. 计算合同表的附件数量（原附件数量-该货物下的附件记录数）
        if(extCproductList!=null) {
            contract.setExtNum(contract.getExtNum() - extCproductList.size());
        }

        //6. 计算合同表的总价（新合同总价=原合同总价-货物总价-货物下的所有附件总价和）
        contract.setTotalAmount( contract.getTotalAmount() - dbProduct.getAmount() - extTotalAmount );

        //7.更新到合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 根据船期查询出货信息
     *
     * @param shipTime
     * @param companyId
     */
    @Override
    public List<ContractProductVo> findByShipTime(String shipTime, String companyId) {
        return contractProductDao.findByShipTime(shipTime,companyId);
    }
}