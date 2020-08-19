package com.itheima.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.domain.cargo.ContractProductVo;
import com.itheima.service.cargo.ContractProductService;
import com.itheima.web.controller.BaseController;
import com.itheima.web.utils.DownloadUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 *
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController{

    @Reference
    private ContractProductService contractProductService;

    /**
     * 进入出货表导出页面
     *  1）http://localhost:8080/cargo/contract/print.do
     *  2）参数：无
     *  3）返回： /WEB-INF/pages/cargo/print/contract-print.jsp
     */
    @RequestMapping("/print")
    public String print(){
        return "cargo/print/contract-print";
    }


    /**
     * 使用Excel模块导出
     * @param inputDate
     * @throws Exception
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {

        //读取Excel的模板
        InputStream inputStream = session.getServletContext()
                .getResourceAsStream("/make/xlsprint/tOUTPRODUCT.xlsx");


        //1.创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        //2.获取第一张工作表
        XSSFSheet sheet = workbook.getSheetAt(0);


        //3.获取标题行
        XSSFRow titleRow = sheet.getRow(0);
        //获取标题单元格
        XSSFCell cell = titleRow.getCell(1);
        //设置内容
        //inputDate 2012-10 -> 2012年10
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        cell.setCellValue(title);


        //4.查询出货表的内容
        List<ContractProductVo> list = contractProductService.findByShipTime(inputDate, getLoginCompanyId());


        //5.获取模板的内容行的每列的样式
        CellStyle[] cellStyles = new CellStyle[8];
        for(int i=0;i<8;i++){
            cellStyles[i] = sheet.getRow(2).getCell(i+1).getCellStyle();
        }


        //6.创建内容行
        if(list!=null && list.size()>0){

            for(int i = 0;i<list.size();i++){
                //取出每个对象
                ContractProductVo vo = list.get(i);

                //创建行
                XSSFRow contentRow = sheet.createRow(i + 2);
                //设置行高
                contentRow.setHeightInPoints(24);
                //客户
                Cell contentCell = null;
                //创建行
                contentCell = contentRow.createCell(1);
                //设置样式
                contentCell.setCellStyle(cellStyles[0]);
                if(vo.getCustomName()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getCustomName());
                }else{
                    contentCell.setCellValue("");
                }

                //创建行
                contentCell = contentRow.createCell(2);
                //设置样式
                contentCell.setCellStyle(cellStyles[1]);
                // 订单号
                if(vo.getContractNo()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getContractNo());
                }else{
                    contentCell.setCellValue("");
                }


                //货号
                //创建行
                contentCell = contentRow.createCell(3);
                //设置样式
                contentCell.setCellStyle(cellStyles[2]);
                if(vo.getProductNo()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getProductNo());
                }else{
                    contentCell.setCellValue("");
                }

                // 数量

                //创建行
                contentCell = contentRow.createCell(4);
                //设置样式
                contentCell.setCellStyle(cellStyles[3]);
                if(vo.getCnumber()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getCnumber());
                }else{
                    contentCell.setCellValue("");
                }
                // 工厂

                //创建行
                contentCell = contentRow.createCell(5);
                //设置样式
                contentCell.setCellStyle(cellStyles[4]);
                if(vo.getFactoryName()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getFactoryName());
                }else{
                    contentCell.setCellValue("");
                }

                // 	工厂交期
                //创建行
                contentCell = contentRow.createCell(6);
                //设置样式
                contentCell.setCellStyle(cellStyles[5]);
                if(vo.getDeliveryPeriod()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getDeliveryPeriod());
                }else{
                    contentCell.setCellValue("");
                }
                // 	船期
                //创建行
                contentCell = contentRow.createCell(7);
                //设置样式
                contentCell.setCellStyle(cellStyles[6]);
                if(vo.getShipTime()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getShipTime());
                }else{
                    contentCell.setCellValue("");
                }
                // 贸易条款

                //创建行
                contentCell = contentRow.createCell(8);
                //设置样式
                contentCell.setCellStyle(cellStyles[7]);
                if(vo.getTradeTerms()!=null){
                    //设置内容
                    contentCell.setCellValue(vo.getTradeTerms());
                }else{
                    contentCell.setCellValue("");
                }
            }

        }


        //7.把工作簿作为文件流写出给用户（response）
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把工作簿字节数据写入到ByteArrayOutputStream流
        workbook.write(byteArrayOutputStream);
        new DownloadUtil().download(byteArrayOutputStream,response,"出货表.xlsx");
    }
}