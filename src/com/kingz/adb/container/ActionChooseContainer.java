package com.kingz.adb.container;

import com.kingz.adb.action.ActionType;
import com.kingz.adb.adb.AdbRunnner;
import com.kingz.adb.config.ConfigManager;
import com.kingz.adb.inter_face.IActionListenner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: King.Z <br>
 * date:  2017/11/29 16:18 <br>
 * description: APK行为选择区域
 * 采用绝对布局
 */
public class ActionChooseContainer extends Container implements IActionListenner {
    public static final int ACTION_BTN_X = 10;
    public static final int ACTION_BTN_WIDTH = 100;
    public static final int ACTION_BTN_HEIGHT = 30;
    public static final int ACTION_BTN_OFFSET_Y = 80;
    public static final int ACTION_BTN_OFFSET_X = 5;

    private JLabel mTitle;
    private JButton addPkgBtn;
    private JTextField appenPkg;
    private JTextField splashPageId;
    private static String appPkgname = "";
    private String splashPage = "";
    private JMainFrame _mainFrame;
    private JComboBox<String> pkgComboBox;
    public static final String PKG_CONFIG_FILE = "\\packages.txt";
    public static final String SPLASH_CONFIG_FILE = "\\startPage.txt";

    private static List<String> pkgList = new ArrayList<>();
    private static List<String> splashPageList = new ArrayList<>();
    private static Map<ActionType, String> cmdMap = new HashMap<>();
    private static Map<String, JButton> actionMap = new HashMap<>();

    //static {
    //    pkgList.add("com.starcor.hunan");
    //    pkgList.add("com.starcor.xinjiang");
    //    pkgList.add("com.starcor.xinjiang.gov");
    //    pkgList.add("com.starcor.xinjiang.dispatcher");
    //}

    static {
        cmdMap.put(ActionType.CLEAR, "adb shell pm clear ");
        cmdMap.put(ActionType.UNINSTALL, "adb uninstall ");
        cmdMap.put(ActionType.FORCE_STOP, "adb shell am force-stop ");
        cmdMap.put(ActionType.START_APP, "adb shell am start ");
        cmdMap.put(ActionType.INPUT_TEXT, "adb shell input text ");
    }

    public ActionChooseContainer(JFrame jFrame) {
        //FlowLayout flowLayout = new FlowLayout();
        //flowLayout.setAlignment(FlowLayout.LEFT);
        _mainFrame = (JMainFrame) jFrame;
        setLayout(null);
        initConfigData();
        initPkgChooseArea();
        initChooseBtn();
    }

    private void initConfigData() {
        if(ConfigManager.getConfigData(pkgList,PKG_CONFIG_FILE)){
            if(pkgList.size() != 0){
                appPkgname = pkgList.get(0);
                //System.out.println("初始化包数据----成功 有数据");
            }else{
                System.out.println("初始化包数据---无数据或失败");
            }
        }

        if(ConfigManager.getConfigData(splashPageList,SPLASH_CONFIG_FILE)){
            if(splashPageList.size() != 0){
                String s = splashPageList.get(0);
                if(s != null && !s.isEmpty() && !s.equals("")){
                    splashPage = splashPageList.get(0);
                    //System.out.println("初始化Splash数据----成功");
                }
            }else{
                System.out.println("初始化Splash数据----无数据或失败");
            }
        }

    }

    private void initChooseBtn() {
        attachBtn("Clear", ActionType.CLEAR.value(),
                new Rectangle(ACTION_BTN_X,
                        40 + ACTION_BTN_OFFSET_Y,
                        ACTION_BTN_WIDTH,
                        ACTION_BTN_HEIGHT));
        attachBtn("Uninstall", ActionType.UNINSTALL.value(),
                new Rectangle(ACTION_BTN_X + ACTION_BTN_WIDTH + ACTION_BTN_OFFSET_X,
                        40 + ACTION_BTN_OFFSET_Y,
                        ACTION_BTN_WIDTH,
                        ACTION_BTN_HEIGHT));
        attachBtn("Stop", ActionType.FORCE_STOP.value(),
                new Rectangle(ACTION_BTN_X + 2 * (ACTION_BTN_WIDTH + ACTION_BTN_OFFSET_X),
                        40 + ACTION_BTN_OFFSET_Y,
                        ACTION_BTN_WIDTH,
                        ACTION_BTN_HEIGHT));
        attachBtn("Comming...", ActionType.CATCH_SCREEN.value(),
                new Rectangle(ACTION_BTN_X + 3 * (ACTION_BTN_WIDTH + ACTION_BTN_OFFSET_X),
                        40 + ACTION_BTN_OFFSET_Y,
                        ACTION_BTN_WIDTH,
                        ACTION_BTN_HEIGHT));
        attachBtn("Start：", ActionType.START_APP.value(),
                new Rectangle(ACTION_BTN_X,
                        ACTION_BTN_OFFSET_Y,
                        ACTION_BTN_WIDTH,
                        ACTION_BTN_HEIGHT));

        splashPageId = new JTextField();
        splashPageId.setBounds(new Rectangle(ACTION_BTN_X + ACTION_BTN_WIDTH + ACTION_BTN_OFFSET_X,
                        ACTION_BTN_OFFSET_Y,
                        265,
                        ACTION_BTN_HEIGHT));
        splashPageId.setFont(new Font("Helvetica", Font.PLAIN,14));
        splashPageId.setText(splashPage);
        add(splashPageId);
    }

