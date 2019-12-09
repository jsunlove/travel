package com.qingmu.travel.service.impl;

import com.qingmu.travel.mapper.TravelMapper;
import com.qingmu.travel.pojo.Travel;
import com.qingmu.travel.service.TravelService;
import com.qingmu.travel.util.MyException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class TravelServiceImpl implements TravelService {

    @Autowired
    private TravelMapper travelMapper;

    /*添加出差信息*/
    @Override
    public int addTravel(Travel travel) {
        return travelMapper.addTravel(travel);
    }

    /*删除出差信息*/
    @Override
    public int deleteTravel(Integer id) {
        return travelMapper.deleteTravel(id);
    }

    /*更新出差信息*/
    @Override
    public int updateTravel(Travel travel) {
        return travelMapper.updateTravel(travel);
    }

    /*通过id查找所有信息*/
    @Override
    public Travel getTravelById(Integer id) {
        return travelMapper.getTravelById(id);
    }

    /*查找所有信息*/
    @Override
    public List<Travel> getAllTravel() {
        return travelMapper.getAllTravel();
    }

    /*通过条件查找信息*/
    @Override
    public List<Travel> getTravelByRequire(String item, String start_date, String stop_date) {
        return travelMapper.getTravelByRequire(item,start_date,stop_date);
    }

    /*文件导入*/
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        List<Travel> travelList = new ArrayList<>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        Sheet sheet = wb.getSheetAt(0);
        if(sheet!=null){
            notNull = true;
        }
        Travel travel;
        //r = 2 表示从第三行开始循环 如果你的第三行开始是数据
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {
            //通过sheet表单对象得到 行对象
            HSSFRow row = (HSSFRow) sheet.getRow(r);
            if (row == null){
                continue;
            }
            //sheet.getLastRowNum() 的值是 10，所以Excel表中的数据至少是10条；不然报错 NullPointerException
            travel = new Travel();

            if( row.getCell(0).getCellType() !=1){
                //循环时，得到每一行的单元格进行判断
                throw new MyException("导入失败(第"+(r+1)+"行,用户名请设为文本格式)");
            }
            String username = row.getCell(0).getStringCellValue();
            //得到每一行第一个单元格的值
            if(username == null || username.isEmpty()){
                //判断是否为空
                throw new MyException("导入失败(第"+(r+1)+"行,用户名未填写)");
            }
            row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
            //得到每一行的 第二个单元格的值
            String password = row.getCell(1).getStringCellValue();
            if(password==null || password.isEmpty()){
                throw new MyException("导入失败(第"+(r+1)+"行,密码未填写)");
            }



            //完整的循环一次 就组成了一个对象
            travel.setSpace(username);
            travel.setItem(password);


            travelList.add(travel);
        }
//        for (Travel travelResord : travelList) {
//            String name = travelResord.getUsername();
//            int cnt = travelMapper.selectByName(name);
//            if (cnt == 0) {
//                travelMapper.addUser(travelResord);
//                System.out.println(" 插入 "+travelResord);
//            } else {
//                travelMapper.updateUserByName(travelResord);
//                System.out.println(" 更新 "+travelResord);
//            }
//        }
        return notNull;
    }
}
