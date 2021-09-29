package com.kingz.adb.widget;

import javax.swing.*;
import java.awt.*;

public class ComponentsUtils {

    public static JLabel createLabel(String text) {
        return createLabel(text, new Font("Helvetica", Font.PLAIN, 18));
    }

    public static JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }
}
