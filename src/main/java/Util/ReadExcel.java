package Util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.TempFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouchuang on 2017/6/5.
 */
public class ReadExcel {
    public static List<HashMap<String,Object>> readData(File file)throws Exception{
        int totalRows =0;
        int totalCells=0;
        List<HashMap<String,Object>> datalist = new ArrayList<HashMap<String,Object>>();
        Workbook rwb = createWorkboo(file);
        Sheet sheet=rwb.getSheetAt(0);//或者rwb.getSheet(0)
        // 得到Excel的行数
        totalRows = sheet.getPhysicalNumberOfRows();
        // 得到Excel的列数(前提是有行数)
        if (totalRows > 1 && sheet.getRow(0) != null) {
            totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        List<String> titleList  = new ArrayList<String>();
        if(totalRows>0){
            for(int k=0;k<totalCells;k++){
                titleList.add(sheet.getRow(0).getCell(k).getStringCellValue());
            }
        }else{
            throw new Exception("上传文件内容为空，请不要开玩笑");
        }

        for (int i = 1; i < totalRows; i++) {
            HashMap<String,Object> datamap =  new HashMap<String,Object>();
            boolean flag  = false;
            for (int j = 0; j < totalCells; j++) {
                Object obj =  null;
                Cell cell = sheet.getRow(i).getCell(j);
                try{
                    obj = getValueFromCell(cell);
                }catch(Exception e){
                    e.printStackTrace();
                    throw new Exception((cell!=null?cell.getCellType():"")+"第"+(i+1)+"行，第'"+titleList.get(j)+"'列，值为'"+(cell!=null?cell.getStringCellValue():" ")+"'，异常信息："+e.getMessage());
                }
                if(obj!=null)flag = true;
                datamap.put(titleList.get(j),obj);
            }
            if(flag)datalist.add(datamap);
        }
        return datalist;
    }
    public static Workbook createWorkboo(File file)throws  Exception{
        //读写xls和xlsx格式时，HSSFWorkbook针对xls，XSSFWorkbook针对xlsx
        InputStream inputStream = new FileInputStream(file);
        if(file.getName().endsWith(".xls")){
            return  new HSSFWorkbook(inputStream);
        }else{
            return new XSSFWorkbook(inputStream);
        }
    }
    public static List<HashMap<String,Object>> readData(String filePath)throws Exception{
        File file = new File(filePath);
        return  readData(file);
    }

    private static Object getValueFromCell(Cell cell){
        Object obj = null;
        if(cell !=null){
            switch (cell.getCellType())
            {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA:
                {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        // 如果是Date类型则，取得该Cell的Date值
                        obj = cell.getDateCellValue();
                    }
                    // 如果是纯数字
                    else
                    {
                        // 取得当前Cell的数值
                        HSSFDataFormatter dataFormatter = new HSSFDataFormatter();
                        String str = dataFormatter.formatCellValue(cell);
                        if(str.contains("%")){
                            str = str.replace("%","");
                            obj = new BigDecimal(str.replace(",","").trim()).divide(new BigDecimal(100),12,BigDecimal.ROUND_HALF_EVEN);
                        }else{
                            obj = BigDecimal.valueOf(Double.parseDouble(str.replace("*","").replace(",","").replace("¥","").trim()));
                        }
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    obj = cell.getStringCellValue().replaceAll("'", "''");
                    break;
                // 默认的Cell值
                default:
                    obj = null;
            }
        }else {
            obj =  null;
        }
        return obj;
    }
}
