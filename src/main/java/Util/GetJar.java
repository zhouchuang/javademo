package Util;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchuang on 2017/7/3.
 */
public class GetJar {
    public static void main(String[] args)throws Exception {
        String txtPath = "C:\\Users\\zhouchuang\\Desktop\\考勤餐补生成工具\\class.txt";
        List<String> list=  new ArrayList<>();
        Files.readAllLines(Paths.get(txtPath), Charset.forName("GBK")).forEach(s->{
            if(s.contains("考勤餐补生成工具")&& !list.contains(s.split("考勤餐补生成工具")[1])){
                list.add(s.split("考勤餐补生成工具")[1]);
            }
        });
        System.out.println(list);
    }
}
