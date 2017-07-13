package KaisaBid;

import Util.HttpClientUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouchuang on 2017/6/29.
 */
@Data
@NoArgsConstructor
public class KaisaBidHistoryController {
    KaisaBid kaisaBid = null;
    private List<KaisaBidHistory> data = null;



    public CategoryDataset getDailyTurnoverChartData(){
        getHistoryLoanData();
        List<KaisaBidHistory> dailylist = new ArrayList<KaisaBidHistory>();
        KaisaBidHistory daily = null;
        for(KaisaBidHistory kaisaBidHistory : data){
            if(daily==null||!kaisaBidHistory.getDate().equals(daily.getDate())){
                daily = new KaisaBidHistory();
                daily.setDate(kaisaBidHistory.getDate());
                daily.setAmount(kaisaBidHistory.getAmount());
                dailylist.add(daily);
            }else{
                daily.setAmount(daily.getAmount().add(kaisaBidHistory.getAmount()));
            }
        }
        Collections.reverse(dailylist);
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        for(KaisaBidHistory kaisaBidHistory : dailylist){
            mDataset.addValue(kaisaBidHistory.getAmount().doubleValue()/10000,"日投标总金额", DateUtils.formatDate(kaisaBidHistory.getDate(),"MM-dd"));
        }
        return mDataset;
    }

    public XYDataset getLoanTimeRefMoneyChartData(){
        DefaultXYDataset  dataset = new DefaultXYDataset();
        double[][] data = new double[2][this.data.size()];
        for(int i=0;i<this.data.size(); i++){
            KaisaBidHistory kaisaBidHistory = this.data.get(i);
            data[0][i] = kaisaBidHistory.getTimestamp();
            data[1][i] = kaisaBidHistory.getAmount().doubleValue()/10000.0;
        }
        dataset.addSeries("开标点", data);
        return  dataset;
    }
    public  PieDataset getAmountConstituteChartData( )
    {
        List<KaisaBidHistory> constitueList = new ArrayList<KaisaBidHistory>();
        KaisaBidHistory costitue = null;
        for(KaisaBidHistory kaisaBidHistory : data){
            if(costitue==null||!kaisaBidHistory.getType().equals(costitue.getType())){
                costitue = new KaisaBidHistory();
                costitue.setType(kaisaBidHistory.getType());
                costitue.setAmount(kaisaBidHistory.getAmount());
                constitueList.add(costitue);
            }else{
                costitue.setAmount(costitue.getAmount().add(kaisaBidHistory.getAmount()));
            }
        }
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for(KaisaBidHistory consitue : constitueList){
            dataset.setValue(consitue.getType(),consitue.getAmount());
        }
        return dataset;
    }
    public  PieDataset getInterestConstituteChartData( )
    {
        List<KaisaBidHistory> constitueList = new ArrayList<KaisaBidHistory>();
        KaisaBidHistory costitue = null;
        for(KaisaBidHistory kaisaBidHistory : data){
            if(costitue==null||!kaisaBidHistory.getType().equals(costitue.getType())){
                costitue = new KaisaBidHistory();
                costitue.setType(kaisaBidHistory.getType());
                costitue.setInterest(new BigDecimal(kaisaBidHistory.getAmount().doubleValue()*kaisaBidHistory.getRate()/3600000*kaisaBidHistory.getTerm()));
                constitueList.add(costitue);
            }else{
                costitue.setInterest(costitue.getInterest().add(new BigDecimal(kaisaBidHistory.getAmount().doubleValue()*kaisaBidHistory.getRate()/3600000*kaisaBidHistory.getTerm())));
            }
        }
        DefaultPieDataset dataset = new DefaultPieDataset( );
        for(KaisaBidHistory consitue : constitueList){
            dataset.setValue(consitue.getType(),consitue.getInterest());
        }
        return dataset;
    }


    public  CategoryDataset getAmountInterestConstitutePanel( )
    {
        List<KaisaBidHistory> constitueList = new ArrayList<KaisaBidHistory>();
        KaisaBidHistory costitue = null;
        for(KaisaBidHistory kaisaBidHistory : data){
            if(costitue==null||!kaisaBidHistory.getType().equals(costitue.getType())){
                costitue = new KaisaBidHistory();
                costitue.setType(kaisaBidHistory.getType());
                costitue.setInterest(new BigDecimal(kaisaBidHistory.getAmount().doubleValue()*kaisaBidHistory.getRate()/3600000*kaisaBidHistory.getTerm()));
                costitue.setAmount(kaisaBidHistory.getAmount());
                constitueList.add(costitue);
            }else{
                costitue.setInterest(costitue.getInterest().add(new BigDecimal(kaisaBidHistory.getAmount().doubleValue()*kaisaBidHistory.getRate()/3600000*kaisaBidHistory.getTerm())));
                costitue.setAmount(costitue.getAmount().add(kaisaBidHistory.getAmount()));
            }
        }
        DefaultCategoryDataset mDataset = new DefaultCategoryDataset();
        for(KaisaBidHistory kaisaBidHistory : constitueList){
            mDataset.addValue(kaisaBidHistory.getAmount().doubleValue()/10000,"本金",kaisaBidHistory.getType());
            mDataset.addValue(kaisaBidHistory.getInterest().doubleValue()/10000,"利息",kaisaBidHistory.getType());
        }
        return mDataset;
    }

