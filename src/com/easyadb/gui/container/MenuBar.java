package com.easyadb.gui.container;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * author: King.Z <br>
 * date:  2017/12/9 15:29 <br>
 * description: 菜单栏 <br>
 */
class MenuBar extends Container {
    MenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();  //创建菜单工具栏
        frame.setJMenuBar(menuBar);
        initFileTab(frame, menuBar);
    }

    private void initFileTab(JFrame frame, JMenuBar menuBar) {
        JMenu mnNewMenu = new JMenu("File");     //创建JMenu菜单对象
        JMenuItem mnItemOpen = new JMenuItem("Open...");  //菜单项
        mnItemOpen.addActionListener(event -> {
            final JFileChooser fc = new JFileChooser();
            try {
                File f = new File("."); //后续可换路径
                if (f.exists())
                    fc.setSelectedFile(f);
            } catch (Exception ignored) {
            }
            fc.setDialogTitle("Select File or Folder to open");
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setAcceptAllFileFilterUsed(true);
            int returnVal = fc.showOpenDialog(frame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                //try {
                //    BytecodeViewer.viewer.setIcon(true);
                //    BytecodeViewer.openFiles(new File[]{fc.getSelectedFile()}, true);
                //    BytecodeViewer.viewer.setIcon(false);
                //} catch (Exception e1) {
                //    new the.bytecode.club.bytecodeviewer.api.ExceptionUI(e1);
                //}
            }
        });
        JMenuItem subItem2 = new JMenuItem("Test2..............");//菜单项
        mnNewMenu.add(mnItemOpen);   //将菜单项目添加到菜单
        JSeparator separator_1 = new JSeparator();
        mnNewMenu.add(separator_1);

        mnNewMenu.add(subItem2);    //将菜单项目添加到菜单

        menuBar.add(mnNewMenu);      //将菜单增加到菜单工具栏
    }
}
