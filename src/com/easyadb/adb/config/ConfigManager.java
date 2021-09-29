package com.easyadb.adb.config;

import com.easyadb.gui.container.MainViewerGUI;
import com.easyadb.adb.dm.IpModel;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class ConfigManager {
     public static final String Eencoding = "UTF-8";
     public static boolean getConfigData(List<String> dataList, String fileName){
         try {
            File file = new File(MainViewerGUI.cfgLocalPath.concat(fileName));
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader( new FileInputStream(file), Eencoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT;
                dataList.clear();
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    if(!lineTXT.isEmpty()  && !lineTXT.equals("")){
                        //System.out.println("read config:" + lineTXT);
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

     public static boolean getIpConfigData(List<IpModel> dataList, String fileName){
         try {

             File ipData = new File(MainViewerGUI.cfgLocalPath.concat(fileName));
             if (ipData.isFile() && ipData.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(ipData), Eencoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTXT;
                dataList.clear();
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    if(!lineTXT.isEmpty()  && !lineTXT.equals("")){
                        System.out.println("[Read] cfg: " + lineTXT);
                        String[] ipParams = lineTXT.split("#");
                        IpModel ip = new IpModel();
                        if(ipParams.length == 2){
                            try {
                                ip = new IpModel(ipParams[0],Integer.valueOf(ipParams[1]));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else if(ipParams.length == 1){
                            ip = new IpModel(ipParams[0]);
                        }
                        dataList.add(ip);
                    }
                }
                Collections.sort(dataList);
                read.close();
                return true;
            } else {
                System.out.println("找不到指定的文件！创建一个");
                if(ipData.createNewFile()){
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
            File file = new File(MainViewerGUI.cfgLocalPath.concat(fileName));
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

      public static boolean setIpConfigData(String fileName, IpModel ipModel, boolean isAppend){
        try {
            File file = new File(MainViewerGUI.cfgLocalPath.concat(fileName));
            if (file.isFile() && file.exists()) {
                OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream(file,isAppend), Eencoding);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                bufferedWriter.write(ipModel.toString()+"\n");
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

    /**
     * 通过文件复制更新数据
     */
      public static boolean updateIpConfigData(String fileName, IpModel ipModel){
        try {
            File file = new File(MainViewerGUI.cfgLocalPath.concat(fileName));
            File fileBkg = new File(MainViewerGUI.cfgLocalPath.concat(fileName).concat("bkg"));
            fileBkg.createNewFile();
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), Eencoding);
                BufferedReader bufferedReader = new BufferedReader(read);

                OutputStreamWriter writer = new OutputStreamWriter( new FileOutputStream(fileBkg), Eencoding);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
                String lineTXT;
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    if(lineTXT.contains(ipModel.getIp())){
                        lineTXT = ipModel.toString();
                    }
                    System.out.println("[Update] cfg: " + lineTXT);
                    bufferedWriter.write(lineTXT+"\n");
                }
                read.close();
                bufferedWriter.close();
                file.delete();
                fileBkg.renameTo(file);
                return true;
            } else {
                System.out.println("文件更新失败！");
                return false;
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

}
