package com.topnews.android.pager;

import android.view.View;

import com.topnews.android.R;
import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/2/26.
 *
 * 标签页-设置
 */

public class SettingPager extends BasePager {

    @Override
    public View initView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.setting_pager,null);
        return view;
    }

    @Override
    public void initData() {

    }
}
