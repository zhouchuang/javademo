package Util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhouchuang on 2017/6/5.
 */
public class DateUtil {
    public static boolean compare(String realTime,String compTime){
        int realtime  = Integer.parseInt(realTime.split(":")[0])*3600+Integer.parseInt(realTime.split(":")[1])*60+Integer.parseInt(realTime.split(":")[2]);
        int comptime  = Integer.parseInt(compTime.split(":")[0])*3600+Integer.parseInt(compTime.split(":")[1])*60+Integer.parseInt(compTime.split(":")[2]);
        return realtime>=comptime;
    }

    public static boolean isTimeFormat(String time){
        return StringUtils.isNotEmpty(time)&&time.matches("[0-9]{2}:[0-9]{2}:[0-9]{2}");
    }
}
