package common;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouchuang on 2017/6/5.
 */
public abstract class  DataFilter {
    protected DataFilter dataFilter;
    public void setNextFilter(DataFilter dataFilter){
        this.dataFilter = dataFilter;
    }
    public final List<HashMap<String,Object>> filterHander(List<HashMap<String,Object>> list){
        List<HashMap<String,Object>> newlist = doFilter(list,dataFilter);
        if(dataFilter!=null){
            return dataFilter.filterHander(newlist);
        }else{
            return newlist;
        }
    }
    protected abstract  List<HashMap<String,Object>>  doFilter(List<HashMap<String,Object>> list, DataFilter dataFilter);
}
