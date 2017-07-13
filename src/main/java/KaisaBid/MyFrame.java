package KaisaBid;



import Util.ChartPanelUtil;
import Util.ChineseUtil;
import Util.CreatePanelRunnable;
import Util.PropertiesUtil;
import mealallowance.MealAllowanceController;
import mealallowance.view.ExcelFilter;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jfree.chart.*;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by zhouchuang on 2017/6/6.
 */
public class MyFrame implements ActionListener {
    KaisaBidController kaisaBidController = new KaisaBidController();
    KaisaBidHistoryController kaisaBidHistoryController = new KaisaBidHistoryController();
    JFrame frame = new JFrame("佳兆业金服投标助手");// 框架布局
    Container con = new Container();


    JMenu config=new JMenu("配置") ;     //创建JMenu菜单对象
    JMenuItem scanconfig=new JMenuItem("扫描配置") ;  //菜单项
    JMenuItem urlconfig=new JMenuItem("网络配置") ;//菜单项
    JMenu help=new JMenu("帮助") ;     //创建JMenu菜单对象
    JMenuItem about=new JMenuItem("关于") ;  //菜单项
    JMenuItem sop=new JMenuItem("使用说明") ;  //菜单项
    JMenuItem data = new JMenuItem("历史数据分析");

    JLabel rate = new JLabel("  利率范围(%)");
    JLabel term = new JLabel("  期限范围(天)");
    JLabel status = new JLabel("             标状态");
    JLabel money = new JLabel("最小可投金额");
    JLabel priority = new JLabel("             优先级");
    JComboBox statusComboBox=new JComboBox();
    JCheckBox priority0 = new JCheckBox("利率高");
    JCheckBox priority1 = new JCheckBox("期限小");
    JCheckBox priority2 = new JCheckBox("期限大");
    JCheckBox priority3 = new JCheckBox("可投金额大");
    JLabel priorityText = new JLabel("1");
    JTextField rateText0 = new JTextField();
    JTextField rateText1 = new JTextField();
    JTextField termText0 = new JTextField();
    JTextField termText1 = new JTextField();
    JTextField moneyText = new JTextField();
    JButton listener = new JButton("开始监听");
    JButton stoplistener = new JButton("停止监听");
    JButton scanfigBt = new JButton("保存");
    JButton urlconfigBt = new JButton("保存");
    JLabel msg = new JLabel();

    JFrame framesub = null;
    JLabel interval = new JLabel("  扫描间隔(秒)");
    JLabel pageSize = new JLabel("  扫描条数(条)");
    JTextField intervalText = new JTextField();
    JTextField pageSizeText = new JTextField();

    JFrame framesuburl = null;
    JLabel scanurl = new JLabel("  扫描地址");
    JLabel loanList = new JLabel("  我要投资");
    JLabel loanDetail = new JLabel("  项目详情");
    JLabel autoJump = new JLabel("  自动跳转");
    JRadioButton jumpLoanDetail = new JRadioButton("项目详情");
    JRadioButton jumpLoanList = new JRadioButton("我要投资");
    ButtonGroup buttonGroup = new ButtonGroup();
    JTextField scanurlText = new JTextField();
    JTextField loanListText = new JTextField();
    JTextField loanDetailText = new JTextField();

