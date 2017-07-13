package KaisaBid.chain;

import common.DataFilter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhouchuang on 2017/6/24.
 */
public class InvestAbleMoneyFilter extends DataFilter {
    private int minMoney;
    public InvestAbleMoneyFilter(int minMoney){
        this.minMoney = minMoney;
    }
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        return list.stream().filter(map -> minMoney<=((BigDecimal)map.get("money")).intValue()).collect(Collectors.toList());
    }
}

