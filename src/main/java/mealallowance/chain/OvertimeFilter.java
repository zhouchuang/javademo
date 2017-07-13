package mealallowance.chain;

import Util.DateUtil;
import common.DataFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/5.
 */
public class OvertimeFilter extends DataFilter {
    public String  overtime ="19:00:00";
    public OvertimeFilter(String overtime ){
        this.overtime = overtime;
    }
    @Override
    public List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> {
            Object obj = map.get("下班时间");
            if(obj!=null){
                String closingTime = (String)obj;
                if(DateUtil.isTimeFormat(closingTime))
                    return DateUtil.compare(closingTime,overtime);
            }
            return false;
        }).collect(Collectors.toList());
    }
}
