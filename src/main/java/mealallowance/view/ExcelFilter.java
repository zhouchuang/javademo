package mealallowance.view;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by zhouchuang on 2017/6/6.
 */
public class ExcelFilter extends FileFilter {
    public boolean accept(java.io.File f) {
        if (f.isDirectory())return true;
        return f.getName().endsWith(".xls");  //设置为选择以.class为后缀的文件
    }
    public String getDescription(){
        return ".xls";
    }
}
