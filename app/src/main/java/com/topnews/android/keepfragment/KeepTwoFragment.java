package com.topnews.android.keepfragment;

import android.view.View;

/**
 * Created by dell on 2017/3/28.
 *
 * 用户收藏页-专题
 */

public class KeepTwoFragment extends BaseKeepFragment {

    @Override
    public void dataLoad() {
        setCurState(DATA_LOAD_ERROR);
    }

    @Override
    public View onSuccessView() {
        return null;
    }
}
