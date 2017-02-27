package com.topnews.android.pager;

import android.view.View;

/**
 * Created by dell on 2017/2/26.
 *
 * 四个标签页的基类
 */

public abstract class BasePager {

    /**
     * 初始化布局 必须由子类实现
     * @return
     */
    public abstract View initView();

    /**
     * 初始化数据 必须由子类实现
     */
    public abstract void initData();
}
