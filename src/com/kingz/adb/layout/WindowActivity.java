package com.kingz.adb.layout;

import com.kingz.adb.container.JMainFrame;

import javax.swing.*;
import java.awt.*;

public class WindowActivity {
    public static void main(String[] args) {
        //try {
        //    //http://www.52im.net/thread-26-1-1.html
        //    BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
        //    org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper.launchBeautyEyeLNF();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        // 开启一个线程，所有的Swing组件必须由事件分派线程进行配置，线程将鼠标点击和按键控制转移到用户接口组件。
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new JMainFrame();
            }
        });

    }
}
