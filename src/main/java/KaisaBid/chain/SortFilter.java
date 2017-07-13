package KaisaBid.chain;

import common.DataFilter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouchuang on 2017/6/26.
 */
public class SortFilter extends DataFilter {
    private String priority;
    private String[] priorityType=new String[]{"rate","term","term","money"};
    public SortFilter(String priority){
        this.priority = priority;
    }
    @Override
    protected List<HashMap<String, Object>> doFilter(List<HashMap<String, Object>> list, DataFilter dataFilter) {
        Collections.sort(list,new Comparator<HashMap<String,Object>>(){
            @Override
            public int compare(HashMap<String, Object> o1, HashMap<String, Object> o2) {
                int index = Integer.parseInt(priority.split(",")[0]);
                String type = priorityType[index-1];
                if(o2.get(type).toString().compareTo(o1.get(type).toString())==0){
                    if(priority.split(",").length>1){
                        index = Integer.parseInt(priority.split(",")[1]);
                        type = priorityType[index-1];
                        if(o2.get(type).toString().compareTo(o1.get(type).toString())==0){
                            if(priority.split(",").length>2){
                                index = Integer.parseInt(priority.split(",")[2]);
                                type = priorityType[index-1];
                                if(o2.get(type).toString().compareTo(o1.get(type).toString())==0){
                                    if(priority.split(",").length>3){
                                        index = Integer.parseInt(priority.split(",")[3]);
                                        type = priorityType[index-1];
                                        return compareTo(o1,o2,type,index==2);
                                    }else{
                                        return compareTo(o1,o2,type,index==2);
                                    }
                                }else{
                                    return compareTo(o1,o2,type,index==2);
                                }
                            }else{
                                return compareTo(o1,o2,type,index==2);
                            }
                        }else{
                            return compareTo(o1,o2,type,index==2);
                        }
                    }else{
                        return compareTo(o1,o2,type,index==2);
                    }
                }else{
                    return compareTo(o1,o2,type,index==2);
                }
            }
        });
        return list;
    }
    public int compareTo(HashMap<String, Object> o1 ,HashMap<String, Object> o2 ,String type ,boolean isre){
        if(isre){
            return o1.get(type).toString().compareTo(o2.get(type).toString());
        }else{
            return o2.get(type).toString().compareTo(o1.get(type).toString());
        }

    }
}
