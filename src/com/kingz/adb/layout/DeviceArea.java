package com.kingz.adb.layout;

import java.awt.*;

/**
 * author: King.Z <br>
 * date:  2017/11/27 17:52 <br>
 * description: 设备连接区域 <br>
 */
public class DeviceArea extends FlowLayout {

    public DeviceArea() {
    }

    /**
     * @see FlowLayout#RIGHT
     * @see FlowLayout#LEFT
     * @param align  对齐方式
     */
    public DeviceArea(int align) {
        super(align);
    }

    public DeviceArea(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

}
