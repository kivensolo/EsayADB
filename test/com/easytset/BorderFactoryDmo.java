package com.easytset;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BorderFactoryDmo {
    JFrame jFrame = new JFrame("BorderUse"); //窗口
    Container contentPane = jFrame.getContentPane(); //内容窗格

    public static void main(String[] args) {
        BorderFactoryDmo borderUse = new BorderFactoryDmo();
        borderUse.go();
    }

    public void go() {
        contentPane.setLayout(new GridLayout(5, 2));  //更改内容窗格的布局管理器

        //第一种边框：只需一个参数。
        Border border = BorderFactory.createBevelBorder(1);  //新建Border实例
        Border border1 = BorderFactory.createTitledBorder(border, "Bevel Border");    //相当于为border加名字
        JPanel jPanel1 = new JPanel();  //新建面板
        contentPane.add(jPanel1);    //排版
        jPanel1.setBorder(border1);    //为jPanel1设置边框
        JTextArea textArea = new JTextArea(0, 15); //添加文本区
        jPanel1.add(textArea);    //文本区添加到面板
        textArea.setText("创建只有一个参数的Bevel Border, \n参数：0，1，2；对应：突起，凹陷，平"); //显示文本内容

        //第二种边框，有三种重载函数1个参数，2个 参数，4个参数
        border = BorderFactory.createBevelBorder(0, Color.magenta, Color.GREEN);
        Border border2 = BorderFactory.createTitledBorder(border, "Bevel Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border2);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Bevel Border,第一个颜色对应左上两边，\n第二个颜色对应右下两边，此外还可以有四种颜色");

        //第三种边框：5个参数，前面四个参数表示不同边的宽度
        border = BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLUE);
        Border border3 = BorderFactory.createTitledBorder(border, "Matte Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border3);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Mette Border ,前面四个参数\n分别对应四条边框的宽度,上左下右四边，颜色可以用图标代替");

        //和上面一样的边框
        border = BorderFactory.createMatteBorder(15, 20, 30, 40, Color.BLUE);
        Border border4 = BorderFactory.createTitledBorder(border, "Matte Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border4);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Mette Border ,前面四个参数10，20，30，40");


        //第四种边框：宽度默认只有一个值的边框，最后一个参数表示是否有圆角
        border = BorderFactory.createLineBorder(Color.BLACK, 7, false);
        Border border5 = BorderFactory.createTitledBorder(border, "Line Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border5);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Line Border ,可以有1 or 2 or 3个参数\n，第一个颜色，第二个宽度，第三个是否有圆角:此时为false");

        //第五种边框：其他两种边框的组合体
        border = BorderFactory.createCompoundBorder(border1, border3);
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Compound Border ,组合第1和第3个边框：");

        //第六种边框：虚线边框，第一个参数是Paint的子类对象，即表示颜色，后面的参数一起分别表示虚线的长和虚线之间的宽度
        GradientPaint gradientPaint = new GradientPaint(200f, 15f, Color.GREEN, 350, 15f, Color.RED, false);//建立渐变颜色
        border = BorderFactory.createDashedBorder(gradientPaint, 10f, 15);
        Border border7 = BorderFactory.createTitledBorder(border, "Dashed Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border7);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("创建有颜色的Dashed Border ,有3参数：\n第一实现Paint的对象，第二虚线的长，第三虚线之间的宽");

        //和上面一样的边框
        border = BorderFactory.createDashedBorder(Color.CYAN, 15f, 15);
        Border border8 = BorderFactory.createTitledBorder(border, "Dashed Border");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border8);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("此时Dashed的第一个参数是Color的静态数据成员\nColor.CYNA，第7个实例是渐变的Paint对象");

        //第七种边框：也是组合边框，相当于给边框加名字的功能，可以不组合。
        Border border9 = BorderFactory.createTitledBorder("OnlyTitle Botter");
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border9);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("上面的所以实例中都只用其他Border，然后\n再嵌入到Title Border 中，以达到有title\n的目的，此例是title的真面目");

        //和上面一样
        Border border10 = BorderFactory.createTitledBorder("OnlyTitle Botter");
        ((TitledBorder) border10).setTitleColor(Color.RED);
        jPanel1 = new JPanel();
        contentPane.add(jPanel1);
        jPanel1.setBorder(border10);
        textArea = new JTextArea(0, 15);
        jPanel1.add(textArea);
        textArea.setText("当Title Border和其他Border，一起使用时，\n还可以有最多6参数，参数3，4表示位置，\n参数5表示字体，最后表示字体颜色\n");
        textArea.append("第五个参数字体，可以用setTitleColer函数修改颜色\n第六个参数也是可以修改的");


        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(1200, 800);
        jFrame.setVisible(true);
    }


}

