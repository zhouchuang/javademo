package Util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouchuang on 2017/5/20.
 */
public class WriteExcelUtil {
    public static XSSFWorkbook createExcel(List list, String sheetName)throws Exception{


        XSSFWorkbook workbook =  new XSSFWorkbook() ;

        //时间类型
        XSSFCellStyle dateStyle = workbook.createCellStyle();
        XSSFDataFormat format= workbook.createDataFormat();
        dateStyle.setDataFormat(format.getFormat("yyyy-MM-dd"));

        Sheet sheet = workbook.createSheet(sheetName);
       /* XSSFCellStyle rightAlignStyle = getCellRightAlign(workbook);
        XSSFCellStyle rightAlignPrecision = getCellRightAlignPrecision(workbook);*/
        XSSFCellStyle totalStyle = getCellTotal(workbook);
        XSSFCellStyle centerStyle  = getCellCenterAlign(workbook);
        if(list.size()>0){
            setColumnWidth(sheet,list.get(0).getClass());
            String[] keys = getKeys(list.get(0).getClass());
            int rows = createTitle(sheet,list.get(0).getClass(),getTitleStyle(workbook));
            for(int i=0;i<list.size();i++){
                Object obj =  list.get(i);
                Row row = sheet.createRow(i+rows);
                for(int j=0;j<keys.length;j++){
                    String key = keys[j];
                    Field field = obj.getClass().getDeclaredField(key);
                    field.setAccessible(true);
                    Cell cell = row.createCell(j);
                    Object value  = field.get(obj);
                    if(value!=null){
                        ExcelAssistant  excelAssistant = field.getAnnotation(ExcelAssistant.class);
                        cell.setCellStyle(centerStyle);
                        if(value instanceof  String){
                            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(value.toString());
                        }else if(value instanceof Date){
                            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                            //cell.setCellStyle(dateStyle);
                            cell.setCellValue((Date)value);
                            String dateformat = "";

                            if(excelAssistant!=null){
                                dateformat = excelAssistant.dateformat();
                            }
                            if(StringUtils.isNotEmpty(dateformat)){
                                int year = ((Date) value).getYear()+1900;
                                int month = ((Date) value).getMonth()+1;
                                int days = ((Date) value).getDate();
                                cell.setCellValue(dateformat.replace("YYYY",year+"").replace("MM",month+"").replace("DD",days+""));
                            }else{
                                Calendar calendar  = Calendar.getInstance();
                                calendar.setTime((Date)value);
                                cell.setCellValue(calendar);
                            }

                        }else if(value instanceof BigDecimal){
                            int precision = 2;
                            boolean showPercentile = false;
                            if(excelAssistant!=null){
                                precision = excelAssistant.precision();
                                showPercentile = excelAssistant.showPercentile();
                            }
                            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                            if(showPercentile){
                                cell.setCellValue(((BigDecimal)value).doubleValue());
                                //cell.setCellStyle(centerStyle);
                            }else{
                                cell.setCellValue(((BigDecimal) value).setScale(precision,BigDecimal.ROUND_HALF_EVEN).toString());
                                //cell.setCellStyle(centerStyle);
                            }

                        }else if(value instanceof Integer ){
                            cell.setCellType(XSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue(Double.parseDouble(value.toString()));
                            //cell.setCellStyle(centerStyle);
                           /* IndexedColors indexedColors = null;
                            if(excelAssistant!=null){
                                indexedColors = excelAssistant.fontColor();
                            }
                            if(indexedColors.name()!=IndexedColors.BLACK.name()){
                                cell.setCellStyle(getCellColor(workbook,indexedColors));
                            }*/
                        }else {
                            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(value.toString());
                        }


                        IndexedColors indexedColors = null;
                        String fontColorNotMatch = "";
                        if(excelAssistant!=null){
                            indexedColors = excelAssistant.fontColor();
                            fontColorNotMatch = excelAssistant.fontColorNotMatch();
                        }
                        if(indexedColors.name()!=IndexedColors.BLACK.name()) {
                            if ("".equals(fontColorNotMatch) || !fontColorNotMatch.equals(value.toString()))
                                cell.setCellStyle(getCellColor(workbook, indexedColors));
                        }

                        if(i==list.size()-1){
                            ExcelAssistant excelAssistant1 = obj.getClass().getAnnotation(ExcelAssistant.class);
                            if(excelAssistant1!=null&&excelAssistant1.total())
                                cell.setCellStyle(totalStyle);
                        }
                    }else{
                        //cell.setCellValue("--");
                        cell.setCellValue("");
                    }
                }
            }
        }
        return workbook;
    }
    private static XSSFCellStyle getTitleStyle( XSSFWorkbook workbook ){
        XSSFFont font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 10);
        //设置字体
        font1.setFontName("微软雅黑");
        //设置加粗
        //font1.setBold(true);
        //设置字体颜色
        //font1.setColor(IndexedColors.BLACK.getIndex());
        //font1.setColor(HSSFColor.YELLOW.index);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font1);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        /*style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
*/



        return style;
    }

