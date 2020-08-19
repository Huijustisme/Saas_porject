package com.itheima.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.dao.cargo.ContractDao;
import com.itheima.dao.cargo.ExtCproductDao;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ExtCproduct;
import com.itheima.domain.cargo.ExtCproductExample;
import com.itheima.service.cargo.ExtCproductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 购销附件实现
 */
@Service // 注意：阿里的@Service注解
public class ExtCProductServiceImpl implements ExtCproductService {
    @Autowired
    private ExtCproductDao extCproductDao;
    @Autowired
    private ContractDao contractDao;//合同Dao

    /**
     * 分页查询
     *
     * @param extCproductExample
     * @param pageNum
     * @param pageSize
     */
    @Override
    public PageInfo<ExtCproduct> findByPage(ExtCproductExample extCproductExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(extCproductDao.selectByExample(extCproductExample));
    }

    /**
     * 查询所有
     *
     * @param extCproductExample
     */
    @Override
    public List<ExtCproduct> findAll(ExtCproductExample extCproductExample) {
        return extCproductDao.selectByExample(extCproductExample);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public ExtCproduct findById(String id) {
        return extCproductDao.selectByPrimaryKey(id);
    }

    /**
     * 新增
     *
     * @param extCproduct
     */
    @Override
    public void save(ExtCproduct extCproduct) {
        extCproduct.setId(UUID.randomUUID().toString());
        extCproduct.setCreateTime(new Date());
        extCproduct.setUpdateTime(new Date());
        //1.计算附件总价（单价*数量）
        Double amount = 0d;
        if(extCproduct.getPrice()!=null && extCproduct.getCnumber()!=null){
            amount = extCproduct.getPrice() * extCproduct.getCnumber();
            extCproduct.setAmount(amount);
        }

        //2.插入一条附件表记录
        extCproductDao.insertSelective(extCproduct);

        //3.计算新合同的总价（原合同总价+附件总价）
        //3.1 获取合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        if(contract.getTotalAmount()!=null){
            contract.setTotalAmount( contract.getTotalAmount() + amount  );
        }else{
            contract.setTotalAmount(amount);
        }

        //4.计算合同的附件数量（原合同的附件数+1）
        contract.setExtNum(contract.getExtNum()+1);

        //5.更新合同表的数据
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 修改
     *
     * @param extCproduct
     */
    @Override
    public void update(ExtCproduct extCproduct) {
        //1.计算当前附件的总价（数量cnumber*单价price）
        Double newAmount = 0d;
        if(extCproduct.getPrice()!=null && extCproduct.getCnumber()!=null){
            newAmount = extCproduct.getPrice() * extCproduct.getCnumber();
            extCproduct.setAmount(newAmount);
        }

        //2.获取原附件的总价
        ExtCproduct dbExtCproduct = extCproductDao.selectByPrimaryKey(extCproduct.getId());
        Double oldAmount = dbExtCproduct.getAmount();

        //3.修改附件表的一条记录
        extCproductDao.updateByPrimaryKeySelective(extCproduct);

        //4.计算购销合同的总价（新合同总价=合同原总价-原附件总价+新附件总价）
        //4.1 获取合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount( contract.getTotalAmount() - oldAmount + newAmount );

        //5.更新购销合同表
        contractDao.updateByPrimaryKeySelective(contract);
    }

    /**
     * 删除部门
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        //查询附件对象
        ExtCproduct extCproduct = extCproductDao.selectByPrimaryKey(id);

        //1.删除附件表的一条记录
        extCproductDao.deleteByPrimaryKey(id);

        //2.计算合同的总价（原合同的总价-附件总价）
        //2.1 查询合同对象
        Contract contract = contractDao.selectByPrimaryKey(extCproduct.getContractId());
        contract.setTotalAmount(contract.getTotalAmount() - extCproduct.getAmount());

        //3.计算合同的附件数量（原附件数量-1）
        contract.setExtNum( contract.getExtNum() - 1 );

        //4.更新合同表的记录
        contractDao.updateByPrimaryKeySelective(contract);
    }
}