package com.topnews.android.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by dell on 2017/2/26.
 *
 * 自定义application
 */

public class BaseApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {

        super.onCreate();
        this.mContext=getApplicationContext();
    }

    public static Context getmContext(){

        return mContext;
    }
}
