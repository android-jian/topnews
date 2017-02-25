package com.topnews.android.com.topnews.android.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by dell on 2017/2/25.
 *
 * 标签页-直播
 */

public class TalkingPager extends BasePager{

    public TalkingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {

        TextView textView=new TextView(mActivity);
        textView.setText("TalkingPager");
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);

        fl_content.addView(textView);
    }
}
