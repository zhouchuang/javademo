package KaisaBid.chain;

import common.DataFilter;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/24.
 */
public class TermFilter extends DataFilter {
    private Integer[] term;
    public TermFilter(Integer[] term){
        this.term = term;
    }
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> term[0]<=(int)map.get("term")&&term[1]>=(int)map.get("term")).collect(Collectors.toList());
    }
}
