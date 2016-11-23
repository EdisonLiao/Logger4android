package com.edison.www.logger4android;

import android.os.Environment;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016-11-23.
 */
public class Loggger implements Closeable{

    private static final StringBuilder DEFAULTPATH = new StringBuilder(Environment.getExternalStorageDirectory()
            + File.separator + "Loggger");     //默认的日志存储文件夹名
    private static final String DEFAULTLOGNAME = Environment.getExternalStorageDirectory()
            + File.separator + "Loggger" + File.separator + "log.txt";  //默认的日志名

    private StringBuilder savePath;     //日志存放文件名
    private String logName;     //日志名
    private String logContent;  //日志内容
    private StringBuilder finalLogContent; //最终的日志内容，时分秒毫秒+logContent
    private ExecutorService writeLogService;
    private boolean okToClose = false;

    public Loggger(){
        this(new LogBuilder());
    }

    Loggger(LogBuilder logBuilder){
        this.savePath = logBuilder.savePath;
        this.logName = logBuilder.logName;
    }

    public synchronized void writeLog(String content){
        this.logContent = content;
        writeLogService = Executors.newSingleThreadExecutor();
        writeLogService.execute(runnable);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                finalLogContent = new StringBuilder();
                String originSavePath = savePath.toString();
                File logFile = new File(savePath.append(File.separator).append(logName).toString());
                if (!logFile.exists()) {
                    File dir = new File(originSavePath);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    logFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(logFile,true);
                    fos.write(finalLogContent.append(getCurrentTime()).append(" ").
                            append(logContent).append("\n").toString().getBytes("utf-8"));
                    fos.flush();
                    fos.close();
                    setOkToClose(true);
                } else {
                    FileOutputStream fos = new FileOutputStream(logFile,true);
                    fos.write(finalLogContent.append(getCurrentTime()).append(" ").
                            append(logContent).append("\n").toString().getBytes("utf-8"));
                    fos.flush();
                    fos.close();
                    setOkToClose(true);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    };


    public static class LogBuilder{
        StringBuilder savePath;
        String logName;

        public LogBuilder(){
            savePath = DEFAULTPATH;
            logName = DEFAULTLOGNAME;
        }

        public LogBuilder savePath(String path){
            if (TextUtils.isEmpty(path)){
                this.savePath = DEFAULTPATH;
                throw new IllegalArgumentException("save path cannot be empty!");
            }else {
                this.savePath = new StringBuilder(path);
            }
            return this;
        }

        /**
         * 自命名日志名(We can name our log)
         * @param name 日志名 (The log name what you want)
         * @return
         */
        public LogBuilder logByName(String name){
            if (TextUtils.isEmpty(name)){
                this.logName = DEFAULTLOGNAME;
                throw new IllegalArgumentException("log name cannot be empty!");
            }else {
                this.logName = name;
            }
            return this;
        }

        /**
         * 日志名为当天日期 (The log name is today's date)
         * @return
         */
        public LogBuilder logByDay(){
            String originDate = "1992-07-18";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            StringBuilder now = new StringBuilder(sdf.format(System.currentTimeMillis()));
            if (!originDate.equals(now.toString())){
                this.logName = now.append(".txt").toString();
            }
            return this;
        }

        public Loggger build(){
            return new Loggger(this);
        }

    }

    private String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(System.currentTimeMillis());
    }

    @Override
    public void close(){
        if (isOkToClose() && writeLogService != null){
            writeLogService.shutdownNow();
        }
    }

    public boolean isOkToClose() {
        return okToClose;
    }

    public synchronized void setOkToClose(boolean okToClose) {
        this.okToClose = okToClose;
    }
}
