package KaisaBid;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by zhouchuang on 2017/6/6.
 */
public class ExeFilter extends FileFilter {
    public boolean accept(File f) {
        if (f.isDirectory())return true;
        return f.getName().endsWith(".exe");  //设置为选择以.class为后缀的文件
    }
    public String getDescription(){
        return ".exe";
    }
}
