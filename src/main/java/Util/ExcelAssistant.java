package Util;

import org.apache.poi.ss.usermodel.IndexedColors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhouchuang on 2017/4/27.
 */

@Retention(RetentionPolicy.RUNTIME) // 注解会在class字节码文件中存在，在运行时可以通过反射获取到
@Target({ElementType.FIELD,ElementType.TYPE})//属性范围
public  @interface ExcelAssistant {
    String titleName() default "";  //主标题
    String mergeTitleName() default ""; //合并的标题
    boolean NonNull() default false; //不能为空
    int width() default  2000; //设置默认宽度为4000
    int precision() default 2; //默认精度为2
    boolean showPercentile() default false;//显示成百分比格式，默认为false
    String dateformat() default   "";
    IndexedColors fontColor() default IndexedColors.BLACK;
    String fontColorNotMatch() default "";
    boolean total() default false;//是否需要统计
}
