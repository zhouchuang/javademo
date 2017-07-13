package mealallowance.model;

import Util.ExcelAssistant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Date;

/**
 * Created by zhouchuang on 2017/6/13.
 */
@Data
@NoArgsConstructor
public class WorkException {

    @ExcelAssistant(titleName = "部门",width = 5000)
    private String department;
    @ExcelAssistant(titleName = "姓名")
    private String username;
    @ExcelAssistant(titleName = "日期",dateformat = "YYYY/MM/DD",width = 3000)
    private Date date;
    public void date(Object object){
        date = org.apache.poi.ss.usermodel.DateUtil.parseYYYYMMDDDate((String)object);
    }
    @ExcelAssistant(titleName = "周期")
    private String day;
    @ExcelAssistant(titleName = "上班时间",width = 3000)
    private String workingtime;
    @ExcelAssistant(titleName = "上班情况",fontColor = IndexedColors.DARK_YELLOW,fontColorNotMatch="正常")
    private String workingstatus="正常";
    @ExcelAssistant(titleName = "下班时间",width = 3000)
    private String closingtime;
    @ExcelAssistant(titleName = "下班情况",fontColor = IndexedColors.DARK_YELLOW,fontColorNotMatch="正常")
    private String closingstatus="正常";
    @ExcelAssistant(titleName = "迟到次数",fontColor = IndexedColors.RED,fontColorNotMatch="正常")
    private String lateCount="正常";
    @ExcelAssistant(titleName = "异常情况",fontColor = IndexedColors.RED)
    private String exceptionSituation="正常";
}
