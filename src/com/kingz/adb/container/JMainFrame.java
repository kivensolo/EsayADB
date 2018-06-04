package com.kingz.adb.container;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

/**
 * author: King.Z <br>
 * date:  2017/11/27 16:48 <br>
 * description: 程序主窗口Frame <br>
 * JTextField ：editText
 * JComboBox
 * JCheckBox
 * JTextArea
 * JLabel
 * JButton
 * JRadioButton
 * JOptionPane 错误对话框  .showMessageDialog
 * \showInputDialog
 * \showConfirmDialog  0：是 1：否  2：取消
 */
public class JMainFrame extends JFrame {

    public static final String TITLE = "Easy ADB(v 1.0)     Edit by ZekeWong ";

    DeviceConnectContainer connectContainer;
    DetailInfoContainer infoComponent;
    ActionChooseContainer actionChooseContainer;
    public static String rootPath = "";
    public static String resLocalPath = "";
    public static String cfgLocalPath = "";

    public void init() {
        setTitle(TITLE);
        //setExtendedState(Frame.MAXIMIZED_BOTH);// 将窗口最大化
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 490);
        setVisible(true);
    }

    public JMainFrame() {
        //Toolkit kit = Toolkit.getDefaultToolkit();
        initPath();
        initIconView();
        setLayout(new BorderLayout());
        addWindowListener(new CloseWindowAdapter());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initConnectArea();
        initLogArea();
        initActionChooseArea();
        //add("East", new JButton("设备连接列表区域"));
        //add("West", new JButton("West Btn"));
        initMsgView();
        new Menu(this);
        //调用框架组件的首选大小，或者我们可以用SetSize方法来替换它
        //pack();
    }

    private void initPath(){
        File directory = new File(".");
        try {
            rootPath = directory.getCanonicalPath();
            resLocalPath = rootPath +"\\res";
            cfgLocalPath = rootPath +"\\config";
            //System.out.println("rootPath = " + rootPath + "  ;resLocalPath="+resLocalPath + "  ;config LocalPath = " + cfgLocalPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void addWindowListener(WindowListener l) {
        super.addWindowListener(l);
    }

    private void initConnectArea() {
        connectContainer = new DeviceConnectContainer(this);
        add("North", connectContainer);
    }

    private void initLogArea() {
        infoComponent = new DetailInfoContainer();
        Dimension dimension = new Dimension(400, 200);
        infoComponent.setSize(dimension);
        add("South", infoComponent);
    }

    private void initActionChooseArea() {
        actionChooseContainer = new ActionChooseContainer(this);
        add("Center", actionChooseContainer);
    }


    private void initIconView() {
        Image img = new ImageIcon(JMainFrame.resLocalPath.concat("/icon.png")).getImage();
        setIconImage(img);
    }

    private void initMsgView() {

    }

    class CloseWindowAdapter extends WindowAdapter {
        @Override
        public void windowActivated(WindowEvent e) {
            super.windowActivated(e);
        }

        @Override
        public void windowClosed(WindowEvent e) {
            super.windowClosed(e);
        }

        @Override
        public void windowClosing(WindowEvent e) {
            super.windowClosing(e);
        }
    }



}
