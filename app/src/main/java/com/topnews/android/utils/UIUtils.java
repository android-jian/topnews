package com.topnews.android.utils;

import android.content.Context;
import android.os.Handler;

import com.topnews.android.global.BaseApplication;

/**
 * Created by dell on 2017/2/26.
 *
 * UI工具类
 */

public class UIUtils {

    /**
     * 获取上下文对象
     * @return
     */
    public static Context getContext(){

        return BaseApplication.getmContext();
    }

    /**
     * 获取主线程id
     * @return
     */
    public static int getMainThreadId(){
        return BaseApplication.getMainThreadId();
    }

    /**
     * 获取handler实例
     * @return
     */
    public static Handler getHandler(){
        return BaseApplication.getmHandler();
    }

    /**
     * 判断当前是否运行在主线程
     * @return
     */
    public static boolean isRunOnUiThread(){
        return getMainThreadId()==android.os.Process.myTid();
    }

    /**
     * 确保当前的操作运行在UI主线程
     * @param runnable
     */
    public static void runOnUiThread(Runnable runnable){
        if(isRunOnUiThread()){
            runOnUiThread(runnable);
        }else{
            getHandler().post(runnable);
        }
    }

    /**
     * 获取字符串数组
     * @param id
     * @return
     */
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
}
