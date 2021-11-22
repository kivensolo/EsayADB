package com.easyadb.gui.container;

import com.easyadb.adb.action.ActionType;
import com.easyadb.adb.adb.AdbCommandUtil;
import com.easyadb.adb.config.ConfigManager;
import com.easyadb.adb.inter_face.IActionListenner;
import com.easyadb.gui.widget.ComponentsUtils;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
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
 *
 */
public class ActionChooseJpanel extends JPanel implements IActionListenner, ApkDragWindow.IApkChooser {
    public static final int TEXT_SIZE = 18;
    Font font = new Font("Helvetica", Font.PLAIN, TEXT_SIZE);

    private JButton addPkgBtn;
    private JTextField appenPkg;
    private JTextField targetActivityFieldView;
    private static String appPkgname = "";
    private String splashPage = "";
    private MainViewerGUI _mainFrame;
    private JComboBox<String> pkgComboBox;
    private static final String PKG_CONFIG_FILE = "\\packages.txt";
    private static final String SPLASH_CONFIG_FILE = "\\startPage.txt";

    private static List<String> pkgList = new ArrayList<>();
    private static List<String> splashPageList = new ArrayList<>();
    private static Map<ActionType, String> cmdMap = new HashMap<>();
    private static Map<String, JButton> actionMap = new HashMap<>();

    private static final String CMD_QUERY_TMP_INSTALL_APK_PREFIX = "adb shell ls -l /data/local/tmp/";


    private Component paddingHolder = Box.createHorizontalStrut(5);

    //static {
    //    pkgList.add("com.starcor.hunan");
    //    pkgList.add("com.starcor.xinjiang");
    //    pkgList.add("com.starcor.xinjiang.gov");
    //    pkgList.add("com.starcor.xinjiang.dispatcher");
    //}

    static {
        cmdMap.put(ActionType.CLEAR, "adb shell pm clear ");
        cmdMap.put(ActionType.INSTALL, "adb install -r");
        cmdMap.put(ActionType.UNINSTALL, "adb uninstall ");
        cmdMap.put(ActionType.FORCE_STOP, "adb shell am force-stop ");
        cmdMap.put(ActionType.START_APP, "adb shell am start ");
        cmdMap.put(ActionType.INPUT_TEXT, "adb shell input text ");
        //dump the heap of a process
        cmdMap.put(ActionType.DUMP_HEAP, "adb shell am dumpheap ");  //cmd： adb shell am dumpheap [PROCESS] [FILENAME]
    }

    public ActionChooseJpanel(JFrame jFrame) {
        _mainFrame = (MainViewerGUI) jFrame;
        setLayout(new BorderLayout());

        initConfigData();
        initActionsArea();

        //使用JInternalFrame时就必须设置visible为true
        //setVisible(true);
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


    /**
     * 初始化目标应用选择和包名添加View
     */
    private void initActionsArea() {
        ////JScrollPane jScrollPane = new JScrollPane(actionPanel);
        //actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        //add(actionPanel);

        Box vtemp = Box.createVerticalBox();
        JPanel actionPanel = new JPanel();
        //actionPanel.setBackground(Color.ORANGE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder("操作选项");
        titledBorder.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        titledBorder.setTitleFont(new Font("Helvetica", Font.PLAIN, 14));
        actionPanel.setBorder(titledBorder);
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.X_AXIS));

        //第一行
        Box hBox01 = Box.createHorizontalBox();
        hBox01.add(ComponentsUtils.createLabel("目标APP包名:"));
        hBox01.add(Box.createHorizontalStrut(5));
        pkgComboBox = new JComboBox<>();
        pkgComboBox.setFont(font);
        if(pkgList.size() != 0){
            for (String pkg : pkgList) {
                pkgComboBox.addItem(pkg);
            }
            pkgComboBox.setSelectedIndex(0);
        }
        pkgComboBox.addItemListener(new PkgClickedLsr());
        pkgComboBox.setMaximumSize(new Dimension(200,40));
        hBox01.add(pkgComboBox);
        // 创建一个 水平方向胶状 的不可见组件，用于撑满水平方向剩余的空间（如果有多个该组件，则平分剩余空间）
        hBox01.add(Box.createHorizontalGlue());
        vtemp.add(hBox01);
        vtemp.add(Box.createVerticalStrut(5));

        //第二行
        Box hBox02 = Box.createHorizontalBox();
        hBox02.add(paddingHolder);
        addPkgBtn = new JButton("添加新包名");
        addPkgBtn.setFont(font);
        addPkgBtn.addActionListener(new AddPKGLsr());
        hBox02.add(addPkgBtn);
        hBox02.add(paddingHolder);

        appenPkg = new JTextField("",12);
        appenPkg.setPreferredSize(new Dimension(550,40));
        appenPkg.setMaximumSize(new Dimension(550,40));
        appenPkg.setFont(font);

