package KaisaBid;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouchuang on 2017/6/23.
 */
public class TestSpider {
    private String url = "https://192.168.1.101/";
    private String charset = "utf-8";
    public static void main(String[] args)throws Exception {
        //String url="http://php.weather.sina.com.cn/iframe/index/w_cl.php";
       /* String url = "https://www.kaisafax.com/loan/getLoanList?&ajax=1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", "js");
        map.put("day", "0");
        map.put("city", "上海");
        map.put("dfc", "1");
        map.put("pageNum","1");
        map.put("pageSize","3");
        map.put("charset", "utf-8");
        String body = send(url, map,"utf-8");
        System.out.println("交易响应结果：");
        System.out.println(body);

        System.out.println("-----------------------------------");

        map.put("city", "北京");
        body = send(url, map, "utf-8");
        System.out.println("交易响应结果：");
        System.out.println(body);*/
        /*String url = "https://www.kaisafax.com/loan/getLoanList?&ajax=1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNum","1");
        map.put("pageSize","3");
        String body = send(url, map,"utf-8");
        parseData(body);*/

        int i = 1;
        while(getLoanData()){
            System.out.println("自动抓取数据开始:"+i++);
            Thread.sleep(30*1000);
        }

    }

    public static boolean getLoanData()throws  Exception{
        String url = "https://www.kaisafax.com/loan/getLoanList?&ajax=1";
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNum","1");
        map.put("pageSize","5");
        String body = send(url, map,"utf-8");
        return parseData(body);
    }
    private static String reg = ".*amount\":(\\d+).*days\":(\\d+).*investAmount\":(\\d+).*freezeAmount\":(\\d+).*rate\":(\\d+).*status\":(\\d+).*";
    private static int limit= 500;
    private static String status = "11";
    private static int minInvestMoney = 0;
    public static boolean parseData(String data){
       String loans = data.substring(data.indexOf("[{")+2,data.indexOf("}]"));
       for(String loan : loans.split("\\},\\{")){
           System.out.println(loan);
           Pattern p = Pattern.compile(reg);
           Matcher m = p.matcher(loan);
           while(m.find()){
               //借款状态，并且利率大于设定值
               BigDecimal amount = new BigDecimal(Double.parseDouble(m.group(1)));
               int days = Integer.parseInt(m.group(2));
               BigDecimal investAmount = new BigDecimal(Double.parseDouble(m.group(3)));
               BigDecimal freezeAmount = new BigDecimal(Double.parseDouble(m.group(4)));
               int rate = Integer.parseInt(m.group(5));
               String status = m.group(6);
               BigDecimal investAble = amount.subtract(investAmount).subtract(freezeAmount);

               if(status.equals(status)&&rate>=limit){
                   //判断剩余金额大于某个值则打开浏览器去投资

                   if(investAble.intValue()>=minInvestMoney){
                       openBrower();
                       System.out.println("有合适的标啦");
                       return false;
                   }
               }
           }
       }
       return true;
    }

    public static void openBrower(){
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                // 创建一个URI实例
                java.net.URI uri = java.net.URI.create("https://www.kaisafax.com/loan/");
                // 获取当前系统桌面扩展
                java.awt.Desktop dp = java.awt.Desktop.getDesktop() ;
                // 判断系统桌面是否支持要执行的功能
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    // 获取系统默认浏览器打开链接
                    dp.browse( uri ) ;
                }


            } catch (Exception e) {
                e.printStackTrace() ;
            }
        }
    }
    public static String send(String url, Map<String,String> map,String encoding) throws ParseException, IOException {
        String body = "";

        //创建httpclient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //创建post方式请求对象
        HttpPost httpPost = new HttpPost(url);

        //装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if(map!=null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        //设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

       /* System.out.println("请求地址："+url);
        System.out.println("请求参数："+nvps.toString());*/

        //设置header信息
        //指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //执行请求操作，并拿到结果（同步阻塞）
        CloseableHttpResponse response = client.execute(httpPost);
        //获取结果实体
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            //按指定编码转换结果实体为String类型
            body = EntityUtils.toString(entity, encoding);
        }
        EntityUtils.consume(entity);
        //释放链接
        response.close();
        return body;
    }

}