    public DefaultXYZDataset getAmountRateTermTimeBubbleData(){

        List<KaisaBidHistory> constitueList = new ArrayList<KaisaBidHistory>();
        for(KaisaBidHistory kaisaBidHistory : data){
            if(kaisaBidHistory.getFinishTime()==null)continue;
            boolean flag = false;
            for(KaisaBidHistory cositue : constitueList){
                if(cositue.getRate().intValue()==kaisaBidHistory.getRate().intValue()){
                    flag  = true;
                    cositue.setAmount(cositue.getAmount().add(kaisaBidHistory.getAmount()));
                    cositue.setTotalTime(cositue.getTotalTime()+kaisaBidHistory.getFinishTime().getTime()-kaisaBidHistory.getOpenTime().getTime());
                    break;
                }
           }
           if(flag==false){
                KaisaBidHistory kbh = new KaisaBidHistory();
                kbh.setRate(kaisaBidHistory.getRate());
                kbh.setAmount(kaisaBidHistory.getAmount());
                kbh.setTotalTime(kaisaBidHistory.getFinishTime().getTime()-kaisaBidHistory.getOpenTime().getTime());
                constitueList.add(kbh);
           }
        }
        DefaultXYZDataset defaultxyzdataset = new DefaultXYZDataset( );
        double p1[] = new double[constitueList.size()];
        double p2[] = new double[constitueList.size()];
        double p3[] = new double[constitueList.size()];
        for(int i=0;i<constitueList.size(); i++){
            KaisaBidHistory kaisaBidHistory = constitueList.get(i);
            p1[i] = kaisaBidHistory.getRate();
            p2[i] = kaisaBidHistory.getAmount().doubleValue();
            p3[i] = kaisaBidHistory.getTotalTime();
        }
        double p[][]  = {p1,p2,p3};
        defaultxyzdataset.addSeries( "同利率合集" , p );
        return defaultxyzdataset;
    }
    private void getHistoryLoanData(){
        if(data==null){
            Map<String, String> map = new HashMap<String, String>();
            map.put("pageSize","20");
            data = new ArrayList<KaisaBidHistory>();
            for(int i=1;i<=5;i++){
                map.put("pageNum",i+"");
                String body = null;
                try {
                    body = HttpClientUtil.send(kaisaBid.getScanUrl(), map,"utf-8");
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                parseHistoryData(body,data);
            }
        }
    }

    SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    private static String historyData = ".*id\":\"(\\d+)\".*amount\":(\\d+).*days\":(\\d+).*months\":(\\d+).*finishTime\":\"([^\"]+)\".*loanTime\":\"([^\"]+)\".*openTime\":\"([^\"]+)\".*rate\":(\\d+).*status\":(\\d+).*title\":\"([^\"]+)\".*";
    public  List<KaisaBidHistory> parseHistoryData(String data,List<KaisaBidHistory> list) {
        String loans = data.substring(data.indexOf("[{") + 2, data.indexOf("}]"));
        for (String loan : loans.split("\\},\\{")) {
            Pattern p = Pattern.compile(historyData);
            Matcher m = p.matcher(loan);
            while (m.find()) {
                //借款状态，并且利率大于设定值
                String loanId = m.group(1);
                BigDecimal amount = new BigDecimal(Double.parseDouble(m.group(2)));
                int days = Integer.parseInt(m.group(3));
                int month = Integer.parseInt(m.group(4));
                String finishTime = m.group(5);
                String loanTime = m.group(6);
                String openTime = m.group(7);
                int rate = Integer.parseInt(m.group(8));
                String status = m.group(9);
                String name = m.group(10);
                KaisaBidHistory kaisaBidHistory = new KaisaBidHistory();
                kaisaBidHistory.setLoanId(loanId);
                kaisaBidHistory.setAmount(amount);
                kaisaBidHistory.setTerm(days==0?month*30:days);
                kaisaBidHistory.setRate(rate);
                kaisaBidHistory.setStatus(status);
                kaisaBidHistory.setName(name);
                kaisaBidHistory.setType(name);
                kaisaBidHistory.setDate(DateUtil.parseYYYYMMDDDate(loanTime.substring(0,10)));
                try {
                    kaisaBidHistory.setFinishTime(sdf.parse(finishTime));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                try {
                    kaisaBidHistory.setLoanTime(sdf.parse(loanTime));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                try {
                    kaisaBidHistory.setOpenTime(sdf.parse(openTime));
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                kaisaBidHistory.setTimestamp(kaisaBidHistory.getOpenTime());
                list.add(kaisaBidHistory);
            }
        }
        return list;

    }
}
