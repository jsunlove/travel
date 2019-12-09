package com.qingmu.travel.controller;

import com.qingmu.travel.pojo.Travel;
import com.qingmu.travel.service.TravelService;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Controller
public class TravelController {

    @Autowired
    TravelService travelService;

    @GetMapping("/indexTravel")
    public String indexTravel(Model model) {
        List<Travel> travelList = travelService.getAllTravel();
        model.addAttribute("travelList", travelList);
        return "index";
    }

    @RequestMapping("/updateEdit")
    public String getTravelById(Integer id, Model model) {
        Travel travel = travelService.getTravelById(id);
        model.addAttribute("travel", travel);
        return "updateTravel";
    }

    @PostMapping("/updateTravel")
    public String updateTravel(Travel travel) {
        travelService.updateTravel(travel);
        return "redirect:/indexTravel";
    }

    @RequestMapping("/addEdit")
    public String getTravelAdd() {
        return "addTravel";
    }

    @RequestMapping("/addTravel")
    public String addTravel(Travel travel) {
        travelService.addTravel(travel);
        return "redirect:/indexTravel";
    }

    @GetMapping("/deleteTravel")
    public String deleteTravel(Integer id) {
        travelService.deleteTravel(id);
        return "redirect:/indexTravel";
    }

    @RequestMapping(value = {"/travelByRequire"})
    public String getTravelByRequire(String item, String start_date, String stop_date,Model model) {
        List<Travel> travels = travelService.getTravelByRequire(item, start_date, stop_date);
        model.addAttribute("travelList", travels);
        return "index";
    }


    @RequestMapping(value = "/export")
    @ResponseBody
    public void export(HttpServletResponse response) throws IOException {
        List<Travel> travels = travelService.getAllTravel();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("获取excel测试表格");

        HSSFRow row = null;

        row = sheet.createRow(0);//创建第一个单元格
        row.setHeight((short) (26.25 * 20));
        row.createCell(0).setCellValue("出差信息列表");//为第一行单元格设值

        /*为标题设计空间
         * firstRow从第1行开始
         * lastRow从第0行结束
         *
         *从第1个单元格开始
         * 从第3个单元格结束
         */
        CellRangeAddress rowRegion = new CellRangeAddress(0, 0, 0, 2);
        sheet.addMergedRegion(rowRegion);

      /*CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
      sheet.addMergedRegion(columnRegion);*/


        /*
         * 动态获取数据库列 sql语句 select COLUMN_NAME from INFORMATION_SCHEMA.Columns where table_name='user' and table_schema='test'
         * 第一个table_name 表名字
         * 第二个table_name 数据库名称
         * */
        row = sheet.createRow(1);
        /*设置行高*/
        row.setHeight((short) (22.50 * 20));
        /*为第一个单元格设值*/
        row.createCell(0).setCellValue("出差ID");
        row.createCell(1).setCellValue("出差地点");
        row.createCell(2).setCellValue("项目类型");
        row.createCell(3).setCellValue("出差日期");
        row.createCell(4).setCellValue("结束日期");
        row.createCell(5).setCellValue("出差天数");
        row.createCell(6).setCellValue("出差人员");
        row.createCell(7).setCellValue("完成工作");
        row.createCell(8).setCellValue("问题总结");

        for (int i = 0; i < travels.size(); i++) {
            row = sheet.createRow(i + 2);
            Travel travel = travels.get(i);
            row.createCell(0).setCellValue(travel.getId());
            row.createCell(1).setCellValue(travel.getSpace());
            row.createCell(2).setCellValue(travel.getItem());
            row.createCell(3).setCellValue(travel.getStart_date());
            row.createCell(4).setCellValue(travel.getStop_date());
            row.createCell(5).setCellValue(travel.getTravel_days());
            row.createCell(6).setCellValue(travel.getTravel_person());
            row.createCell(7).setCellValue(travel.getFinish_work());
            row.createCell(8).setCellValue(travel.getProblem_sum());
        }
        sheet.setDefaultRowHeight((short) (16.5 * 20));
        //列宽自适应
        for (int i = 0; i <= 13; i++) {
            sheet.autoSizeColumn(i);
        }
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        response.setHeader("Content-disposition", "attachment;filename=user.xls");//默认Excel名称
        wb.write(os);
        os.flush();
        os.close();
    }

    @RequestMapping(value = "/import")
    public String exImport(@RequestParam(value = "filename") MultipartFile file, HttpSession session) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = travelService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:index";
    }


}
