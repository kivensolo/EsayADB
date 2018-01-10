package com.kingz.adb.widget;

import javax.swing.*;

/**
 * author: King.Z <br>
 * date:  2017/11/27 17:47 <br>
 * description: XXXXXXX <br>
 */
public class BaseComboBox extends JComboBox {
    @Override
    public int getSelectedIndex() {
        return super.getSelectedIndex();
    }

    @Override
    public Object getSelectedItem() {
        return super.getSelectedItem();
    }

    /**
     * 返回总个数
     * @return
     */
    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
