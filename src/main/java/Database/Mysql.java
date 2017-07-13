package Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhouchuang on 2017/6/23.
 */
public class Mysql {

    public static String  TABLES = "select TABLE_NAME from information_schema.tables where table_schema='${database}' and table_type='base table'";
    public static String  COLUMNS = "select COLUMN_NAME,COLUMN_COMMENT from INFORMATION_SCHEMA.Columns where table_name='${table}' and table_schema='${database}'";
    public static String  INSERT = "insert into kaisa(name,node,tablename,databasename)values('${name}','${node}','${tablename}','${database}')";
    public static void main(String[] args) {
        String[] databases = new String[]{"kaisa_fax_sys_migrate", "kaisa_fax_user_migrate","kaisa_fax_trade_migrate","kaisa_fax_act_migrate","kaisa_fax_api_migrate","kaisa_fax_log_migrate","kaisa_fax_intervalmq","kaisa_fax_sms"};
        for (String dataBase : databases) {
            List<String> tables = getDataBase(dataBase);
            for (String table : tables) {
                List<Map<String, String>> list = getTableInfo(dataBase, table);
                for (Map<String, String> map : list) {
                    insert(map.get("name"), map.get("node"), table, dataBase);
                }
            }
        }
    }
    public static void insert(String name,String node,String tablename,String database){
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://192.168.188.154:3306/kams_dev?characterEncoding=utf8&useSSL=true";
        //MySQL配置时的用户名
        String user = "kams_dev";
        //MySQL配置时的密码
        String password = "Pes123456";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = INSERT.replace("${name}",name).replace("${node}",node).replace("${tablename}",tablename).replace("${database}",database);
            //3.ResultSet类，用来存放获取的结果集！！
            statement.execute(sql);
            statement.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            //System.out.println("数据库数据成功获取！！");
            System.out.println(name+"\t"+node);
        }
    }
    public static List<String> getDataBase(String database){
        List<String> list = new ArrayList<String>();
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://192.168.188.50:3306/kaisa_fax_sys_migrate?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
        //MySQL配置时的用户名
        String user = "sxfax";
        //MySQL配置时的密码
        String password = "sxfax123456";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = TABLES.replace("${database}",database);
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            String name = null;
            while(rs.next()){
                //获取stuname这列数据
                name = rs.getString("TABLE_NAME");
                list.add(name);
            }
            rs.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            //System.out.println("数据库数据成功获取！！");
        }
        return list;
    }
    public static List<Map<String,String>> getTableInfo(String database , String table) {
        List<Map<String,String>> list  = new ArrayList<Map<String,String>>();
        //声明Connection对象
        Connection con;
        //驱动程序名
        String driver = "com.mysql.jdbc.Driver";
        //URL指向要访问的数据库名mydata
        String url = "jdbc:mysql://192.168.188.50:3306/kaisa_fax_sys_migrate?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&transformedBitIsBoolean=true";
        //MySQL配置时的用户名
        String user = "sxfax";
        //MySQL配置时的密码
        String password = "sxfax123456";
        //遍历查询结果集
        try {
            //加载驱动程序
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            //2.创建statement类对象，用来执行SQL语句！！
            Statement statement = con.createStatement();
            //要执行的SQL语句
            String sql = COLUMNS.replace("${database}",database).replace("${table}",table);
            //3.ResultSet类，用来存放获取的结果集！！
            ResultSet rs = statement.executeQuery(sql);
            String name = null;
            String node = null;
            while(rs.next()){
                //获取stuname这列数据
                name = rs.getString("COLUMN_NAME");
                //获取stuid这列数据
                node = rs.getString("COLUMN_COMMENT");
                //首先使用ISO-8859-1字符集将name解码为字节序列并将结果存储新的字节数组中。
                //然后使用GB2312字符集解码指定的字节数组。
                name = new String(name.getBytes("ISO-8859-1"),"gb2312");

                Map<String,String> map = new HashMap<String,String>();
                map.put("name",name);
                map.put("node",node);
                list.add(map);
            }
            rs.close();
            con.close();
        } catch(ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            //System.out.println("数据库数据成功获取！！");
        }
        return list;
    }
}
