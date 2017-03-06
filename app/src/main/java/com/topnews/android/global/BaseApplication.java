package com.topnews.android.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

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
