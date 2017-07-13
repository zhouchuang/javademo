package Util;

import KaisaBid.KaisaBidHistoryController;
import javafx.scene.chart.NumberAxis;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;

/**
 * Created by zhouchuang on 2017/6/29.
 */
public class ChartPanelUtil {
    public  ChartPanelUtil(KaisaBidHistoryController kaisaBidHistoryController){
        this.kaisaBidHistoryController = kaisaBidHistoryController;
        mChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 20));
        mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 15));
        mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        ChartFactory.setChartTheme(mChartTheme);

    }

    private KaisaBidHistoryController kaisaBidHistoryController;

    StandardChartTheme mChartTheme = new StandardChartTheme("CN");

    public ChartPanel getDailyChartPanel() {
        CategoryDataset mDataset = kaisaBidHistoryController.getDailyTurnoverChartData();
        JFreeChart mChart = ChartFactory.createBarChart3D(
                "每日放款总金额",//图名字
                "日期",//横坐标
                "成交额（万元）",//纵坐标
                mDataset,//数据集
                PlotOrientation.VERTICAL,
                true, // 显示图例
                true, // 采用标准生成器
                false);// 是否生成超链接
        CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
        mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
        mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
        mPlot.setOutlinePaint(Color.RED);//边界线
        return  new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }

    public ChartPanel getAmountConstitutePanel() {
        PieDataset mDataset = kaisaBidHistoryController.getAmountConstituteChartData();
        JFreeChart mChart = ChartFactory.createPieChart(
                "放款金额组成", // chart title
                mDataset,        // data
                true,           // include legend
                true,
                false);
        return new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }

    public ChartPanel getInterestConstitutePanel() {
        PieDataset mDataset = kaisaBidHistoryController.getInterestConstituteChartData();
        JFreeChart mChart = ChartFactory.createPieChart(
                "放款总利息组成", // chart title
                mDataset,        // data
                true,           // include legend
                true,
                false);
        return new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }

    public ChartPanel getAmountInterestConstitutePanel() {
        CategoryDataset mDataset = kaisaBidHistoryController.getAmountInterestConstitutePanel();
        JFreeChart mChart = ChartFactory.createBarChart3D(
                "本金利息对比",//图名字
                "类别",//横坐标
                "金额（万元）",//纵坐标
                mDataset,//数据集
                PlotOrientation.VERTICAL,
                true, // 显示图例
                true, // 采用标准生成器
                false);// 是否生成超链接
        CategoryPlot mPlot = (CategoryPlot)mChart.getPlot();
        mPlot.setBackgroundPaint(Color.LIGHT_GRAY);
        mPlot.setRangeGridlinePaint(Color.BLUE);//背景底部横虚线
        mPlot.setOutlinePaint(Color.RED);//边界线
        return  new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }


    public ChartPanel getLoanTimeMoneyPlotPanel(){
        XYDataset xydataset = kaisaBidHistoryController.getLoanTimeRefMoneyChartData();
        JFreeChart mChart = ChartFactory.createScatterPlot("开标时间金额分布图", "时间（时）","金额（万元）", xydataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }

    public ChartPanel getAmountRateTermTimeBubblePanel(){
        DefaultXYZDataset xydataset = kaisaBidHistoryController.getAmountRateTermTimeBubbleData();
        JFreeChart mChart = ChartFactory.createBubbleChart("利率与金额对结标所用时间的影响图", "利率（万分之）","金额（元）", xydataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }





}
