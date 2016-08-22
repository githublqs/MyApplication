package com.example.administrator.myapplication.fileupload;

import android.util.Log;

/**
 * Created by Administrator on 2016/8/22.
 */

public final class Log2 {

    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    public static final int VERBOSE = 2;
    public static final int WARN = 5;

    private Log2() {
    }

    public static int v(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.v(tag,msg);
        }
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.v(tag,msg,tr);
        }
        return 0;
    }

    public static int d(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.d(tag,msg);
        }
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.d(tag,msg,tr);
        }
        return 0;
    }

    public static int i(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.i(tag,msg);
        }
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.i(tag,msg,tr);
        }
        return 0;
    }

    public static int w(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.w(tag,msg);
        }
        return 0;
    }

    public static int w(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.w(tag,msg,tr);
        }
        return 0;
    }


    public static int w(String tag, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.w(tag,tr);
        }
        return 0;
    }

    public static int e(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.e(tag,msg);
        }
        return 0;
    }

    public static int e(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.e(tag,msg,tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.wtf(tag,msg);
        }
        return 0;
    }

    public static int wtf(String tag, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.wtf(tag,tr);
        }
        return 0;
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        if(Constant.ShowLog){
            return  Log.wtf(tag,msg,tr);
        }
        return 0;
    }

    public static String getStackTraceString(Throwable tr) {
        if(Constant.ShowLog){
            return  Log.getStackTraceString(tr);
        }
        return null;
    }

    public static int println(int priority, String tag, String msg) {
        if(Constant.ShowLog){
            return  Log.println(priority,tag,msg);
        }
        return 0;
    }
}
