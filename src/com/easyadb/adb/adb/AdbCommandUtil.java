package com.easyadb.adb.adb;


import java.io.*;

public class AdbCommandUtil {

    public final String TAG = AdbCommandUtil.class.getSimpleName();

    private ICommandExecutor iCommandExecutor;

    public AdbCommandUtil(ICommandExecutor iCommandExecutor) {
        this.iCommandExecutor = iCommandExecutor;
    }

    /**
     * 执行单条命令
     *
     * @param command
     * @return
     */
    public boolean execute(String command) {
        boolean isSuccess = true;

        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command);
            // 创建2个线程，分别读取输入流缓冲区和错误流缓冲区
            ThreadUtil stdoutUtil = new ThreadUtil(p.getInputStream(), "OUTPUT:" + command);
            ThreadUtil errorOutUtil = new ThreadUtil(p.getErrorStream(), "ERROR:" + command);
            //启动线程读取缓冲区数据
            stdoutUtil.start();
            errorOutUtil.start();

            p.waitFor();
        } catch (IOException e) {
            isSuccess = false;
            e.printStackTrace();
        } catch (InterruptedException e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }

        return isSuccess;
    }

    class ThreadUtil implements Runnable {
        private String type;
        private InputStream inputStream;

        public ThreadUtil(InputStream inputStream, String t) {
            this.inputStream = inputStream;
            this.type = t;
        }

        public void start() {
            Thread thread = new Thread(this);
            thread.start();
        }

        public void run() {
            BufferedReader br = null;
            StringBuffer buffer = new StringBuffer();
            buffer.append(type).append(">>>");
            StringBuffer outBuffer = new StringBuffer();
            try {
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = br.readLine()) != null) {
//                    line += "\n";
                    buffer.append(line);
                    outBuffer.append(line);
                    System.out.println("readLine: " + line);
                    iCommandExecutor.onResult(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    System.out.println("输出" + buffer.toString());
                    String bufferStr = outBuffer.toString();
                    String array[] = bufferStr.split(">>>", 2);
                    String output = array[1];
                    if (output != null && !output.isEmpty()) {
//                        iCommandExecutor.onResult(buffer.toString());
                    }
                    //释放资源
                    inputStream.close();
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public interface ICommandExecutor {
        void onResult(String result);
    }
}
