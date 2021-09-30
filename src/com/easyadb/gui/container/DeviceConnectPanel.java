package com.easyadb.gui.container;

import com.easyadb.adb.action.ActionType;
import com.easyadb.adb.adb.AdbRunnner;
import com.easyadb.adb.config.ConfigManager;
import com.easyadb.adb.dm.IpModel;
import com.easyadb.adb.dm.ResponseFilter;
import com.easyadb.adb.inter_face.IActionListenner;
import com.easyadb.gui.widget.ComponentsUtils;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author: King.Z <br>
 * date:  2017/11/27 17:27 <br>
 * description: 设备连接区域 <br>
 * <p/>
 * Container(contentPane) ---> add(JPanel ) --->JPanel.add（Conponet）
 * <p/>
 * [JTextField]
 * setHorizontalAlignment(JTextField.CENTER);
 * setFont(new Font("谐体",Font.BOLD|Font.ITALIC,16));
 * setEnabled(false);
 * <p/>
 * [JTextArea]
 * setText("JTextArea1");// setText()设置文本显示的内容
 * append("JTextArea2");// append()方法会将给定文本追加到文档结尾。
 * setLineWrap(true);// 设置文本区的换行策略。
 * setFont(new Font("标楷体", Font.BOLD, 16));  //设置当前字体。
 * setTabSize(2);//使用setTabSize()方法设置[Tab]键的跳离距离
 */
public class DeviceConnectPanel extends JPanel implements IActionListenner {
    private DeviceChecker mDeviceChecker;
    private JLabel mStateInfo;
    private MainViewerGUI _mainFrame;
    private JComboBox<String> ipComboBox;
    private JTextField ipAddLabel;
    private JButton addIpBtn;
    private static List<Pair<String, Icon>> deviceState = new ArrayList<>();
    private static Map<String, JButton> actionMap = new HashMap<>();
    private Font font = new Font("Helvetica", Font.PLAIN, 18);

    //--------- Ip 数据 ---------/
    private static List<IpModel> ipList = new ArrayList<>();
    private static String selectedIp = "";
    private static final String IP_CONFIG_FILE = "\\ip.txt";
    //--------- Ip 数据 ---------/

    static {
        deviceState.add(new Pair<String, Icon>("已连接", new ImageIcon(MainViewerGUI.resLocalPath.concat("/link.png"))));
        deviceState.add(new Pair<String, Icon>("未连接", new ImageIcon(MainViewerGUI.resLocalPath.concat("/Unlink.png"))));
    }

