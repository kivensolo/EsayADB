package com.easyadb.adb.adb;

import java.io.*;

/**
 * author: King.Z <br>
 * date:  2017/11/29 16:33 <br>
 * description: ADB执行类 <br>
 *     //TODO 优先级低  优化 增加批量命令处理
 */
public class AdbRunnner {

    public static String start(String adbCmd) {
        return connect(adbCmd);
    }

    private static String connect(String adbCmd) {
        try {
            String[] cmd = new String[]{"cmd.exe", "/C",adbCmd};
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream ins = process.getInputStream();// 获取执行cmd命令后的信息
            InputStream es = process.getErrorStream(); // 错误信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);  // 输出
                sb.append(line);
            }
            synchronized (process){
                int exitValue = process.waitFor();
                process.getOutputStream().close();
            }
            //System.out.println("返回值：" + exitValue);
            return sb.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "连接失败";
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
