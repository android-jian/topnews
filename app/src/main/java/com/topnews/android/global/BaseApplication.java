package com.topnews.android.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.bmob.v3.Bmob;

/**
 * Created by dell on 2017/2/26.
 *
 * 自定义application
 */

public class BaseApplication extends Application {

    private static Context mContext;

    private static Handler mHandler;

    private static int mainThreadId;

    @Override
    public void onCreate() {

        super.onCreate();

        this.mContext=getApplicationContext();
        mHandler=new Handler();
        mainThreadId=android.os.Process.myTid();      //获取主线程id

        Bmob.initialize(this, "c3afc6b5c969611f04beb79125e3ec2f");

        getSharedPreferences("config",MODE_PRIVATE).edit().putBoolean("night", false).commit();
    }

    public static Context getmContext(){

        return mContext;
    }

    public static Handler getmHandler(){

        return mHandler;
    }

    public static int getMainThreadId(){

        return mainThreadId;
    }
}
