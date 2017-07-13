package KaisaBid;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhouchuang on 2017/6/29.
 */
@Data
@NoArgsConstructor
public class KaisaBidHistory {
    String loanId = "";
    BigDecimal amount = new BigDecimal(0);
    BigDecimal interest = new BigDecimal(0);
    Integer term ;
    Date finishTime ;
    Date loanTime;
    Date openTime;
    Integer rate ;
    String status;
    String name;
    Date date;
    String type;
    Double timestamp;
    public void setType(String name){
        this.type = name.substring(0,4);
    }
    public void setTimestamp(Date ltime){
        int hour = ltime.getHours();
        int minutes =ltime.getMinutes();
        this.timestamp = hour+Math.floor(minutes/6.0)/10.0;
    }
    Long totalTime;

}
