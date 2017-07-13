package test;

import java.math.BigDecimal;

/**
 * Created by zhouchuang on 2017/6/23.
 */
public class TestBeg {
    public static void main(String[] args) {
        BigDecimal p1 = new BigDecimal(419948.20);
        BigDecimal p2 = new BigDecimal(419948.20);
        BigDecimal p = new BigDecimal(0);
        p  =  p.add(p1);
        p.add(p2);
        System.out.println(p);
    }
}
