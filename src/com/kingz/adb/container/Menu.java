package com.kingz.adb.container;

import javax.swing.*;
import java.awt.*;

/**
 * author: King.Z <br>
 * date:  2017/12/9 15:29 <br>
 * description: 菜单项 <br>
 */
public class Menu extends Container {
    public Menu(JFrame frame) {
        JMenu jm = new JMenu("Menu");     //创建JMenu菜单对象
        JMenuItem t1 = new JMenuItem("Test1");  //菜单项
        JMenuItem t2 = new JMenuItem("Test2");//菜单项
        jm.add(t1);   //将菜单项目添加到菜单
        jm.add(t2);    //将菜单项目添加到菜单
        JMenuBar br = new JMenuBar();  //创建菜单工具栏
        br.add(jm);      //将菜单增加到菜单工具栏
        frame.setJMenuBar(br);
    }
}
