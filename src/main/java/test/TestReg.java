package test;

/**
 * Created by zhouchuang on 2017/6/15.
 */
public class TestReg {
    public static void main(String[] args) {
        String str = "getM(proofDate)";
        System.out.println(str.matches("[^\\(\\)]+\\([^\\(\\)]+\\)"));
    }
}