    public static String[]  getKeys(Class clazz){
        String keys = "";
        for(Field field : clazz.getDeclaredFields()){
            if(field.getAnnotation(ExcelAssistant.class)!=null){
                keys += field.getName()+",";
            }
        }
        return keys.substring(0,keys.length()-1).split(",");
    }
    public static int  createTitle(Sheet sheet,Class clazz,XSSFCellStyle style){
        if(isMerge(clazz)){
            int mergeCount  =  0;
            int lastConut =  0;
            CellRangeAddress region =null;
            String mergeTitle = "";
            Row row = sheet.createRow(0);
            row.setHeight((short) (400));
            for(Field field : clazz.getDeclaredFields()){
                if(field.getAnnotation(ExcelAssistant.class)!=null){
                    ExcelAssistant excelAssistant = (ExcelAssistant)field.getAnnotation(ExcelAssistant.class);
                    if(StringUtils.isNotEmpty(mergeTitle)&&!excelAssistant.mergeTitleName().equals(mergeTitle)){
                        if(mergeCount==1){
                            region = new CellRangeAddress(0,1, (short)lastConut, (short)(lastConut));
                        }else{
                            region = new CellRangeAddress(0, 0, (short)lastConut, (short) (lastConut+mergeCount-1));
                        }
                        sheet.addMergedRegion(region);
                        Cell cell = row.createCell(lastConut);
                        cell.setCellValue(mergeTitle);
                        cell.setCellStyle(style);
                        lastConut = lastConut+mergeCount;
                        mergeCount=0;
                    }
                    mergeCount++;
                    mergeTitle = excelAssistant.mergeTitleName();
                }
            }
            if(mergeCount==1){
                region = new CellRangeAddress(0,1, (short)lastConut, (short)(lastConut));
            }else{
                region = new CellRangeAddress(0, 0, (short)lastConut, (short) (lastConut+mergeCount-1));
            }
            sheet.addMergedRegion(region);
            Cell cell1 = row.createCell(lastConut);
            cell1.setCellValue(mergeTitle);
            cell1.setCellStyle(style);
            int i = 0;
            Row row1 = sheet.createRow(1);
            row.setHeight((short) (400));
            for(Field field : clazz.getDeclaredFields()){
                if(field.getAnnotation(ExcelAssistant.class)!=null){
                    ExcelAssistant excelAssistant = (ExcelAssistant)field.getAnnotation(ExcelAssistant.class);
                    Cell cell = row1.createCell(i++);
                    cell.setCellStyle(style);
                    if(!excelAssistant.titleName().equals(excelAssistant.mergeTitleName())){
                        cell.setCellValue(excelAssistant.titleName());
                    }else{
                        cell.setCellValue("");
                    }
                }
            }
            return 2;
        }else{
            int i = 0;
            Row row = sheet.createRow(0);
            row.setHeight((short) (400));
            for(Field field : clazz.getDeclaredFields()){
                if(field.getAnnotation(ExcelAssistant.class)!=null){
                    ExcelAssistant excelAssistant = (ExcelAssistant)field.getAnnotation(ExcelAssistant.class);
                    Cell cell = row.createCell(i++);
                    cell.setCellValue(excelAssistant.titleName());
                    cell.setCellStyle(style);
                }
            }
            return 1;
        }

    }
    private static  boolean isMerge(Class clazz){
        for(Field field : clazz.getDeclaredFields()){
            if(field.getAnnotation(ExcelAssistant.class)!=null){
                ExcelAssistant excelAssistant = (ExcelAssistant)field.getAnnotation(ExcelAssistant.class);
                if(StringUtils.isNotEmpty(excelAssistant.mergeTitleName()))return true;
            }
        }
        return false;
    }

    private static void setColumnWidth(Sheet sheet,Class clazz){
        int i =0;
        for(Field field : clazz.getDeclaredFields()){
            if(field.getAnnotation(ExcelAssistant.class)!=null){
                int width = ((ExcelAssistant)field.getAnnotation(ExcelAssistant.class)).width();
                sheet.setColumnWidth(i++, width);
            }
        }
    }

    public static XSSFCellStyle getCellCenterAlign(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        return style;
    }


    public static XSSFCellStyle getCellRightAlign(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return style;
    }

    public static XSSFCellStyle getCellRightAlignPrecision(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
        return style;
    }

    public static XSSFCellStyle getCellColor(XSSFWorkbook workbook,IndexedColors indexedColors){
        XSSFFont font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 10);
        font1.setColor(indexedColors.getIndex());
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font1);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        return style;
    }

    public static XSSFCellStyle getCellTotal(XSSFWorkbook workbook){
        XSSFFont font1 = workbook.createFont();
        font1.setFontHeightInPoints((short) 10);
        //设置字体
        font1.setFontName("微软雅黑");
        //设置加粗
        font1.setBold(true);
        //设置字体颜色
        font1.setColor(IndexedColors.BLACK.getIndex());
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font1);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());// 设置背景色
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        return  style;
    }

}
