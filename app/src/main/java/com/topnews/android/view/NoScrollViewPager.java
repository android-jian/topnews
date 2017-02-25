package com.topnews.android.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dell on 2017/2/25.
 *
 * 禁止滑动的viewpager
 */

public class NoScrollViewPager extends ViewPager {

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写此方法，触摸时什么都不做，从而实现对滑动事件的禁用
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }
}