    private void initPkgChooseArea() {
        mTitle = new JLabel("目标APP包名：");
        mTitle.setFont(new Font("楷体", Font.BOLD, 16));
        mTitle.setBounds(5, 0, 120, 20);
        add(mTitle);
        pkgComboBox = new JComboBox<>();
        if(pkgList.size() != 0){
            for (String pkg : pkgList) {
                pkgComboBox.addItem(pkg);
            }
            pkgComboBox.setSelectedIndex(0);
        }
        pkgComboBox.addItemListener(new PkgClickedLsr());
        pkgComboBox.setBounds(121, 0, 247, 25);
        add(pkgComboBox);

        addPkgBtn = new JButton("添加新包名:");
        addPkgBtn.setFont(new Font("楷体", Font.BOLD, 14));
        addPkgBtn.setBounds(5, 40, 120, 30);
        addPkgBtn.addActionListener(new AddPKGLsr());
        add(addPkgBtn);

        appenPkg = new JTextField("",12);
        appenPkg.setHorizontalAlignment(10);
        appenPkg.setFont(new Font("Helvetica", Font.PLAIN, 12));
        appenPkg.setBounds(130, 40, 250, 30);
        add(appenPkg);
    }

    private void attachBtn(String name, String cmd, Rectangle rect) {
        JButton btn = new JButton(name);
        btn.setActionCommand(cmd);
        btn.setBounds(rect);
        btn.addActionListener(new KActionListenner());
        add(btn);
        actionMap.put(cmd,btn);
    }

    @Override
    public void onStart(ActionType actionType) {
        System.out.println("onStart() actionType = " + actionType);
        if(actionType == null){
            throw new IllegalArgumentException("actionType can't be null");
        }
        if (actionType != ActionType.DEVICES) {
            printf(actionType.value() + "......     ");
        }
        if(actionType != ActionType.CATCH_SCREEN){
            Tick(actionType, false);
        }
    }

    @Override
    public void onSuccess(ActionType actionType, String info) {
        System.out.println("onSuccess() actionType = " + actionType + "; info = " + info);
        //printflnLog("sucess!");
        printflnLog(info);
        if(actionType == null){
            printflnLog(" error !  actionType is null. ");
            return;
        }
        Tick(actionType, true);
        if(actionType == ActionType.START_APP){
            ConfigManager.setConfigData(SPLASH_CONFIG_FILE,splashPageId.getText().trim(),false);
        }
    }

    @Override
    public void onError(ActionType actionType, String info) {
        System.out.println("onError() actionType = " + actionType + "; info = " + info);
        if (actionType == null) {
            printflnLog(" error !  actionType is null. ");
            return;
        }
        printflnLog(" error !  info : " + info);
        Tick(actionType, true);
    }

    class KActionListenner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPkgNameEmpty()) {
                onError(ActionType.EMPTY, "所选包名为空,请选择有效APP");
                return;
            }
            if(!_mainFrame.connectContainer.isCurrentDeviceConnected()){
                onError(ActionType.EMPTY, "请先连接设备!");
                return;
            }
            String command = e.getActionCommand();
            final ActionType actionType = ActionType.fromString(command);
            onStart(actionType);
            if(actionType == ActionType.CONNECT
                || actionType == ActionType.DISCONNECT
                || actionType == ActionType.CLEAR
                || actionType == ActionType.UNINSTALL
                || actionType == ActionType.FORCE_STOP){
                doAction(cmdMap.get(actionType) + appPkgname , actionType);
            }else if(actionType == ActionType.START_APP){
                String pageId = splashPageId.getText();
                if(!pageId.trim().isEmpty()){
                    ConfigManager.setConfigData(SPLASH_CONFIG_FILE,pageId,false);
                }
                doAction(cmdMap.get(actionType) + appPkgname +"/" + pageId, actionType);
            }
        }
    }

    protected class PkgClickedLsr implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            appPkgname = e.getItem().toString();
        }
    }

    protected class AddPKGLsr implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String addPkg = appenPkg.getText();
            if ("".equals(addPkg)) {
                printflnLog("请先输入包名");
                return;
            }
            if (!pkgList.contains(addPkg)) {
                if (ConfigManager.setConfigData(PKG_CONFIG_FILE, addPkg,true)) {
                    if (ConfigManager.getConfigData(pkgList, PKG_CONFIG_FILE)) {
                        sycnBkgBox();
                    }
                    printflnLog("添加成功");
                } else {
                    printflnLog("添加失败，请先检查config\\packages.txt文件是否存在");
                }
            } else {
                printflnLog("已存在，无需重复添加");
            }
        }
    }

   private void sycnBkgBox(){
        if(pkgList.size() != 0){
            pkgComboBox.removeAllItems();
            for (String ip : pkgList) {
                pkgComboBox.addItem(ip);
            }
            pkgComboBox.setSelectedIndex(0);
        }
    }

    private void doAction(final String cmd, final ActionType actionType) {
        if (cmd == null || "".equals(cmd)) {
            onError(ActionType.EMPTY, "功能未实现,敬请期待");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = AdbRunnner.start(cmd);
                onSuccess(actionType, result);
                //if (JMainFrame.ERROR_KEYS.contains(result) && !"".equals(result)) {
                //    onError(actionType,result);
                //} else {
                //    onSuccess(actionType, result);
                //}
            }
        }).start();
    }

    private boolean isPkgNameEmpty() {
        return "".equals(appPkgname.trim());
    }

    private void printflnLog(String value) {
        _mainFrame.infoComponent.appenText(value + "\r\n");
    }

    private void printf(String value) {
        _mainFrame.infoComponent.appenText(value);
    }

    private void Tick(ActionType actionType, boolean b) {
        if (actionMap != null && actionMap.size() != 0) {
            JButton jButton = actionMap.get(actionType.value());
            if(jButton != null){
                jButton.setEnabled(b);
            }
        }
    }
}
