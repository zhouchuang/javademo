package KaisaBid;

import KaisaBid.chain.*;
import Util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import mealallowance.chain.NameFilter;
import mealallowance.chain.OvertimeFilter;
import mealallowance.chain.WorkExceptionFilter;
import mealallowance.model.MealAllowance;
import mealallowance.model.WorkException;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.poi.util.ArrayUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouchuang on 2017/6/6.
 */
@Data
@NoArgsConstructor
public class KaisaBidController extends Thread {





    KaisaBid kaisaBid = null;
    private boolean openBrower =true;
    private int count;
    private boolean scanAble=true;
    private JButton listener;
    private JButton stopListener;
    private JLabel msg;
    private JTable  table;
    private boolean isStart=false;
    Vector<String> title = null;
    Vector<Vector<Object>> objs = null;

    private static String reg = ".*id\":\"(\\d+)\".*amount\":(\\d+).*days\":(\\d+).*months\":(\\d+).*investAmount\":(\\d+).*freezeAmount\":(\\d+).*rate\":(\\d+).*status\":(\\d+).*title\":\"([^\"]+)\".*";
    //private String  priorityIndexs = "1";
    public void startScan()throws Exception{
        if(isStart){
            scanAble = true;
            scan();
        }else{
            start();
            isStart = true;
        }
    }
    private void scan(){
        if(scanAble){
            count++;
            msg.setText("第"+count+"次扫描");
            if(!getLoanData()){
                stopListener.doClick();
            }
        }
    }
    public void run(){
        scanAble = true;
        while(true){
            scan();
            try {
                Thread.sleep(kaisaBid.getScanInteval()*1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public void stopScan()throws Exception{
        scanAble=false;
        listener.setEnabled(true);
    }

    public  boolean getLoanData(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("pageNum","1");
        map.put("pageSize",kaisaBid.getPageSize()+"");
        String body = null;
        try {
            body = HttpClientUtil.send(kaisaBid.getScanUrl(), map,"utf-8");
        } catch (IOException e) {
            return false;
        }
        return parseData(body);
    }

    public  boolean parseData(String data){
        String loans = data.substring(data.indexOf("[{")+2,data.indexOf("}]"));
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        for(String loan : loans.split("\\},\\{")){

            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(loan);
            while(m.find()){
                //借款状态，并且利率大于设定值
                String loanId = m.group(1);
                BigDecimal amount = new BigDecimal(Double.parseDouble(m.group(2)));
                int days = Integer.parseInt(m.group(3));
                int month = Integer.parseInt(m.group(4));
                BigDecimal investAmount = new BigDecimal(Double.parseDouble(m.group(5)));
                BigDecimal freezeAmount = new BigDecimal(Double.parseDouble(m.group(6)));
                int rate = Integer.parseInt(m.group(7));
                String status = m.group(8);
                BigDecimal investAble = amount.subtract(investAmount).subtract(freezeAmount);
                String name = m.group(9);

                HashMap<String,Object> map = new HashMap<String,Object>();
                map.put("loanId",loanId);
                map.put("term",days==0?month*30:days);
                map.put("rate",rate);
                map.put("status",status);
                map.put("money",investAble);
                map.put("name",name);
                list.add(map);
            }
        }
        Vector<Vector<Object>> objs = new Vector<Vector<Object>>(list.size());
        for(int i=0;i<list.size() ;i++){
            HashMap<String,Object> map  = list.get(i);
            Vector<Object> sub = new Vector<Object>(map.size()+1);
            //"标名", "期限", "收益率", "可投金额", "状态"
            sub.addElement(map.get("name"));
            sub.addElement(map.get("term"));
            sub.addElement((int)map.get("rate")/100.0+"%");
            sub.addElement(map.get("money"));
            sub.addElement(getChinese(map.get("status").toString()));
            objs.addElement(sub);
        }
        DefaultTableModel model=new DefaultTableModel(objs,title);
        table.setModel(model);
        table.validate();
        /*table.repaint();
        table.updateUI();*/
        StatusFilter statusFilter = new StatusFilter(this.kaisaBid.getStatus());
        RateFilter rateFilter = new RateFilter(this.kaisaBid.getRate());
        TermFilter termFilter = new TermFilter(this.kaisaBid.getTerm());
        InvestAbleMoneyFilter investAbleMoneyFilter = new InvestAbleMoneyFilter(this.kaisaBid.getMoney());
        SortFilter sortFilter = new SortFilter(this.kaisaBid.getPriorityIndexs());
        statusFilter.setNextFilter(rateFilter);
        rateFilter.setNextFilter(termFilter);
        termFilter.setNextFilter(investAbleMoneyFilter);
        investAbleMoneyFilter.setNextFilter(sortFilter);
        List<HashMap<String,Object>> newlist = statusFilter.filterHander(list);
        if(newlist.size()>0){
            openBrower(newlist.get(0).get("loanId").toString());
            return false;
        }else{
            return true;
        }
    }

    public  String getChinese(String status){
        String type = "筹款中,已流标,已满标,放款中,还款中";
        return type.split(",")[Integer.parseInt(status)-7];
    }
    /*public static String send(String url, Map<String,String> map,String encoding) throws ParseException, IOException {
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
    }*/

    public  void openBrower(String id){
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                // 创建一个URI实例
                java.net.URI uri =null;
                if(this.kaisaBid.getAutoJumpPage().equals("loanDetail")){
                    uri = java.net.URI.create(this.kaisaBid.getLoanDetail()+id);
                }else{
                    uri = java.net.URI.create(this.kaisaBid.getLoanList());
                }
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
}
