package mealallowance.chain;

import common.DataFilter;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/13.
 */
public class WorkExceptionFilter extends DataFilter {
    String keyfilter = "未打卡,迟到";
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> {
            Object sb = map.get("上班情况");
            Object xb = map.get("下班情况");
            if(sb!=null){
                String sbStr = (String)sb;
                if(StringUtils.isNotEmpty(sbStr)&&keyfilter.contains(sbStr)){
                    return true;
                }
            }
            if(xb!=null){
                String xbStr = (String)xb;
                if(StringUtils.isNotEmpty(xbStr)&&keyfilter.contains(xbStr)){
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
    }
}
