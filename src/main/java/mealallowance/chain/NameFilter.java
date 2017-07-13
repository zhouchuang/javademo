package mealallowance.chain;

import common.DataFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/5.
 */
public class NameFilter extends DataFilter {
    private String username;
    public NameFilter(String username){
        this.username = username;
    }
    public  List<HashMap<String,Object>> doFilter(List<HashMap<String,Object>> list, DataFilter dataFilter){
        return list.stream().filter(map -> username.equals((String)map.get("姓名"))).collect(Collectors.toList());
    }
}
