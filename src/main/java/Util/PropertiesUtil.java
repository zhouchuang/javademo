package Util;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by zhouchuang on 2017/6/28.
 */
public class PropertiesUtil {
    private   static  String param1;
    private   static  String param2;
    private   static Properties prop =null;
    static  {
        File file = new File("C:"+File.separator+"Users"+File.separator+System.getenv().get("USERNAME")+File.separator+".tools");
        File profile= new File("C:"+File.separator+"Users"+File.separator+System.getenv().get("USERNAME")+File.separator+".tools"+File.separator+"kaisa.properties");
        if(!file.exists()){
            file.mkdir();
        }
        if(!profile.exists()){
            try {
                profile.createNewFile();
                InputStream in = Object. class .getResourceAsStream( "/kaisa.properties" );
                OutputStream os = new FileOutputStream(profile);
                //文件拷贝
                byte flush[]  = new byte[1024];
                int len = 0;
                while(0<=(len=in.read(flush))){
                    os.write(flush, 0, len);
                }
                //关闭流的注意 先打开的后关
                os.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        prop =  new  Properties();
        try  {
            //InputStream in = Object. class .getResourceAsStream( "/kaisa.properties" );
            InputStream in = new FileInputStream(profile);
            prop.load(in);
            in.close();
        }  catch  (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getValue(String key){
        return  prop.getProperty( key ).trim();
    }
    public static void setValue(String key,String value){
        try {
            prop.setProperty(key,value);
            FileOutputStream outputFile = new FileOutputStream("C:"+File.separator+"Users"+File.separator+System.getenv().get("USERNAME")+File.separator+".tools"+File.separator+"kaisa.properties");
            prop.store(outputFile, "modify");
            outputFile.flush();
            outputFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Object copyProertiesToObject(Object obj){
        Field[] fileds = obj.getClass().getDeclaredFields();
        try {
            for(Field field : fileds){
                field.setAccessible(true);
                if(field.getType()==Integer.class){
                    field.set(obj,Integer.parseInt(getValue(field.getName())));
                }else if(field.getType()==Boolean.class){
                    field.set(obj,field.getName().equals("true")?true:false);
                }else if(field.getType().isArray()){
                    if(field.getType().getName().contains("Integer")){
                        String value = getValue(field.getName());
                        Integer[] vals  = new Integer[value.split(",").length];
                        for(int i=0;i<vals.length;i++){
                            vals[i] = Integer.parseInt(value.split(",")[i]);
                        }
                        field.set(obj,vals);
                    }else{

                    }
                }else{
                    field.set(obj,getValue(field.getName()));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
