package com.topnews.android.keepfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/3/28.
 */

public class BaseKeepFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView mText=new TextView(UIUtils.getContext());
        mText.setText("用户收藏");
        mText.setTextColor(Color.RED);
        mText.setTextSize(20);

        return mText;
    }
}
