package KaisaBid.chain;

import common.DataFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/24.
 */
public class RateFilter extends DataFilter {
    private Integer[] minRate;
    public RateFilter(Integer[] minRate){
        this.minRate = minRate;
    }
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> minRate[0]<=(int)map.get("rate")&&minRate[1]>=(int)map.get("rate")).collect(Collectors.toList());
    }
}
