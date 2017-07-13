package KaisaBid.chain;

import common.DataFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/24.
 */
public class StatusFilter extends DataFilter{
    private String status;
    public StatusFilter(String status){
        this.status = status;
    }
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> status.equals("12")||status.equals((String)map.get("status"))).collect(Collectors.toList());
    }
}
