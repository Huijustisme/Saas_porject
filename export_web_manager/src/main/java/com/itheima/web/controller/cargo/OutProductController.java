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
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
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
 * 出货表控制器
 */
@Controller
@RequestMapping("/cargo/contract")
public class OutProductController extends BaseController{

    @Reference
    private ContractProductService contractProductService;

    /**
     * 进入出货表页面
     *  1）URL： http://localhost:8080/cargo/contract/print.do
     *  2）参数：无
     *  3）返回： /WEB-INF/pages/cargo/print/contract-print.jsp
     */
    @RequestMapping("/print")
    public String print(){
        return "cargo/print/contract-print";
    }

    /**
     * Excel导出出货表
     *  1）URL：http://localhost:8080/cargo/contract/printExcel.do
     *  2)参数：inputDate=2020-04
     *  3）返回：Excel文件
     */
    /*@RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {

        //1.创建工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();

        //2.创建工作表
        HSSFSheet sheet = workbook.createSheet("出货表");

        //合并单元格
        //CellRangeAddress: 里面包含四个参数，依次是，开始行，结束行，开始列，结束列
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        //设置列宽度
        *//**
     * 参数一：列索引
     * 参数二：列宽度值， 256个单位 = 1个字符数
     *//*
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,20*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,11*256);
        sheet.setColumnWidth(5,11*256);
        sheet.setColumnWidth(6,11*256);
        sheet.setColumnWidth(7,11*256);
        sheet.setColumnWidth(8,11*256);

        //3.创建标题行
        HSSFRow titleRow = sheet.createRow(0);
        //设置行高
        titleRow.setHeightInPoints(36);//36磅
        //创建标题单元格
        HSSFCell cell = titleRow.createCell(1);
        //设置内容
        //inputDate 2012-10 -> 2012年10
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        //设置标题的样式
        cell.setCellStyle(bigTitle(workbook));
        cell.setCellValue(title);

        //4.创建表头行
        HSSFRow headerRow = sheet.createRow(1);
        //设置行高
        headerRow.setHeightInPoints(26);
        String[] headerStrArray = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for(int i=0;i<headerStrArray.length;i++){
            //创建单元格
            cell = headerRow.createCell(i+1);
            //设置样式
            cell.setCellStyle(title(workbook));
            //设置数据
            cell.setCellValue(headerStrArray[i]);
        }

        //5.查询出货表的内容
        List<ContractProductVo> list = contractProductService.findByShipTime(inputDate, getLoginCompanyId());

        //6.创建内容行
        if(list!=null && list.size()>0){

            for(int i = 0;i<list.size();i++){
                //取出每个对象
                ContractProductVo vo = list.get(i);

                //创建行
                HSSFRow contentRow = sheet.createRow(i + 2);
                //设置行高
                contentRow.setHeightInPoints(24);
                //客户
                Cell contentCell = null;
                if(vo.getCustomName()!=null){
                    //创建行
                    contentCell = contentRow.createCell(1);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getCustomName());
                }else{
                    contentCell.setCellValue("");
                }


                // 订单号
                if(vo.getContractNo()!=null){
                    //创建行
                    contentCell = contentRow.createCell(2);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getContractNo());
                }else{
                    contentCell.setCellValue("");
                }

                //货号
                if(vo.getProductNo()!=null){
                    //创建行
                    contentCell = contentRow.createCell(3);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getProductNo());
                }else{
                    contentCell.setCellValue("");
                }

                // 数量
                if(vo.getCnumber()!=null){
                    //创建行
                    contentCell = contentRow.createCell(4);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getCnumber());
                }else{
                    contentCell.setCellValue("");
                }
                // 工厂
                if(vo.getFactoryName()!=null){
                    //创建行
                    contentCell = contentRow.createCell(5);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getFactoryName());
                }else{
                    contentCell.setCellValue("");
                }
                // 	工厂交期
                if(vo.getDeliveryPeriod()!=null){
                    //创建行
                    contentCell = contentRow.createCell(6);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getDeliveryPeriod());
                }else{
                    contentCell.setCellValue("");
                }
                // 	船期
                if(vo.getShipTime()!=null){
                    //创建行
                    contentCell = contentRow.createCell(7);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
                    //设置内容
                    contentCell.setCellValue(vo.getShipTime());
                }else{
                    contentCell.setCellValue("");
                }
                // 贸易条款
                if(vo.getTradeTerms()!=null){
                    //创建行
                    contentCell = contentRow.createCell(8);
                    //设置样式
                    contentCell.setCellStyle(text(workbook));
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
        new DownloadUtil().download(byteArrayOutputStream,response,"出货表.xls");
    }*/

