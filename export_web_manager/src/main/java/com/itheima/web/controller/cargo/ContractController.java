package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.Contract;
import com.itheima.domain.cargo.ContractExample;
import com.itheima.service.cargo.ContractService;
import com.itheima.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 购销合同控制器
 */
@Controller
@RequestMapping("/cargo/contract")
public class ContractController extends BaseController {

    @Reference
    private ContractService contractService;


    /**
     * 分页查询
     *   1）URL：http://localhost:8080/cargo/contract/list.do
     *   2）参数：pageNum=1&pageSize=5
     *   3)返回： /WEB-INF/pages/cargo/contract/contract-list.jsp
     */
    @RequestMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize){
        ////当前登录企业的ID
        //String companyId = getLoginCompanyId();
//
        ////1.查询合同数据
        //ContractExample contractExample = new ContractExample();
        ////创建Criteria对象
        //ContractExample.Criteria contractCriteria = contractExample.createCriteria();
//
        ////添加条件
        //contractCriteria.andCompanyIdEqualTo(companyId);//company_id = 'xxx'
        ////按照create_time倒序
        //contractExample.setOrderByClause("create_time desc");
//
        //PageInfo pageInfo = contractService.findByPage(contractExample,pageNum,pageSize);
//
        ////2.存入request域
        //request.setAttribute("pageInfo",pageInfo);
//
        ////3.返回页面
        //return "cargo/contract/contract-list";


        //当前登录企业的ID
        String companyId = getLoginCompanyId();

        //1.查询合同数据
        ContractExample contractExample = new ContractExample();
        //创建Criteria对象
        ContractExample.Criteria contractCriteria = contractExample.createCriteria();

        //添加条件
        contractCriteria.andCompanyIdEqualTo(companyId);//company_id = 'xxx'
        //按照create_time倒序
        contractExample.setOrderByClause("create_time desc");


        //=====================细粒度权限控制：begin==========================================
        //1.获取用户级别
        Integer degree = getLoginUser().getDegree();
        if(degree!=null){
            if(degree==4){
                //普通员工：只能查询自己创建的购销合同
                //create_by = '当前登录用户ID'

                contractCriteria.andCreateByEqualTo(getLoginUser().getId());

            }else if(degree==3){
                //小部门经理: 查看本部门员工创建的合同

                //create_dept = '当前登录用户部门ID'

                contractCriteria.andCreateDeptEqualTo(getLoginUser().getDeptId());
            }else if(degree==2){
                //大部门经理：查询本部门及子部门的所有合同
                PageInfo pageInfo = contractService.selectByDeptId(getLoginUser().getDeptId(),pageNum,pageSize);
                //2.存入request域
                request.setAttribute("pageInfo",pageInfo);
                //3.返回页面
                return "cargo/contract/contract-list";
            }

        }

        //默认为1：企业管理员：查询企业下的所有购销合同
        //======================细粒度权限控制：end=========================================

        PageInfo pageInfo = contractService.findByPage(contractExample,pageNum,pageSize);

        //2.存入request域
        request.setAttribute("pageInfo",pageInfo);

        //3.返回页面
        return "cargo/contract/contract-list";
    }

    /**
     * 进入添加页面
     *  1）URL：http://localhost:8080/cargo/contract/toAdd.do
     *  2）参数：无
     *  3）返回：/WEB-INF/pages/cargo/contract/contract-add.jsp
     */
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "cargo/contract/contract-add";
    }

    /**
     * 保存数据（添加/修改）
     *   1）URL：http://localhost:8080/cargo/contract/edit.do
     *   2）参数：部门表单数据
     *   3）返回：重定向回到列表
     */
    @RequestMapping("/edit")
    public String edit(Contract contract){
        //获取登录企业信息
        String companyId = getLoginCompanyId();
        String companyName = getLoginCompanyName();

        contract.setCompanyId(companyId);
        contract.setCompanyName(companyName);

        //创建人ID
        contract.setCreateBy(getLoginUser().getId());
        //创建人部门ID
        contract.setCreateDept(getLoginUser().getDeptId());

        //判断是否有ID
        if(StringUtils.isEmpty(contract.getId())){
            //添加
            contractService.save(contract);
        }else{
            //修改
            contractService.update(contract);
        }

        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 进入修改页面
     *  1）URL：http://localhost:8080/cargo/contract/toUpdate.do
     *  2)参数：id=1f35c554-3777-419b-b607-f88473068edb
     *  3)返回：/WEB-INF/pages/cargo/contract/contract-update.jsp
     */
    @RequestMapping("/toUpdate")
    public String toUpdate(String id){
        String companyId = getLoginCompanyId();

        //1.根据id查询合同
        Contract contract = contractService.findById(id);
        //3.存入request域
        request.setAttribute("contract",contract);

        return "cargo/contract/contract-update";
    }

    /**
     * 删除部门
     *  1）URL：http://localhost:8080/cargo/contract/delete.do
     *  2）参数：id=1
     *  3）返回：{"flag":true/false,"errorMsg":"xxxxx"}
     */
    @RequestMapping("/delete")
    public String delete(String id){
        contractService.delete(id);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 查询合同
     *  1）URL：http://localhost:8080/cargo/contract/toView.do
     *  2）参数：id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
     *  3）返回：/WEB-INF/pages/cargo/contract/contract-view.jsp
     */
    @RequestMapping("/toView")
    public String toView(String id){
        //根据id 查询合同
        Contract contract = contractService.findById(id);
        request.setAttribute("contract",contract);
        return "cargo/contract/contract-view";
    }
    /**
     * 提交合同
     *  1）URL：http://localhost:8080/cargo/contract/submit.do
     *  2）参数：id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
     *  3）返回：重定向到列表
     */
    @RequestMapping("/submit")
    public String submit(String id){
        //把合同的status改为1
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(1);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }

    /**
     * 取消合同
     *  1）URL：http://localhost:8080/cargo/contract/cancel.do
     *  2）参数：id=dd63eb3c-6d4e-4a85-9c37-fcfda1998c1d
     *  3）返回：重定向到列表
     */
    @RequestMapping("/cancel")
    public String cancel(String id){
        //把合同的status改为1
        Contract contract = new Contract();
        contract.setId(id);
        contract.setState(0);
        contractService.update(contract);
        return "redirect:/cargo/contract/list.do";
    }
}