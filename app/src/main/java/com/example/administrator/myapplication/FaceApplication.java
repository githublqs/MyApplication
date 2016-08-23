package com.example.administrator.myapplication;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.administrator.myapplication.fileupload.Constant;
import com.example.administrator.myapplication.fileupload.Log2;

/**
 * Created by lqs on 2016/8/17.
 */

public class FaceApplication extends Application {
    private   static final String TAG="FaceApplication";
    private static FaceApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        Constant.init();
        instance=this;
    }
    public  static  FaceApplication getInstance(){
        return instance;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        //Log2.d(TAG, "onTerminate");
        Constant.reset();
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
       // Log2.d(TAG, "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        //Log2.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }
    public static void logOut(){
        if(instance!=null){
            SharedPreferences sp=instance.getSharedPreferences("settings",MODE_PRIVATE);
            SharedPreferences.Editor edtor = sp.edit();
            edtor.clear();
            edtor.commit();
        }
    }
    /**
     * 是否已经登陆
     * @return
     */
    public  static  boolean logedIn(){
        if(instance!=null){
            SharedPreferences sp=instance.getSharedPreferences("settings",MODE_PRIVATE);
            return  sp.contains("username");
        }
        return  false;
    }
}