    /**
     * 使用Excel模块导出
     * @throws Exception
     */
    /*@RequestMapping("/printExcel")
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

            int index = 2;
            for(int j=0;j<4000;j++) { // 169*4000

                for (int i = 0; i < list.size(); i++) {
                    //取出每个对象
                    ContractProductVo vo = list.get(i);

                    //创建行
                    XSSFRow contentRow = sheet.createRow(index++);
                    //设置行高
                    contentRow.setHeightInPoints(24);
                    //客户
                    Cell contentCell = null;
                    //创建行
                    contentCell = contentRow.createCell(1);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[0]);
                    if (vo.getCustomName() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getCustomName());
                    } else {
                        contentCell.setCellValue("");
                    }

                    //创建行
                    contentCell = contentRow.createCell(2);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[1]);
                    // 订单号
                    if (vo.getContractNo() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getContractNo());
                    } else {
                        contentCell.setCellValue("");
                    }


                    //货号
                    //创建行
                    contentCell = contentRow.createCell(3);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[2]);
                    if (vo.getProductNo() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getProductNo());
                    } else {
                        contentCell.setCellValue("");
                    }

                    // 数量

                    //创建行
                    contentCell = contentRow.createCell(4);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[3]);
                    if (vo.getCnumber() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getCnumber());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 工厂

                    //创建行
                    contentCell = contentRow.createCell(5);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[4]);
                    if (vo.getFactoryName() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getFactoryName());
                    } else {
                        contentCell.setCellValue("");
                    }

                    // 	工厂交期
                    //创建行
                    contentCell = contentRow.createCell(6);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[5]);
                    if (vo.getDeliveryPeriod() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getDeliveryPeriod());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 	船期
                    //创建行
                    contentCell = contentRow.createCell(7);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[6]);
                    if (vo.getShipTime() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getShipTime());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 贸易条款

                    //创建行
                    contentCell = contentRow.createCell(8);
                    //设置样式
                    contentCell.setCellStyle(cellStyles[7]);
                    if (vo.getTradeTerms() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getTradeTerms());
                    } else {
                        contentCell.setCellValue("");
                    }
                }
            }

        }


        //7.把工作簿作为文件流写出给用户（response）
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //把工作簿字节数据写入到ByteArrayOutputStream流
        workbook.write(byteArrayOutputStream);
        new DownloadUtil().download(byteArrayOutputStream,response,"出货表.xlsx");
    }
*/
    //大标题的样式
    public CellStyle bigTitle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short)16);
        font.setBold(true);//字体加粗
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        return style;
    }

    //小标题的样式
    public CellStyle title(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short)12);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);				//横向居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线
        return style;
    }

    //文字样式
    public CellStyle text(Workbook wb){
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontName("Times New Roman");
        font.setFontHeightInPoints((short)10);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.LEFT);				//横向居左
        style.setVerticalAlignment(VerticalAlignment.CENTER);		//纵向居中
        style.setBorderTop(BorderStyle.THIN);						//上细线
        style.setBorderBottom(BorderStyle.THIN);					//下细线
        style.setBorderLeft(BorderStyle.THIN);						//左细线
        style.setBorderRight(BorderStyle.THIN);						//右细线

        return style;
    }

    /**
     *
     * 使用SXXFWorkBook导出近百万条数据
     */
    @RequestMapping("/printExcel")
    public void printExcel(String inputDate) throws Exception {

        //1.创建工作簿
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        //2.创建工作表
        Sheet sheet = workbook.createSheet("出货表");

        //合并单元格
        //CellRangeAddress: 里面包含四个参数，依次是，开始行，结束行，开始列，结束列
        sheet.addMergedRegion(new CellRangeAddress(0,0,1,8));

        //设置列宽度
        sheet.setColumnWidth(0,6*256);
        sheet.setColumnWidth(1,26*256);
        sheet.setColumnWidth(2,20*256);
        sheet.setColumnWidth(3,26*256);
        sheet.setColumnWidth(4,11*256);
        sheet.setColumnWidth(5,11*256);
        sheet.setColumnWidth(6,11*256);
        sheet.setColumnWidth(7,11*256);
        sheet.setColumnWidth(8,11*256);

        //3.创建标题行
        Row titleRow = sheet.createRow(0);
        //设置行高
        titleRow.setHeightInPoints(36);//36磅
        //创建标题单元格
        Cell cell = titleRow.createCell(1);
        //设置内容
        //inputDate 2012-10 -> 2012年10
        String title = inputDate.replaceAll("-0","年").replaceAll("-","年")+"月份出货表";
        //设置标题的样式
        cell.setCellStyle(bigTitle(workbook));
        cell.setCellValue(title);

        //4.创建表头行
        Row headerRow = sheet.createRow(1);
        //设置行高
        headerRow.setHeightInPoints(26);
        String[] headerStrArray = {"客户","订单号","货号","数量","工厂","工厂交期","船期","贸易条款"};
        for(int i=0;i<headerStrArray.length;i++){
            //创建单元格
            cell = headerRow.createCell(i+1);
            //设置样式
            cell.setCellStyle(title(workbook));
            //设置数据
            cell.setCellValue(headerStrArray[i]);
        }

        //5.查询出货表的内容
        List<ContractProductVo> list = contractProductService.findByShipTime(inputDate, getLoginCompanyId());

        //6.创建内容行
        if(list!=null && list.size()>0){
            int index=2;
            for(int j=0;j<4000;j++) {
                for (int i = 0; i < list.size(); i++) {
                    //取出每个对象
                    ContractProductVo vo = list.get(i);

                    //创建行
                    Row contentRow = sheet.createRow(index++);
                    //设置行高
                    contentRow.setHeightInPoints(24);
                    //客户
                    Cell contentCell = null;
                    //创建行
                    contentCell = contentRow.createCell(1);
                    if (vo.getCustomName() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getCustomName());
                    } else {
                        contentCell.setCellValue("");
                    }
                    //创建行
                    contentCell = contentRow.createCell(2);

                    // 订单号
                    if (vo.getContractNo() != null) {

                        //设置内容
                        contentCell.setCellValue(vo.getContractNo());
                    } else {
                        contentCell.setCellValue("");
                    }

                    //货号
                    //创建行
                    contentCell = contentRow.createCell(3);
                    if (vo.getProductNo() != null) {

                        //设置内容
                        contentCell.setCellValue(vo.getProductNo());
                    } else {
                        contentCell.setCellValue("");
                    }

                    // 数量
                    //创建行
                    contentCell = contentRow.createCell(4);
                    if (vo.getCnumber() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getCnumber());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 工厂
                    //创建行
                    contentCell = contentRow.createCell(5);
                    if (vo.getFactoryName() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getFactoryName());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 	工厂交期
                    //创建行
                    contentCell = contentRow.createCell(6);
                    if (vo.getDeliveryPeriod() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getDeliveryPeriod());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 	船期
                    //创建行
                    contentCell = contentRow.createCell(7);
                    if (vo.getShipTime() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getShipTime());
                    } else {
                        contentCell.setCellValue("");
                    }
                    // 贸易条款
                    //创建行
                    contentCell = contentRow.createCell(8);
                    if (vo.getTradeTerms() != null) {
                        //设置内容
                        contentCell.setCellValue(vo.getTradeTerms());
                    } else {
                        contentCell.setCellValue("");
                    }

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