    JFrame chartFrame = null;
    final JTabbedPane jtp = new JTabbedPane();
    public void initConfig(){
        KaisaBid kaisaBid = (KaisaBid) PropertiesUtil.copyProertiesToObject(new KaisaBid());
        moneyText.setText(kaisaBid.getMoney().toString());
        termText0.setText(kaisaBid.getTerm()[0].toString());
        termText1.setText(kaisaBid.getTerm()[1].toString());
        rateText0.setText((kaisaBid.getRate()[0]/100.0)+"");
        rateText1.setText((kaisaBid.getRate()[1]/100.0)+"");
        scanurlText.setText(kaisaBid.getScanUrl());
        loanDetailText.setText(kaisaBid.getLoanDetail());
        loanListText.setText(kaisaBid.getLoanList());
        intervalText.setText(kaisaBid.getScanInteval().toString());
        pageSizeText.setText(kaisaBid.getPageSize().toString());
        String priorityIndexs = kaisaBid.getPriorityIndexs();
        for(String index : priorityIndexs.split(",")){
            if(index.equals("1")){
                priority0.setSelected(true);
            }
            if(index.equals("2")){
                priority1.setSelected(true);
            }
            if(index.equals("3")){
                priority2.setSelected(true);
            }
            if(index.equals("4")){
                priority3.setSelected(true);
            }
        }
        priorityText.setText(priorityIndexs);
        String jumpage = kaisaBid.getAutoJumpPage();
        if(jumpage.equals("loanDetail")){
            jumpLoanDetail.setSelected(true);
        }else{
            jumpLoanList.setSelected(true);
        }
        statusComboBox.addItem("筹款中");//7
        statusComboBox.addItem("已流标");//8
        statusComboBox.addItem("已满标");//9
        statusComboBox.addItem("放款中");//10
        statusComboBox.addItem("还款中");//11
        statusComboBox.addItem("以上都是");
        statusComboBox.setSelectedIndex(Integer.parseInt(kaisaBid.getStatus())-7);
        kaisaBidController.setKaisaBid(kaisaBid);
        kaisaBidHistoryController.setKaisaBid(kaisaBid);
    }

