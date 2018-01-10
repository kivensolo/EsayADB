package com.kingz.adb.config;

import com.kingz.adb.container.JMainFrame;

import java.io.*;
import java.util.List;

public class ConfigManager {
     public static final String Eencoding = "UTF-8";
     public static boolean getConfigData(List<String> dataList, String fileName){
         try {
            File file = new File(JMainFrame.cfgLocalPath.concat(fileName));
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader( new FileInputStream(file), Eencoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT;
                dataList.clear();
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    if(!lineTXT.isEmpty()  && !lineTXT.equals("")){
                        System.out.println("read config:" + lineTXT);
                        dataList.add(lineTXT);
                    }
                }
                read.close();
                return true;
            } else {
                System.out.println("找不到指定的文件！创建一个");
                if(file.createNewFile()){
                    System.out.println("创建成功");
                }
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
         return false;
    }

      public static boolean setConfigData(String fileName, String data, boolean isAppend){
        try {
            File file = new File(JMainFrame.cfgLocalPath.concat(fileName));
            if (file.isFile() && file.exists()) {
                OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream(file,isAppend), Eencoding);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(data+"\n");
                bufferedWriter.close();
                return true;
            } else {
                System.out.println("找不到指定的文件！");
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

}
