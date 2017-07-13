package KaisaBid;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.util.Vector;

/**
 * Created by zhouchuang on 2017/6/28.
 */
@Data
@NoArgsConstructor
public class KaisaBid {
    private Integer[] rate;
    private Integer[] term;
    private Integer money=100;
    private String status="7";
    private Integer scanInteval=30;
    private Integer pageSize=5;
    private String scanUrl="https://www.kaisafax.com/loan/getLoanList?&ajax=1";
    private String loanDetail="https://www.kaisafax.com/loan/loanDetail?loanId=";
    private String loanList="https://www.kaisafax.com/loan";
    private String autoJumpPage="loanDetail";
    private Boolean openBrower =true;
    private String  priorityIndexs = "1";
}