    public DeviceConnectPanel(MainViewerGUI mainFrame) {
        //Dimension dimension = new Dimension(400, 100);
        //setPreferredSize(dimension);
        _mainFrame = mainFrame;
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(10);
        setLayout(flowLayout);

        if(ConfigManager.getIpConfigData(ipList, IP_CONFIG_FILE)){
            if(ipList.size() != 0){
                selectedIp = ipList.get(0).getIp();
                System.out.println("初始化IP数据----成功 有数据");
            }else{
                System.out.println("初始化IP数据---无数据或失败");
            }
        }
        add(ComponentsUtils.createLabel("目标设备IP:"));

        ipComboBox = new JComboBox<>();
        ipComboBox.setFont(font);
        if(ipList.size() != 0){
            for (IpModel ip : ipList) {
                ipComboBox.addItem(ip.getIp());
            }
            ipComboBox.setSelectedIndex(0);
        }
        ipComboBox.addItemListener(new IPChangeAction());
        add(ipComboBox);

        attachBtn("连接", ActionType.CONNECT.value());
        attachBtn("断开", ActionType.DISCONNECT.value());

        mStateInfo = new JLabel("未连接", new ImageIcon(MainViewerGUI.resLocalPath.concat("/Unlink.png")), SwingConstants.CENTER);
        mStateInfo.setSize(200, 30);
        mStateInfo.setFont(font);
        add(mStateInfo);

        ipAddLabel = new JTextField("",12);
        ipAddLabel.setHorizontalAlignment(10);
        ipAddLabel.setFont(font);
        add(ipAddLabel);

        addIpBtn = new JButton("添加");
        addIpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String addIp = ipAddLabel.getText();
                if("".equals(addIp) || !isIpMatches(addIp)){
                    printflnLog("无效参数");
                    return;
                }
                IpModel ipModel = new IpModel(addIp);
                boolean ipIsexited = false;
                for(IpModel ip:ipList){
                    if(ip.getIp().equals(addIp)){
                        ipIsexited = true;
                        break;
                    }
                }
                if (!ipIsexited) {
                    if (ConfigManager.setIpConfigData(IP_CONFIG_FILE,ipModel,true)) {
                        if (ConfigManager.getIpConfigData(ipList, IP_CONFIG_FILE)) {
                            if (ipList.size() != 0) {
                                selectedIp = ipList.get(0).getIp();
                            }
                            sycnIpBox();
                        }
                        printflnLog("添加成功");
                    }else{
                        printflnLog("添加失败，请先检查config\\ip.txt文件是否存在");
                    }
                }else{
                    printflnLog("已存在，无需重复添加");
                }
            }
        });
        add(addIpBtn);

        mDeviceChecker = new DeviceChecker();
    }

    private void attachBtn(String name, String cmd) {
        JButton btn = new JButton(name);
        btn.setActionCommand(cmd);
        btn.addActionListener(new ConnectListenner());
        add(btn);
        if (cmd.equals(ActionType.DISCONNECT.value())) {
            btn.setEnabled(false);
        }
        actionMap.put(cmd, btn);
    }


    @Override
    public void onStart(ActionType actionType) {
        //TODO 应该可以用事件替换
        _mainFrame.infoComponent.clear();
        if (actionMap != null && actionMap.size() != 0) {
            if (actionType != ActionType.DEVICES) {
                printflnLog(actionType.value() + "......     ");
            }
        }
    }

    @Override
    public void onSuccess(ActionType actionType, String info) {
        if (actionType == null) {
            throw new IllegalArgumentException("actionType can't be null");
        }
        System.out.println("onSuccess  type:" + actionType.value() + "    info:" + info);
        if (actionType != ActionType.DEVICES) {
            printflnLog(info);
            if (actionType == ActionType.CONNECT) {
                _mainFrame.connectPanel.setDisConnectEnable(true);
                mDeviceChecker.update();
                updateIpConfig();
            } else if (actionType == ActionType.DISCONNECT) {
                _mainFrame.connectPanel.setConnectEnable(true);
                mDeviceChecker.update();
            }
        } else {
            if (info.contains(selectedIp)) {
                setConnectStateEnable(true);
            } else {
                setConnectStateEnable(false);
            }
        }
    }

    @Override
    public void onError(ActionType actionType, String info) {
        if(!"List of devices attached".equals(info)){
            printflnLog(info);
        }
        if (actionType == ActionType.CONNECT) {
            setConnectEnable(true);
        } else if (actionType == ActionType.DISCONNECT) {
            setDisConnectEnable(true);
        } else if (actionType == ActionType.DEVICES) {
            setConnectStateEnable(false);
        }
    }

    class IPChangeAction implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            System.out.println("on ipItem clicked. ");
            selectedIp = e.getItem().toString();
            mDeviceChecker.update();
        }
    }

    class ConnectListenner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            ActionType actionType = ActionType.fromString(actionCommand);
            String ip = selectedIp;
            System.out.println("ActionPerformed is:[" + actionCommand + "]  ip:[" + selectedIp+"]");
            if (isIpMatches(ip)) {
                if (actionType == ActionType.CONNECT) {
                    doAction("adb connect " + ip, actionType);
                } else if (actionType == ActionType.DISCONNECT) {
                    doAction("adb disconnect " + ip, actionType);
                }
            } else {
                onError(ActionType.CONNECT, "IP地址错误！");
            }

        }
    }

    public void setConnectEnable(boolean isEnable) {
        actionMap.get(ActionType.CONNECT.value()).setEnabled(isEnable);
    }

    public void setDisConnectEnable(boolean isEnable) {
        actionMap.get(ActionType.DISCONNECT.value()).setEnabled(isEnable);
    }

    public boolean isCurrentDeviceConnected(){
        return !actionMap.get(ActionType.CONNECT.value()).isEnabled();
    }

    private static Map<String, String> errorInfoMap = new HashMap();

    static {
        errorInfoMap.put("error", "error");
        errorInfoMap.put("unable", "unable");
        errorInfoMap.put(" Failure", "Failure");
        errorInfoMap.put("cannot connect", "cannot connect");
    }


    private void doAction(final String cmd, final ActionType actionType) {
        if (actionType != ActionType.DEVICES) {
            onStart(actionType);
        }
        if (cmd == null || "".equals(cmd)) {
            onError(ActionType.EMPTY, "功能未实现,敬请期待!");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = AdbRunnner.start(cmd);
                Set<Map.Entry<String, String>> entries = ResponseFilter.ERROR_KEYS.entrySet();
                for (Map.Entry<String, String> value:entries){
                    if(result.contains(value.getKey()) || "List of devices attached".equals(result)){
                        onError(actionType, result);
                        return;
                    }
                }
                onSuccess(actionType, result);
            }
        }).start();
    }

    private void printflnLog(String value) {
        _mainFrame.infoComponent.appenText(value + "\r\n");
    }

    private void setConnectStateEnable(boolean enable) {
        if (enable) {
            actionMap.get(ActionType.CONNECT.value()).setEnabled(false);
            actionMap.get(ActionType.DISCONNECT.value()).setEnabled(true);
            mStateInfo.setText(deviceState.get(0).getKey());
            mStateInfo.setIcon(deviceState.get(0).getValue());
        } else {
            actionMap.get(ActionType.CONNECT.value()).setEnabled(true);
            actionMap.get(ActionType.DISCONNECT.value()).setEnabled(false);
            mStateInfo.setText(deviceState.get(1).getKey());
            mStateInfo.setIcon(deviceState.get(1).getValue());
        }
    }


    private class DeviceChecker {
        public DeviceChecker() {
            update();
        }

        public void update() {
            System.out.println("update connect state.");
            doAction("adb devices", ActionType.DEVICES);
        }
    }

    private boolean isIpMatches(String ip){
        Pattern pattern = Pattern.compile("^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)((:?)([0-9]{1,5})?)$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    private void sycnIpBox(){
        if(ipList.size() != 0){
            ipComboBox.removeAllItems();
            for (IpModel ip : ipList) {
                ipComboBox.addItem(ip.getIp());
            }
            ipComboBox.setSelectedIndex(0);
        }
    }

    private void updateIpConfig() {
        for (IpModel ip : ipList) {
            if (ip.getIp().equals(selectedIp)) {
                ip.useTimesAdd();
                ConfigManager.updateIpConfigData(IP_CONFIG_FILE,ip);
                break;
            }
        }
    }
}
