package com.easyadb.gui.widget;

import javax.swing.*;

/**
 * author: King.Z <br>
 * date:  2017/11/27 17:17 <br>
 * description: 基础按钮样式 <br>
 */
public class BaseButton extends JButton{

    @Override
    public void setRolloverEnabled(boolean b) {
        super.setRolloverEnabled(b);
    }

    /**
     * 当鼠标经过时，显示指定的图标
     * @param rolloverIcon
     */
    @Override
    public void setRolloverIcon(Icon rolloverIcon) {
        super.setRolloverIcon(rolloverIcon);
    }

    /**
     * 当选择按钮时，显示指定的图标
     * @param selectedIcon
     */
    @Override
    public void setSelectedIcon(Icon selectedIcon) {
        super.setSelectedIcon(selectedIcon);
    }
}
