package com.topnews.android.utils;

import android.content.Context;

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
     * 获取字符串数组
     * @param id
     * @return
     */
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
}