    public MyFrame() {
        initConfig();
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(500, 320);// 设定窗口大小


        priority0.setName("1");
        priority1.setName("2");
        priority2.setName("3");
        priority3.setName("4");
        //priority0.setSelected(true);


        String[] titles = new String[]{"项目名", "期限", "收益率", "可投金额", "状态"};
        Vector<String> title = new Vector<String>(Arrays.asList(titles));
        Vector<Vector<Object>> objs = new Vector<Vector<Object>>(1);
        Vector<Object> sub = new Vector<Object>(5);
        //"标名", "期限", "收益率", "可投金额", "状态"
        sub.addElement("");
        sub.addElement(0);
        sub.addElement(0);
        sub.addElement(0);
        sub.addElement("");
        objs.addElement(sub);


        JTable table = new JTable();

        table.setPreferredScrollableViewportSize(new Dimension(460, 70));
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        DefaultTableModel model=new DefaultTableModel(objs,title);
        table.setModel(model);
        table.repaint();
        table.updateUI();



        config.add(scanconfig) ;   //将菜单项目添加到菜单
        config.add(urlconfig) ;    //将菜单项目添加到菜单
        help.add(about);
        help.add(sop);
        help.add(data);
        JMenuBar  br=new  JMenuBar() ;  //创建菜单工具栏
        br.add(config) ;      //将菜单增加到菜单工具栏
        br.add(help);
        frame.setJMenuBar(br) ;  //为 窗体设置  菜单工具栏



        status.setBounds(10, 10, 95, 20);
        statusComboBox.setBounds(100, 10, 125, 20);
        money.setBounds(230, 10, 95, 20);
        moneyText.setBounds(320, 10, 125, 20);
        rate.setBounds(10, 40, 95, 20);
        rateText0.setBounds(100, 40, 60, 20);
        rateText1.setBounds(165, 40, 60, 20);
        term.setBounds(230, 40, 95, 20);
        termText0.setBounds(320, 40, 60, 20);
        termText1.setBounds(385, 40, 60, 20);
        priority.setBounds(10,70,95,20);
        priority0.setBounds(100, 70, 65, 20);
        priority1.setBounds(165, 70, 65, 20);
        priority2.setBounds(230, 70, 65, 20);
        priority3.setBounds(295, 70, 90, 20);
        priorityText.setBounds(400,70,100,20);
        listener.setBounds(150,230,90,20);
        stoplistener.setBounds(260,230,90,20);
        msg.setBounds(10,230,120,20);
        scrollPane.setBounds(10,95,460,130);

        /*moneyText.setText("100");
        termText0.setText("1");
        termText1.setText("180");
        rateText0.setText("5.0");
        rateText1.setText("10.0");*/

        stoplistener.addActionListener(this);
        listener.addActionListener(this);
        scanconfig.addActionListener(this);
        about.addActionListener(this);
        urlconfig.addActionListener(this);
        sop.addActionListener(this);
        data.addActionListener(this);
        priority0.addActionListener(this);
        priority1.addActionListener(this);
        priority2.addActionListener(this);
        priority3.addActionListener(this);
        con.add(status);
        con.add(statusComboBox);
        con.add(money);
        con.add(moneyText);
        con.add(rate);
        con.add(rateText0);
        con.add(rateText1);
        con.add(term);
        con.add(termText0);
        con.add(termText1);
        con.add(priority);
        con.add(priority0);
        con.add(priority1);
        con.add(priority2);
        con.add(priority3);
        con.add(priorityText);

        con.add(listener);
        con.add(stoplistener);
        con.add(msg);

        con.add(scrollPane);

        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        frame.add( con);// 添加布局1


        kaisaBidController.setListener(listener);
        kaisaBidController.setStopListener(stoplistener);
        kaisaBidController.setTable(table);
        kaisaBidController.setObjs(objs);
        kaisaBidController.setTitle(title);
        kaisaBidController.setMsg(msg);

    }
    /**
     * 时间监听的方法
     */
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(priority0)||e.getSource().equals(priority1)||e.getSource().equals(priority2)||e.getSource().equals(priority3)){
            String pristr = priorityText.getText();
            pristr = ","+pristr+",";
            String name  = ((JCheckBox)e.getSource()).getName();
            boolean isSelected = ((JCheckBox) e.getSource()).isSelected();
            if(!isSelected){
                pristr = pristr.replace(","+name+",",",");
            }else{
                pristr += name +",";
            }
            if(pristr.length()>1)
                //priorityText.setText(pristr.substring(1,pristr.length()-1));
                setValue(priorityText,"priorityIndexs",pristr.substring(1,pristr.length()-1));
            else{
                ((JCheckBox) e.getSource()).setSelected(true);
            }
        }else if (e.getSource().equals(about)){
            JOptionPane.showMessageDialog(null, "版本号：v1.0\n\rzhouchuang1217@163.com\r\n欢迎提出您的宝贵建议", "信息", 2);
        }else if(e.getSource().equals(sop)){
            JOptionPane.showMessageDialog(null, "自己琢磨", "信息", 2);
        }else if (e.getSource().equals(scanfigBt)) {// 判断触发方法的按钮是哪个
            try{
                int inteval = Integer.parseInt(intervalText.getText());
                PropertiesUtil.setValue("scanInteval",inteval+"");
                kaisaBidController.getKaisaBid().setScanInteval(inteval);
            }catch (Exception e1){
                JOptionPane.showMessageDialog(null, "时间间隔只能输入数字", "警告", 2);
            }

            try{
                int pageSize = Integer.parseInt(pageSizeText.getText());
                PropertiesUtil.setValue("pageSize",pageSize+"");
                kaisaBidController.getKaisaBid().setPageSize(pageSize);
            }catch (Exception e1){

                JOptionPane.showMessageDialog(null, "扫描条数只能输入数字", "警告", 2);
            }
            if(kaisaBidController.getKaisaBid().getScanInteval()<15&&!"zhouchuang".equals(System.getenv().get("USERNAME"))){

                JOptionPane.showMessageDialog(null, "为了减少因频繁扫描给系统的压力，请输入大于等于15的时间间隔", "警告", 2);
            }
            if(kaisaBidController.getKaisaBid().getPageSize()>20){

                JOptionPane.showMessageDialog(null, "扫描页面数量最多20条", "警告", 2);
            }
            framesub.dispose();
        }else if (e.getSource().equals(urlconfigBt)) {// 判断触发方法的按钮是哪个
            PropertiesUtil.setValue("scanUrl",scanurlText.getText());
            kaisaBidController.getKaisaBid().setScanUrl(scanurlText.getText());
            PropertiesUtil.setValue("loanDetail",loanDetailText.getText());
            kaisaBidController.getKaisaBid().setLoanDetail(loanDetailText.getText());
            PropertiesUtil.setValue("loanList",loanListText.getText());
            kaisaBidController.getKaisaBid().setLoanList(loanListText.getText());
            PropertiesUtil.setValue("autoJumpPage",jumpLoanDetail.isSelected()?jumpLoanDetail.getName():jumpLoanList.getName());
            kaisaBidController.getKaisaBid().setAutoJumpPage(jumpLoanDetail.isSelected()?jumpLoanDetail.getName():jumpLoanList.getName());
            framesuburl.dispose();
        }else if(e.getSource().equals(urlconfig)){
            if(framesuburl==null){
                buttonGroup.add(jumpLoanDetail);
                buttonGroup.add(jumpLoanList);
                jumpLoanList.setName("loanList");
                jumpLoanDetail.setName("loanDetail");
                //jumpLoanDetail.setSelected(true);

                framesuburl =  new JFrame("网络配置");//构造一个新的JFrame，作为新窗口。
                Container consub = new Container();
                double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                framesuburl.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
                framesuburl.setSize(400, 200);// 设定窗口大小
                framesuburl.setBounds(// 让新窗口与Swing7窗口示例错开50像素。
                        new Rectangle(
                                (int) framesuburl.getBounds().getX() + 50,
                                (int) framesuburl.getBounds().getY() + 50,
                                (int) framesuburl.getBounds().getWidth(),
                                (int) framesuburl.getBounds().getHeight()
                        )
                );

                scanurl.setBounds(10,10,95,20);
                scanurlText.setBounds(80,10,300,20);
                loanDetail.setBounds(10,40,95,20);
                loanDetailText.setBounds(80,40,300,20);
                urlconfigBt.setBounds(150,130,100,20);
                loanList.setBounds(10,70,95,20);
                loanListText.setBounds(80,70,300,20);
                autoJump.setBounds(10,100,95,20);
                jumpLoanDetail.setBounds(80,100,100,20);
                jumpLoanList.setBounds(180,100,100,20);
                /*scanurlText.setText("https://www.kaisafax.com/loan/getLoanList?&ajax=1");
                loanDetailText.setText("https://www.kaisafax.com/loan/loanDetail?loanId=");
                loanListText.setText("https://www.kaisafax.com/loan");*/
                consub.add(scanurl);
                consub.add(scanurlText);
                consub.add(loanDetail);
                consub.add(loanDetailText);
                consub.add(urlconfigBt);
                consub.add(loanList);
                consub.add(loanListText);
                consub.add(autoJump);
                consub.add(jumpLoanDetail);
                consub.add(jumpLoanList);

                urlconfigBt.addActionListener(this);
                framesuburl.setVisible(true);
                framesuburl.add( consub);// 添加布局1
            }else{
                framesuburl.setVisible(true);
            }
        }else if (e.getSource().equals(scanconfig)) {// 判断触发方法的按钮是哪个
           if(framesub==null){
                framesub =  new JFrame("扫描配置");//构造一个新的JFrame，作为新窗口。
                Container consub = new Container();
                double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                framesub.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
                framesub.setSize(280, 200);// 设定窗口大小
                framesub.setBounds(// 让新窗口与Swing7窗口示例错开50像素。
                        new Rectangle(
                                (int) framesub.getBounds().getX() + 50,
                                (int) framesub.getBounds().getY() + 50,
                                (int) framesub.getBounds().getWidth(),
                                (int) framesub.getBounds().getHeight()
                        )
                );
                interval.setBounds(10,10,95,20);
                intervalText.setBounds(100,10,125,20);
                pageSize.setBounds(10,40,95,20);
                pageSizeText.setBounds(100,40,125,20);
                scanfigBt.setBounds(90,120,100,20);
               /* intervalText.setText("30");
                pageSizeText.setText("5");*/
                consub.add(interval);
                consub.add(intervalText);
                consub.add(pageSize);
                consub.add(pageSizeText);
                consub.add(scanfigBt);

                scanfigBt.addActionListener(this);
                framesub.setVisible(true);
                framesub.add( consub);// 添加布局1
            }else{
                framesub.setVisible(true);
            }

        }else   if (e.getSource().equals(listener)) {// 判断触发方法的按钮是哪个
            listener.setEnabled(false);
            Integer[] rate = new Integer[2];
            Integer[] term = new Integer[2];
            Integer money = 0;
            String priorityType="";
            try {
                rate[0] = (int)(Double.parseDouble(rateText0.getText())*100+0.00000001);
                rate[1] = (int)(Double.parseDouble(rateText1.getText())*100+0.00000001);
                term[0] = Integer.parseInt(termText0.getText());
                term[1] = Integer.parseInt(termText1.getText());
                money = Integer.parseInt(moneyText.getText());
                priorityType = priorityText.getText();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(null, "期限和比率只能输入数字", "警告", 2);
                stoplistener.doClick();
                return;
            }
            PropertiesUtil.setValue("money",money+"");
            kaisaBidController.getKaisaBid().setMoney(money);
            PropertiesUtil.setValue("rate",rate[0]+","+rate[1]);
            kaisaBidController.getKaisaBid().setRate(rate);
            PropertiesUtil.setValue("term",term[0]+","+term[1]);
            kaisaBidController.getKaisaBid().setTerm(term);
            PropertiesUtil.setValue("status",(statusComboBox.getSelectedIndex()+7)+"");
            kaisaBidController.getKaisaBid().setStatus((statusComboBox.getSelectedIndex()+7)+"");
            PropertiesUtil.setValue("priorityIndexs",priorityType);
            kaisaBidController.getKaisaBid().setPriorityIndexs(priorityType);
            moneyText.setEnabled(false);
            rateText0.setEnabled(false);
            rateText1.setEnabled(false);
            termText0.setEnabled(false);
            termText1.setEnabled(false);
            statusComboBox.setEnabled(false);
            config.setEnabled(false);
            help.setEnabled(false);
            priority0.setEnabled(false);
            priority1.setEnabled(false);
            priority2.setEnabled(false);
            priority3.setEnabled(false);
            try {
                kaisaBidController.startScan();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else  if (e.getSource().equals(stoplistener)) {

            moneyText.setEnabled(true);
            rateText0.setEnabled(true);
            rateText1.setEnabled(true);
            termText0.setEnabled(true);
            termText1.setEnabled(true);
            statusComboBox.setEnabled(true);
            config.setEnabled(true);
            help.setEnabled(true);
            priority0.setEnabled(true);
            priority1.setEnabled(true);
            priority2.setEnabled(true);
            priority3.setEnabled(true);
            try {
                kaisaBidController.stopScan();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(e.getSource().equals(data)){

            if(chartFrame==null){
                chartFrame = new JFrame("历史数据图表(前100条数据)");
                double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
                double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
                chartFrame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
                ImageIcon ii =  new ImageIcon(MyFrame.class.getResource("/chart.gif"));

                jtp.setPreferredSize(new Dimension(800,600));
                jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
                jtp.setTabPlacement(JTabbedPane.LEFT);
                //jtp.addTab("日放款金额",ii,createEmptyPanel(),"do noting");
                chartFrame.add(jtp, BorderLayout.CENTER);
                chartFrame.pack();
                chartFrame.setLocationRelativeTo(null);
                chartFrame.setVisible(true);

                ChartPanelUtil chartPanelUtil = new ChartPanelUtil(kaisaBidHistoryController);
                CreatePanelRunnable createPanelRunnable = new CreatePanelRunnable(jtp,chartPanelUtil,ii);
                Thread thread = new Thread(createPanelRunnable);
                thread.start();
            }else{
                chartFrame.setVisible(true);
            }
        }

    }

    /*private JPanel createEmptyPanel(){
        JPanel panel = new JPanel(false);

        //设置布局
        panel.setLayout(new GridLayout(1,1));

        //创建一个label放到panel中
        JLabel filler = new JLabel("");
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.add(filler);
        return panel;
    }
    private ChartPanel createDailyBar() {

        StandardChartTheme mChartTheme = new StandardChartTheme("CN");
        mChartTheme.setLargeFont(new Font("黑体", Font.BOLD, 20));
        mChartTheme.setExtraLargeFont(new Font("宋体", Font.PLAIN, 15));
        mChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        ChartFactory.setChartTheme(mChartTheme);
        CategoryDataset mDataset = kaisaBidHistoryController.getDailyTurnoverChartData();
        JFreeChart mChart = ChartFactory.createBarChart3D(
                "日放款总金额走势图",//图名字
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
        return new ChartPanel(mChart) {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(600, 400);
            }
        };
    }*/


    public void setValue(Object comp ,String key,Object msg){
        if(comp instanceof  JTextField){
            ((JTextField) comp).setText(msg.toString());
            PropertiesUtil.setValue(key,msg.toString());
        }else if(comp instanceof  JLabel){
            ((JLabel) comp).setText(msg.toString());
            PropertiesUtil.setValue(key,msg.toString());
        }
    }
}
