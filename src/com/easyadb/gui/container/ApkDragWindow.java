package com.easyadb.gui.container;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FilenameFilter;

public class ApkDragWindow extends JFrame {

    private final IApkChooser mApkChooser;

    private JTextField textField;
    private JButton btRight;
    private JButton btConfirm;


    public static void open(IApkChooser iApkChooser) {
        ApkDragWindow apkDragWindow = new ApkDragWindow(iApkChooser);
        apkDragWindow.setVisible(true);
    }

    public ApkDragWindow(IApkChooser iApkChooser) {
        mApkChooser = iApkChooser;

        initFrame();
        initTextField();
        initButton();

        add(textField);
        add(btRight);
        add(btConfirm);

    }

    private void initFrame() {
        setTitle("Install");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initTextField() {
        textField = new JTextField();
        setText(true, "请将要安装的Apk文件拖拽到此处/点击右侧按钮选择");
        textField.setEditable(false);
        textField.setBounds(50, 100, 500, 50);
        textField.setTransferHandler(new TransferHandler() {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    if (!isApkFile(filepath)) {
                        return false;
                    }
                    System.out.println("filepath from drag:" + filepath);
                    setText(filepath);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (int i = 0; i < flavors.length; i++) {
                    if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void initButton() {
        btRight = new JButton();
        btRight.setText("打开");
        btRight.setActionCommand("open");
        btRight.setBounds(600, 100, 100, 50);
        btRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                if ("open".equals(command)) {
//                    JFileChooser jfc = new JFileChooser();
//                    //后缀名过滤器
//                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                            "apk文件(*.apk)", "apk");
//                    jfc.setFileFilter(filter);
//                    jfc.showOpenDialog(btRight);
//                    File f =  jfc.getSelectedFile();//使用文件类获取选择器选择的文件
//                    String filepath = f.getAbsolutePath();//返回路径名
//                   setText(filepath);

                    FileDialog dialog = new FileDialog(ApkDragWindow.this, "选择要安装的APK文件");
                    dialog.setFilenameFilter(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return isApkFile(name);
                        }
                    });
                    // 过滤apk类型文件
                    dialog.setFile("*.apk");
                    dialog.setVisible(true);
                    String path = dialog.getDirectory();
                    File f = new File(path);
                    File[] fs = f.listFiles(new java.io.FileFilter() {

                        @Override
                        public boolean accept(File file) {
                            String path = file.getAbsolutePath();
                            if (isApkFile(path)) {
                                //JOptionPane弹出对话框类，显示绝对路径名
                                setText(path);
                                return true;
                            } else {
                                setText(true, "请选择正确的apk文件");
                                return false;
                            }
                        }
                    });
                }
            }
        });

        btConfirm = new JButton();
        btConfirm.setText("确认安装");
        btConfirm.setActionCommand("confirm");
        btConfirm.setBounds(350, 200, 100, 50);
        btConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String apkFilePath = textField.getText();
                if (apkFilePath.isEmpty() || !isApkFile(apkFilePath)) {
                    return;
                }
                mApkChooser.onApkChosen(apkFilePath);
                dispose();
            }
        });
    }


    private void setText(String text) {
        setText(false, text);
    }

    private void setText(boolean hint, String text) {
        textField.setForeground(hint ? Color.GRAY : Color.BLACK);
        textField.setText(text);
    }

    private boolean isApkFile(String path) {
        return path.endsWith(".apk");
    }

    public interface IApkChooser {

        /**
         * 确认要安装的apk
         *
         * @param apkFilePath apk文件路径
         */
        void onApkChosen(String apkFilePath);

    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            dispose();
            mApkChooser.onApkChosen(null);
            return; //直接返回，阻止默认动作，阻止窗口关闭
        }
        super.processWindowEvent(e); //该语句会执行窗口事件的默认动作(如：隐藏)
    }
}