        hBox02.add(appenPkg);
        hBox02.add(Box.createHorizontalGlue());
        vtemp.add(hBox02);
        vtemp.add(Box.createVerticalStrut(5));

        ////第三行
        Box hBox03 = Box.createHorizontalBox();
        hBox03.add(paddingHolder);
        attachBtn(hBox03,"Open:", ActionType.START_APP.value());
        hBox03.add(paddingHolder);
        targetActivityFieldView = new JTextField();
        //targetActivityFieldView.setBounds(new Rectangle(ACTION_BTN_X + ACTION_BTN_WIDTH + ACTION_BTN_OFFSET_X,
        //                ACTION_BTN_OFFSET_Y,
        //                350,
        //                ACTION_BTN_HEIGHT));
        targetActivityFieldView.setFont(font);
        targetActivityFieldView.setText(splashPage);
        targetActivityFieldView.setPreferredSize(new Dimension(450,40));
        targetActivityFieldView.setMaximumSize(new Dimension(1000,40));
        hBox03.add(targetActivityFieldView);
        hBox03.add(Box.createHorizontalGlue());
        vtemp.add(hBox03);
        vtemp.add(Box.createVerticalStrut(5));

        Box hBox04 = Box.createHorizontalBox();
        attachBtn(hBox04,"Clear", ActionType.CLEAR.value());
        attachBtn(hBox04,"Install", ActionType.INSTALL.value());
        attachBtn(hBox04,"Uninstall", ActionType.UNINSTALL.value());
        attachBtn(hBox04,"Stop", ActionType.FORCE_STOP.value());
        attachBtn(hBox04,"Comming...", ActionType.CATCH_SCREEN.value());
        hBox04.add(Box.createHorizontalGlue());
        vtemp.add(hBox04);
        vtemp.add(Box.createVerticalGlue());
        actionPanel.add(vtemp);
        this.add(actionPanel);
    }

    private void attachBtn(Box box,String name, String cmd) {
        JButton btn = new JButton(name);
        btn.setActionCommand(cmd);
        btn.setFont(font);
        btn.addActionListener(new KActionListenner());
        box.add(btn);
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
            ConfigManager.setConfigData(SPLASH_CONFIG_FILE, targetActivityFieldView.getText().trim(),false);
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

    @Override
    public void onApkChosen(String apkFilePath) {
        System.out.println("install target apk file: " + apkFilePath);
        if (null == apkFilePath || apkFilePath.isEmpty()) {
            Tick(ActionType.INSTALL, true);
            return;
        }
        ActionType actionType = ActionType.INSTALL;
        doAction(cmdMap.get(actionType) + " " + apkFilePath , actionType);
    }

    class KActionListenner implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (isPkgNameEmpty()) {
                onError(ActionType.EMPTY, "所选包名为空,请选择有效APP");
                return;
            }
            String command = e.getActionCommand();
            final ActionType actionType = ActionType.fromString(command);
            if(!_mainFrame.connectPanel.isCurrentDeviceConnected()){
                onError(ActionType.EMPTY, "请先连接设备!");
                return;
            }
            onStart(actionType);
            if (actionType == ActionType.INSTALL) {
                alertInstallWindow();
            }else if(actionType == ActionType.CONNECT
                || actionType == ActionType.DISCONNECT
                || actionType == ActionType.CLEAR
                || actionType == ActionType.UNINSTALL
                || actionType == ActionType.FORCE_STOP){
                doAction(cmdMap.get(actionType) + appPkgname , actionType);
            }else if(actionType == ActionType.START_APP){
                String pageId = targetActivityFieldView.getText();
                if(!pageId.trim().isEmpty()){
                    ConfigManager.setConfigData(SPLASH_CONFIG_FILE,pageId,false);
                }
                doAction(cmdMap.get(actionType) + appPkgname +"/" + pageId, actionType);
            }else if(actionType == ActionType.DUMP_HEAP){
                doAction(cmdMap.get(actionType) + appPkgname + "/data/anr/" + "33.hprof" , actionType);
            }
        }
    }

    private void alertInstallWindow() {
        ApkDragWindow.open(this);
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
            if ("".equals(addPkg.trim())) {
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
//                String result = AdbRunnner.start(cmd);
//                onSuccess(actionType, result);
                new AdbCommandUtil(new AdbCommandUtil.ICommandExecutor() {
                    @Override
                    public void onResult(String result) {
                        onSuccess(actionType, result);
                    }
                }).execute(cmd);
                //if (MainViewerGUI.ERROR_KEYS.contains(result) && !"".equals(result)) {
                //    onError(actionType,result);
                //} else {
                //    onSuccess(actionType, result);
                //}
            }
        }).start();
        if (ActionType.INSTALL.equals(actionType)) {
            // todo 获取安装进度
            // 先启动 adb push 进程，然后每隔 0.5 秒钟调用一次 adb shell ls -l /data/local/tmp/_tmp.apk
            // 获取到已经推送的文件的大小，再对比文件本身的大小，就可以知道进度了。
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }

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
