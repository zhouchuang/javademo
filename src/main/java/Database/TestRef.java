package Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouchuang on 2017/8/1.
 */
public class TestRef {
    public static void main(String[] args) {
        List<String> list = null;
        list = test(list);
        System.out.println(list.size());
    }
    public static List<String> test(List list){
        list = getlist();
        return list;
    }
    public static List<String>  getlist(){
        List<String> list = new ArrayList<String>();
        list.add("a");
        list.add("b");
        return list;
    }
}
