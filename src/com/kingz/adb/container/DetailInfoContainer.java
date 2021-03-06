package com.kingz.adb.container;

import javax.swing.*;
import java.awt.*;

/**
 * //TODO 增加菜单按钮  进行背景颜色的选择
 */
public class DetailInfoContainer extends Container {
    public static final int POS_X = 15;
    public static final int POS_Y = 300;
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 200;
    private int _width = 0;
    private int _height = 0;
    JPanel jLeftPanel;
    JTextArea jTextArea;
    //TODO 添加清除按钮

    public DetailInfoContainer() {
        _width = DEFAULT_WIDTH;
        _height = DEFAULT_HEIGHT;
        setLayout(new BorderLayout());
        jLeftPanel = new JPanel();
        //JButton jButton = new JButton("清楚");
        //jLeftPanel.add(JButton);
        jTextArea = new JTextArea(10, 15);
        jTextArea.setTabSize(4);
        //setFont(new Font("标楷体", Font.PLAIN, 14));
        jTextArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        jTextArea.setLineWrap(true);        // 激活自动换行功能
        jTextArea.setWrapStyleWord(true);   // 激活断行不断字功能
        jTextArea.setEnabled(false);
        jTextArea.setDisabledTextColor(Color.YELLOW);
        jTextArea.setBackground(Color.gray);
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
        JScrollPane jscrollPane = new JScrollPane(jTextArea);
        add(jscrollPane);
    }

    public void appenText(String value) {
        if (jTextArea != null) {
            if(jTextArea.getText() == null || jTextArea.getText().equals("")){
                jTextArea.append("\n" + value);
            }else{
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
