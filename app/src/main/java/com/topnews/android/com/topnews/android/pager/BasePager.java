package com.topnews.android.com.topnews.android.pager;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.topnews.android.R;

/**
 * Created by dell on 2017/2/25.
 *
 * 四个标签页的基类
 */

public class BasePager {

    public Activity mActivity;

    public FrameLayout fl_content;

    public View rootView;           //根布局对象

    public BasePager(Activity activity){

        this.mActivity=activity;
        rootView=initView();
    }

    /**
     * 初始化布局
     * @return
     */
    public View initView(){
        View view=View.inflate(mActivity, R.layout.base_pager,null);
        fl_content= (FrameLayout) view.findViewById(R.id.fl_content);

        return view;
    }

    /**
     * 初始化数据
     */
    public void initData(){

    }
}
