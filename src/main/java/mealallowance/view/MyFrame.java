package mealallowance.view;



import Util.ChineseUtil;
import Util.ReadExcel;
import Util.WriteExcelUtil;
import javafx.scene.control.ComboBox;
import mealallowance.MealAllowanceController;
import mealallowance.chain.NameFilter;
import mealallowance.chain.OvertimeFilter;
import mealallowance.model.MealAllowance;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;

/**
 * Created by zhouchuang on 2017/6/6.
 */
public class MyFrame  implements ActionListener {

    MealAllowanceController mealAllowanceController = new MealAllowanceController();
    JFrame frame = new JFrame("考勤餐补生成工具");// 框架布局
    Container con = new Container();//
    JLabel label1 = new JLabel("文件目录");
    JLabel label=new JLabel("员工姓名");
    JComboBox comboBox=new JComboBox();
    JTextField text1 = new JTextField();// TextField 目录的路径
    JButton button1 = new JButton("导入考勤文件");// 选择
    JFileChooser jfc = new JFileChooser();// 文件选择器
    JButton button3 = new JButton("生成");//
    JList jList = new JList();
    JTextArea jTextArea = new JTextArea("1：点击'导入考勤文件'按钮选中考勤统计文件（默认名称为'研发一部二部'）\n\r2：导入后选中需要导出的人员名字（默认选中为当前计算机登陆者名字）\r\n3：选中你需要生成的文件类型（可多选）\r\n4：点击生成，文件生成的默认位置在桌面上");
    JLabel typeLabel = new JLabel("类        型");
    //ButtonGroup buttonGroup = new ButtonGroup();
    JCheckBox jRadioButton1 = new JCheckBox("考勤异常");
    JCheckBox jRadioButton2 = new JCheckBox("加班餐补");
    public MyFrame() {


       /* buttonGroup.add(jRadioButton2);
        buttonGroup.add(jRadioButton1);*/
        text1.setEditable(false);
        jTextArea.setEditable(false);
        String desktoppath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        jfc.setCurrentDirectory(new File(desktoppath));
        jfc.setFileFilter(new ExcelFilter());
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

        frame.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));// 设定窗口出现位置
        frame.setSize(500, 280);// 设定窗口大小
        label1.setBounds(10, 10, 70, 20);
        text1.setBounds(75, 10, 278, 20);
        button1.setBounds(355, 10, 120, 20);
        button3.setBounds(200, 215, 100, 20);
        label.setBounds(10,40,70,20);
        comboBox.setBounds(75,40,400,20);
        jTextArea.setBounds(10,110,460,100);
        typeLabel.setBounds(10,70,50,20);
        jRadioButton1.setBounds(75,70,80,20);
        jRadioButton2.setBounds(155,70,80,20);
        button1.addActionListener(this); // 添加事件处理
        button3.addActionListener(this); // 添加事件处理
        con.add(label1);
        con.add(text1);
        con.add(button1);
        con.add(label);
        con.add(comboBox);
        con.add(typeLabel);
        con.add(jRadioButton1);
        con.add(jRadioButton2);
        con.add(jTextArea);
        button3.setEnabled(false);
        con.add(button3);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        frame.add( con);// 添加布局1
    }
    /**
     * 时间监听的方法
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
            jfc.setFileSelectionMode(0);// 设定只能选择到文件
            int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
            if (state == 1) {
                return;// 撤销则返回
            } else {
                File f = jfc.getSelectedFile();// f为选择到的文件
                text1.setText(f.getAbsolutePath());
                java.util.HashSet<String> usernames = mealAllowanceController.getUsename(f.getAbsolutePath());
                String yourname = System.getenv().get("USERNAME");
                usernames.stream().forEach(username ->{
                    comboBox.addItem(username);
                    String pinyin = null;
                    try {
                        pinyin = ChineseUtil.translateChineseToPinyin(username);
                    } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                        jTextArea.setText(badHanyuPinyinOutputFormatCombination.getLocalizedMessage());
                    }
                    if(pinyin.equals(yourname)){
                        comboBox.setSelectedItem(username);
                    }
                });
                button3.setEnabled(true);
            }
        }
        if (e.getSource().equals(button3)) {
            if(jRadioButton1.isSelected()||jRadioButton2.isSelected()){
                if(jRadioButton1.isSelected())
                    mealAllowanceController.generalExceptionExcel((String)comboBox.getSelectedItem(),text1.getText());
                if(jRadioButton2.isSelected())
                    mealAllowanceController.generalAllowanceExcel((String)comboBox.getSelectedItem(),text1.getText());
                JOptionPane.showMessageDialog(null, "生成成功", "提示", 2);
            }else{
                JOptionPane.showMessageDialog(null, "至少选择一项需要生成的类型", "警告", 2);
            }


            //打开浏览器
            //java.awt.Desktop dp=java.awt.Desktop.getDesktop();
        }
    }


}
