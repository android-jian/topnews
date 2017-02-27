package com.topnews.android.pager;

import android.view.View;

import com.topnews.android.R;
import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/2/26.
 *
 * 标签页-直播
 */

public class TalkingPager extends BasePager {

    @Override
    public void initData() {

    }

    @Override
    public View initView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.talking_pager,null);
        return view;
    }
}
