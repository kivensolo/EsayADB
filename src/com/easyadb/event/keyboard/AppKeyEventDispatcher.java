package com.easyadb.event.keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AppKeyEventDispatcher implements KeyEventDispatcher {
    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        //BytecodeViewer.checkHotKey(e);
        return false;
    }
}