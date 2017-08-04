package Database;

import java.math.BigDecimal;

/**
 * Created by zhouchuang on 2017/8/3.
 */
public class TestBigInt {
    public static void main(String[] args) {
        BigDecimal b = new BigDecimal(600000.000).multiply(new BigDecimal(3)).multiply(new BigDecimal(2.52)).divide(new BigDecimal(36500),10,BigDecimal.ROUND_HALF_UP);
        System.out.println(b);
    }
}
