package com.topnews.android.keepfragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.topnews.android.R;
import com.topnews.android.adapter.KeepTwoAdapter;
import com.topnews.android.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/3/28.
 *
 * 用户收藏页-专题
 */

public class KeepTwoFragment extends BaseKeepFragment {

    private List<String> mDatas;
    private RecyclerView mRecycler;

    @Override
    public void dataLoad() {

        mDatas = new ArrayList<String>();

        for (int i=0;i<60;i++){
            mDatas.add("这是第"+i+"条测试数据");
        }
        setCurState(DATA_LOAD_SUCCESS);
    }

    @Override
    public View onSuccessView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.user_keep,null);

        mRecycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(UIUtils.getContext());
        mRecycler.setLayoutManager(manager);
        mRecycler.setAdapter(new KeepTwoAdapter(mDatas));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
}
