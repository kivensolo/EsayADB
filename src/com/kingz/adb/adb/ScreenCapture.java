package com.kingz.adb.adb;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * author: King.Z <br>
 * date:  2017/11/29 16:45 <br>
 * description: XXXXXXX <br>
 *     //  TODO 后续做 截屏以及截屏需要保存的目录
 */
public class ScreenCapture {

    public static String[] getDevices() {
        String command = "adb devices";
        ArrayList<String> devices = new ArrayList<>();
        try {
            Process process = Runtime.getRuntime().exec(command);
            InputStream ins = process.getInputStream();
            InputStreamReader insReader = new InputStreamReader(ins);
            BufferedReader bufferedReader = new BufferedReader(insReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.endsWith("device")) {
                    String device = line.substring(0, line.length() - "device".length()).trim();
                    devices.add(device);
                }
                line = bufferedReader.readLine();
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices.toArray(new String[]{});
    }

    public static String getModel(String device) {
        String command = "adb -s " + device + " shell getprop";
        System.out.println(command);
        String model = null;

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line = bufferedReader.readLine();
            while (line != null) {
                if (line.contains("[ro.product.model]:")) {
                    model = line.substring(("[ro.product.model]:").length()).trim();
                    model = model.substring(1, model.length() - 1);
                    break;
                }
                line = bufferedReader.readLine();
            }
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void snapshot(String device, String toPath, String toFile) {
        String temp = "scrsnp.png";
        long t0 = new Date().getTime();
        String command1 = "adb -s " + device + " shell screencap -p /sdcard/" + temp;
        System.out.println(command1);
        cmdExecute(command1);
        long t1 = new Date().getTime();
        System.out.println(t1 - t0);
        String command2 = "adb -s " + device + " pull /sdcard/" + temp + " " + toPath + "/" + toFile;
        System.out.println(command2);
        cmdExecute(command2);
        long t2 = new Date().getTime();
        System.out.println(t2 - t1);
        String command3 = "adb -s " + device + " shell rm /sdcard/" + temp;
        System.out.println(command3);
        cmdExecute(command3);
        long t3 = new Date().getTime();
        System.out.println(t3 - t2);
    }

    public static void directSnapshot(String device, String toPath, String toFile) {

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(toPath + "/" + toFile));

            String command = "adb -s " + device + " shell screencap -p";
            Runtime runtime = Runtime.getRuntime();
            Process getProcess = runtime.exec(command);
            BufferedInputStream bis = new BufferedInputStream(getProcess.getInputStream());
            byte[] buf = new byte[1024 * 1024 * 4];
            int len = bis.read(buf);
            while (len != -1) {
                //bos.write(fixBytes(buf, len));
                len = bis.read(buf);
            }
            bos.close();
            getProcess.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cmdExecute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}