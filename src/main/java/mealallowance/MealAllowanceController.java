package mealallowance;

import Util.ObjectUtil;
import Util.ReadExcel;
import Util.WriteExcelUtil;
import mealallowance.chain.NameFilter;
import mealallowance.chain.OvertimeFilter;
import mealallowance.chain.WorkExceptionFilter;
import mealallowance.model.MealAllowance;
import mealallowance.model.WorkException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/6.
 */
public class MealAllowanceController {


    public HashSet<String> getUsename(String filepath){
        List<HashMap<String,Object>> list = null;
        HashSet<String> usernames = new HashSet<String>();
        try {
            list = ReadExcel.readData(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.stream().forEach(map->{
            usernames.add((String)map.get("姓名"));
        });
        return usernames;
    }
    public void generalAllowanceExcel(String username,String filepath){
        List<HashMap<String,Object>> list = null;
        try {
            list = ReadExcel.readData(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NameFilter nameFilter = new NameFilter(username);
        OvertimeFilter overtimeFilter = new OvertimeFilter("19:00:00");
        nameFilter.setNextFilter(overtimeFilter);
        List<HashMap<String,Object>> newlist = nameFilter.filterHander(list);
        List<MealAllowance> mealAllowances = new ArrayList<MealAllowance>();
        newlist.stream().forEach(map->{
            try {
                MealAllowance mealAllowance = (MealAllowance) ObjectUtil.fromMap(map,MealAllowance.class);
                mealAllowances.add(mealAllowance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        MealAllowance total = new MealAllowance();
        total.setClosingstatus("合计");
        total.setFee(mealAllowances.size()*20);
        total.setIsovertime(mealAllowances.size());
        total.setWorkingstatus(null);
        mealAllowances.add(total);
        generalExcel(mealAllowances,"加班餐费统计（"+username+"）");
    }

    public void generalExceptionExcel(String username,String filepath){
        List<HashMap<String,Object>> list = null;
        try {
            list = ReadExcel.readData(filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        NameFilter nameFilter = new NameFilter(username);
        WorkExceptionFilter workExceptionFilter = new WorkExceptionFilter();
        nameFilter.setNextFilter(workExceptionFilter);
        List<HashMap<String,Object>> newlist = nameFilter.filterHander(list);
        List<WorkException> mealAllowances = new ArrayList<WorkException>();
        newlist.stream().forEach(map->{
            try {
                WorkException mealAllowance = (WorkException) ObjectUtil.fromMap(map,WorkException.class);
                mealAllowances.add(mealAllowance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        generalExcel(mealAllowances,"考勤异常统计（"+username+"）");
    }

    public void generalExcel( List mealAllowances,String fileName){
        try {
            //String fileName = "加班餐费统计（"+username+"）";

            String desktoppath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
            OutputStream out  = new FileOutputStream(desktoppath+ File.separator+fileName+".xlsx");
            XSSFWorkbook workbook = WriteExcelUtil.createExcel(mealAllowances,fileName);
            workbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
