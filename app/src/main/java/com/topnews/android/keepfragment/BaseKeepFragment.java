package com.topnews.android.keepfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.topnews.android.R;
import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/3/28.
 */

public abstract class BaseKeepFragment extends Fragment {

    public static final int DATA_LOADING=0;       //数据正在加载
    public static final int DATA_LOAD_SUCCESS=1;  //数据加载成功
    public static final int DATA_LOAD_ERROR=2;    //数据加载失败
    public static final int DATA_LOAD_NONE=3;     //数据加载为空
    public int curState=DATA_LOADING;             //默认为正在加载

    private FrameLayout mKeep;         //动态加载页面
    private View keepLoading;
    private View loadError;
    private View loadNone;
    private View loadSuccess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=View.inflate(UIUtils.getContext(),R.layout.keep_total,null);
        mKeep = (FrameLayout) view.findViewById(R.id.keep_total);

        initView();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataLoad();        //加载数据
    }

    /**
     * 初始化不同页面
     */
    private void initView(){

        if (keepLoading==null){
            keepLoading = View.inflate(UIUtils.getContext(), R.layout.page_loading,null);
            mKeep.addView(keepLoading);
        }

        if (loadError==null){
            loadError = View.inflate(UIUtils.getContext(), R.layout.keep_error,null);
            mKeep.addView(loadError);
        }

        if (loadNone==null){
            loadNone = View.inflate(UIUtils.getContext(), R.layout.keep_none,null);
            mKeep.addView(loadNone);
        }

        showRightPage();

    }

    /**
     * 根据不同状态显示不同界面
     */
    private void showRightPage(){

        keepLoading.setVisibility((curState==DATA_LOADING)?View.VISIBLE:View.GONE);

        loadNone.setVisibility((curState==DATA_LOAD_NONE)?View.VISIBLE:View.GONE);

        loadError.setVisibility((curState==DATA_LOAD_ERROR)?View.VISIBLE:View.GONE);

        if (loadSuccess==null && curState==DATA_LOAD_SUCCESS){

            loadSuccess=onSuccessView();
            mKeep.addView(loadSuccess);
        }

        if (loadSuccess!=null){
            loadSuccess.setVisibility((curState==DATA_LOAD_SUCCESS)?View.VISIBLE:View.GONE);
        }
    }

    /**
     * 数据加载操作 需要子类做具体实现
     */
    public abstract void dataLoad();

    /**
     * 加载成功的布局 需要子类做具体实现
     * @return
     */
    public abstract View onSuccessView();

    /**
     * 设置当前状态
     * @param state
     */
    public void setCurState(int state){

        curState=state;

        showRightPage();
    }
}
