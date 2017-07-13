package mealallowance.model;

import Util.ExcelAssistant;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.IndexedColors;

import java.util.Date;

/**
 * Created by zhouchuang on 2017/6/6.
 */
@Data
@NoArgsConstructor
@ExcelAssistant(total = true)
public class MealAllowance {

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
    @ExcelAssistant(titleName = "上班情况")
    private String workingstatus="正常";
    @ExcelAssistant(titleName = "下班时间",width = 3000)
    private String closingtime;
    @ExcelAssistant(titleName = "下班情况")
    private String closingstatus="正常";
    @ExcelAssistant(titleName = "是否加班",fontColor = IndexedColors.RED)
    private int isovertime=1;
    @ExcelAssistant(titleName = "费用",fontColor =  IndexedColors.RED)
    private int fee=20;
    @ExcelAssistant(titleName = "备注")
    private String mark;

}
