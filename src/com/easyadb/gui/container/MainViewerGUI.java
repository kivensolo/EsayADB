package com.easyadb.gui.container;

import com.easyadb.event.keyboard.AppKeyEventDispatcher;

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
public class MainViewerGUI extends JFrame {

    public static final String TITLE = "Easy ADB(v 1.0)     Edit by ZekeWong ";

    DeviceConnectPanel connectPanel;
    DetailInfoContainer infoComponent;
    ActionChoosePanel actionChooseContainer;
    public static String rootPath = "";
    public static String resLocalPath = "";
    public static String cfgLocalPath = "";

    /**
     * 窗口是否最大化
     */
    public boolean isMaximized = false;

    private void init() {
        setTitle(TITLE);
        //setExtendedState(Frame.MAXIMIZED_BOTH);// 将窗口最大化
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setVisible(true);
        setShowCenter();
    }

    private void setShowCenter() {
        Dimension frameSize = getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public MainViewerGUI() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new AppKeyEventDispatcher());
        addWindowStateListener(new AppWindowStateAdapter());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

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
        //初始化菜单
        new MenuBar(this);
        //调用框架组件的首选大小，或者我们可以用SetSize方法来替换它
        //pack();

        init();
    }

    //TODO 后续分隔符换成系统值
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
        connectPanel = new DeviceConnectPanel(this);
        add("North", connectPanel);
    }

    private void initLogArea() {
        infoComponent = new DetailInfoContainer();
        Dimension dimension = new Dimension(400, 200);
        infoComponent.setSize(dimension);
        add("South", infoComponent);
    }

    private void initActionChooseArea() {
        actionChooseContainer = new ActionChoosePanel(this);
        add("Center", actionChooseContainer);
    }


    private void initIconView() {
        Image img = new ImageIcon(MainViewerGUI.resLocalPath.concat("/icon.png")).getImage();
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


    public class AppWindowStateAdapter extends WindowAdapter {
        @Override
        public void windowStateChanged(WindowEvent evt) {
            int oldState = evt.getOldState();
            int newState = evt.getNewState();

            if ((oldState & Frame.ICONIFIED) == 0 && (newState & Frame.ICONIFIED) != 0) {
                //System.out.println("Frame was iconized");
            } else if ((oldState & Frame.ICONIFIED) != 0 && (newState & Frame.ICONIFIED) == 0) {
                //System.out.println("Frame was deiconized");
            }

            if ((oldState & Frame.MAXIMIZED_BOTH) == 0 && (newState & Frame.MAXIMIZED_BOTH) != 0) {
                isMaximized = true;
            } else if ((oldState & Frame.MAXIMIZED_BOTH) != 0 && (newState & Frame.MAXIMIZED_BOTH) == 0) {
                isMaximized = false;
            }
        }
    }

}
