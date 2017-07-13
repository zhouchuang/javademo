package Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by zhouchuang on 2017/4/27.
 */
public class ObjectUtil {
    public static Object fromMap(HashMap<String,Object> map , Class clazz) throws Exception{

        Object object = null;
        try {
            object    = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            ExcelAssistant excelAssistant = (ExcelAssistant)field.getAnnotation(ExcelAssistant.class);
            Object value = map.get(excelAssistant.titleName());
            try {
                if(value!=null){
                    try{
                        clazz.getDeclaredMethod(field.getName(),Object.class).invoke(object,value);
                    }catch(NoSuchMethodException e){
                        field.set(object,value);
                    }catch(InvocationTargetException e){
                        e.printStackTrace();
                    }
                }else{
                    if(field.getType()== BigDecimal.class){
                        field.set(object,new BigDecimal("0.0"));
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new Exception("反射出现错误，错误原因为："+value +"  字段名为："+field.getName());
            }
        };
        return object;
    }
}
