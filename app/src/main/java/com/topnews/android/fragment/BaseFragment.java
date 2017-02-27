package com.topnews.android.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/2/25.
 */

public class BaseFragment extends Fragment{

    public View initView(){

        TextView textView=new TextView(UIUtils.getContext());
        textView.setText("hahaha");
        return  textView;
    }
}
