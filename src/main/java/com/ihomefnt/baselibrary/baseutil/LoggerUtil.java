/*
 * @(#)Log.java 11-10-9 下午3:00 CopyRight 2011. All rights reserved
 */
package com.ihomefnt.baselibrary.baseutil;

import android.os.Environment;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日志打印
 * 
 * @author shulong
 * @version 1.0
 */
public abstract class LoggerUtil
{
    /**
     * 日期格式
     */
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     */
    private static final String LOG_PATH = Environment
        .getExternalStorageDirectory() + "/aijia/log";

    /**
     * 日志打印控制
     */
    private static boolean isPrintLog = false;

    /**
     * 日志打印到SD卡控制开
     */
    private static boolean isPrintLogSD = false;

    public static boolean getLogSwitch()
    {
        return isPrintLog;
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void v(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("v",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.v(tag,
                text);
        }
    }

    /**
     * 
     *
     * @param obj tag标记，传入当前调用的类对象即可，方法会转化为该对象对应的类名
     * @param text 日志内容
     */
    public static void d(Object obj, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("d",
                obj.getClass().getSimpleName(),
                text);
        }

        if (obj != null && isPrintLog)
        {
            d(obj.getClass().getSimpleName(),
                text);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void d(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("d",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.d(tag,
                text);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void i(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("i",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.i(tag,
                text);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void w(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("w",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.w(tag,
                text);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     * @param throwable 异常信息
     */
    public static void w(String tag, String text, Throwable throwable)
    {
        if (isPrintLogSD)
        {
            storeLog("w",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.w(tag,
                text,
                throwable);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void e(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("e",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.e(tag,
                text);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     * @param throwable 异常信息
     */
    public static void e(String tag, String text, Throwable throwable)
    {
        if (isPrintLogSD)
        {
            storeLog("e",
                tag,
                text);
        }

        if (isPrintLog)
        {
            android.util.Log.e(tag,
                text,
                throwable);
        }
    }

    /**
     *
     * @param tag 标记
     * @param text 日志内容
     */
    public static void file(String tag, String text)
    {
        if (isPrintLogSD)
        {
            storeLog("f",
                tag,
                text);
        }
    }

    /**
     * 
     * [用于存取错误日志信息] [功能详细描述]
     * 
     * @param type type
     * @param strModule module
     * @param strErrMsg message
     */
    public static void storeLog(String type, String strModule, String strErrMsg)
    {
        File file = openFile("uim.log");

        if (file == null)
        {
            return;
        }

        try
        {
            // 输出
            FileOutputStream fos = new FileOutputStream(file,
                true);
            PrintWriter out = new PrintWriter(fos);
            Date dateNow = new Date();
            String dateNowStr = dateFormat.format(dateNow);
            if (type.equals("e"))
            {
                out.println(dateNowStr + " Error:>>" + strModule + "<<  "
                    + strErrMsg + '\r');
            }
            else if (type.equals("d"))
            {
                out.println(dateNowStr + " Debug:>>" + strModule + "<<  "
                    + strErrMsg + '\r');
            }
            else if (type.equals("i"))
            {
                out.println(dateNowStr + " Info:>>" + strModule + "<<   "
                    + strErrMsg + '\r');
            }
            else if (type.equals("w"))
            {
                out.println(dateNowStr + " Warning:>>" + strModule + "<<   "
                    + strErrMsg + '\r');
            }
            else if (type.equals("v"))
            {
                out.println(dateNowStr + " Verbose:>>" + strModule + "<<   "
                    + strErrMsg + '\r');
            }
            else if (type.equals("f"))
            {
                out.println(dateNowStr + " File:>>" + strModule + "<<   "
                    + strErrMsg + '\r');
            }

            out.flush();
            out.close();
            out = null;

        }
        catch (FileNotFoundException e1)
        {
            e1.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
     * [功能详细描述]
     * 
     * @return 返回文件
     */
    private static File openFile(String name)
    {
        File fileDir = new File(LOG_PATH);

        // 判断目录是否已经存在
        if (!fileDir.exists())
        {
            android.util.Log.i("Log",
                "fileDir is no exists!");
            if (!fileDir.mkdirs())
            {
                return null;
            }
        }

        return new File(LOG_PATH,
            name);
    }

    /** 插入日志 */
    public static void addLog(String logStr)
    {
        File file = checkLogFileIsExist();
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file,
                true);
            fos.write((new Date().toLocaleString() + "  " + logStr)
                .getBytes("gbk"));
            fos.write("\r\n".getBytes("gbk"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null)
                {
                    fos.close();
                    fos = null;
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            fos = null;
            file = null;
        }
    }

    private static File checkLogFileIsExist()
    {
        File file = new File(LOG_PATH);
        if (!file.exists())
        {
            file.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date());
        file = new File(LOG_PATH + dateStr + ".txt");
        if (!isLogExist(file))
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        sdf = null;
        return file;
    }

    /**
     *
     * @param file
     * @return
     */
    private static boolean isLogExist(File file)
    {
        File tempFile = new File(LOG_PATH);
        File[] files = tempFile.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if (files[0].getName().trim().equalsIgnoreCase(file.getName()))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印异常堆栈信息
     * 
     * @param e
     * @return
     */
    public static String getExceptionStackTrace(Throwable e)
    {
        if (e != null)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return sw.toString();
        }
        return "";
    }
}
