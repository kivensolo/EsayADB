package com.easyadb.gui.container;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 日志输出框
 */
public class DetailInfoContainer extends JPanel {
    public static final int POS_X = 15;
    public static final int POS_Y = 300;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;
    private int _width = 0;
    private int _height = 0;
    JPanel jLeftPanel;
    JTextArea jTextArea;
    //TODO 添加清除按钮
    MouseWheelListener sysWheel;

    public DetailInfoContainer() {
        _width = DEFAULT_WIDTH;
        _height = DEFAULT_HEIGHT;
        BorderLayout detailRootView = new BorderLayout();
        setLayout(detailRootView);
        JPanel root = new JPanel();
        root.setLayout(new BorderLayout());
        //Left
        jLeftPanel = new JPanel();
        JButton clearBtn = new JButton("C");
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("TODO 清理打印输出");
            }
        });
        jLeftPanel.add(clearBtn);
        TitledBorder borde = BorderFactory.createTitledBorder("");
        jLeftPanel.setBorder(borde);
        root.add(jLeftPanel,BorderLayout.WEST);

        jTextArea = new JTextArea(10, 15);
        jTextArea.setTabSize(4);
        //setFont(new Font("标楷体", Font.PLAIN, 14));
        jTextArea.setFont(new Font("Helvetica", Font.PLAIN, 16));
        jTextArea.setLineWrap(true);        // 激活自动换行功能
        jTextArea.setWrapStyleWord(true);   // 激活断行不断字功能
        jTextArea.setEnabled(false);
        jTextArea.setDisabledTextColor(Color.LIGHT_GRAY);
        jTextArea.setBackground(Color.decode("2829099"));
        JScrollPane jscrollPane = new JScrollPane(jTextArea);
        //得到系统滚动事件
        sysWheel = jscrollPane.getMouseWheelListeners()[0];
        //移除系统滚动，需要时添加, 防止放大时滚动
        jscrollPane.removeMouseWheelListener(sysWheel);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Logcat");
        titledBorder.setTitleFont(new Font("Helvetica", Font.PLAIN, 14));
        jscrollPane.setBorder(titledBorder);
        jscrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
//                System.out.println("mouseWheelMoved =" + e.toString());
                if (e.isControlDown()) {
                    Font font = jTextArea.getFont();
                    int rotation = e.getWheelRotation();
                    Font newFont;
                    if (rotation < 0) { //向上滚动 放大文字
                        newFont = new Font(font.getFamily(), font.getStyle(), font.getSize() + 1);
                    } else {
                        //向下滚动，缩小文字
                        newFont = new Font(font.getFamily(), font.getStyle(), font.getSize() - 1);
                    }
                    jTextArea.setFont(newFont);
                } else {
                    jscrollPane.addMouseWheelListener(sysWheel);
                    sysWheel.mouseWheelMoved(e);//触发系统滚动事件。
                    jscrollPane.removeMouseWheelListener(sysWheel);
                }
            }
        });
            //jTextArea.addMouseListener(new MouseAdapter() {
        //    public void mouseEntered(MouseEvent mouseEvent) {
        //        jTextArea.setCursor(new Cursor(Cursor.TEXT_CURSOR)); //鼠标进入Text区后变为文本输入指针
        //    }
        //
        //    public void mouseExited(MouseEvent mouseEvent) {
        //        jTextArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //鼠标离开Text区后恢复默认形态
        //    }
        //});
        //jTextArea.getCaret().addChangeListener(new ChangeListener() {
        //    public void stateChanged(ChangeEvent e) {
        //        jTextArea.getCaret().setVisible(true); //使Text区的文本光标显示
        //    }
        //});
        //jTextArea.setVisible(true); //使Text区的文本光标显示

        JScrollPane jScrollPane = new JScrollPane(jscrollPane);
        root.add(jScrollPane, BorderLayout.CENTER);
        add(root,BorderLayout.CENTER);

    }

    public void appenText(String value) {
        if (jTextArea != null) {
            if (jTextArea.getText() == null || jTextArea.getText().equals("")) {
                jTextArea.append("\n" + value);
            } else {
                jTextArea.append(value);
            }
        }
    }

    public void clear() {
        if (jTextArea != null) {
            jTextArea.setText("");
        }
    }

    public void paintComponent(Graphics g) {
        g.drawString("连接信息：", POS_X, POS_Y);
        g.drawString("连接中.........", POS_X, POS_Y + 12);
        g.drawRoundRect(POS_X - 5, POS_Y, _width, _height, 5, 5);
    }

    public Dimension getDimension() {
        return new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public void setWidth(int _width) {
        this._width = _width;
    }

    public void setHeight(int _height) {
        this._height = _height;
    }

    public JLabel createLabel() {
        return new JLabel();
    }

    class Textlabel extends JLabel {

    }
}
