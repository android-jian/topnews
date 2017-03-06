package com.topnews.android.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.topnews.android.utils.UIUtils;
import com.topnews.android.view.LoadingPage;

/**
 * Created by dell on 2017/2/25.
 */

public class ImporNewsFragment extends BaseFragment{

    @Override
    public View onCreateSuccessView() {
        TextView textView=new TextView(UIUtils.getContext());
        textView.setText("hahaha");
        textView.setTextColor(Color.RED);

        return textView;
    }

    @Override
    public LoadingPage.ResultState dataLoad() {
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
