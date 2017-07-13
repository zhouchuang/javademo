package Util;

import KaisaBid.MyFrame;
import org.jfree.chart.ChartPanel;

import javax.swing.*;

/**
 * Created by zhouchuang on 2017/6/29.
 */
public class CreatePanelRunnable implements Runnable {

    private ChartPanelUtil chartPanelUtil;
    private JTabbedPane jTabbedPane;
    private ImageIcon imageIcon;

    public CreatePanelRunnable(JTabbedPane jTabbedPane ,ChartPanelUtil chartPanelUtil,ImageIcon imageIcon){
        this.jTabbedPane = jTabbedPane;
        this.chartPanelUtil = chartPanelUtil;
        this.imageIcon = imageIcon;
    }

    @Override
    public void run() {
        ChartPanel chartPanel0 = chartPanelUtil.getDailyChartPanel();
        jTabbedPane.addTab("放款金额",imageIcon,chartPanel0,"每日放款的历史数据统计");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChartPanel chartPanel1 = chartPanelUtil.getAmountConstitutePanel();
        jTabbedPane.addTab("金额组成",imageIcon,chartPanel1,"所有放款总额组成");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChartPanel chartPanel2 = chartPanelUtil.getLoanTimeMoneyPlotPanel();
        jTabbedPane.addTab("时间分布",imageIcon,chartPanel2,"开标时间分布和金额的关系");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChartPanel chartPanel3 = chartPanelUtil.getInterestConstitutePanel();
        jTabbedPane.addTab("利息组成",imageIcon,chartPanel3,"所有放款总利息组成");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChartPanel chartPanel4 = chartPanelUtil.getAmountInterestConstitutePanel();
        jTabbedPane.addTab("金额利息",imageIcon,chartPanel4,"利息金额对比");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ChartPanel chartPanel5 = chartPanelUtil.getAmountRateTermTimeBubblePanel();
        jTabbedPane.addTab("利率时间",imageIcon,chartPanel5,"利率金额对结标时间的影响");



    }
}
