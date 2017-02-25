package com.topnews.android.com.topnews.android.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by dell on 2017/2/25.
 *
 * 标签页-首页
 */

public class HomePager extends BasePager{

    public HomePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {

        TextView textView=new TextView(mActivity);
        textView.setText("HomePager");
        textView.setTextColor(Color.RED);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);

        fl_content.addView(textView);
    }
}
