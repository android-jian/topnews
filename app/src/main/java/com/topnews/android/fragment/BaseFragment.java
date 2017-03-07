package com.topnews.android.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topnews.android.utils.UIUtils;
import com.topnews.android.view.LoadingPage;

import java.util.ArrayList;

/**
 * Created by dell on 2017/2/25.
 */

public abstract class BaseFragment extends Fragment{

    private LoadingPage loadingPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        loadingPage = new LoadingPage(UIUtils.getContext()){
            @Override
            public View onCreateSuccessView() {
                return BaseFragment.this.onCreateSuccessView();
            }

            @Override
            public ResultState dataLoad() {
                return BaseFragment.this.dataLoad();
            }
        };

        return loadingPage;
    }

    @Override
    public void onStart() {

        super.onStart();

        if (this instanceof TopFragment){        //手动加载第一页数据
            initData();
        }
    }

    /**
     * 加载成功的布局 必须由子类实现
     * @return
     */
    public abstract View onCreateSuccessView();

    /**
     * 加载网络数据 必须由子类实现
     * @return
     */
    public abstract LoadingPage.ResultState dataLoad();

    /**
     * 初始化数据操作
     */
    public void initData(){

        if (loadingPage!=null){
            loadingPage.onLoad();
        }
    }

    /**
     * 对获取的数据进行检查
     * @return
     */
    public LoadingPage.ResultState dataCheck(Object obj){

        if(obj!=null){
            if(obj instanceof ArrayList){
                ArrayList list=(ArrayList) obj;
                if(list.isEmpty()){
                    return LoadingPage.ResultState.STATE_EMPTY;
                }else{
                    return LoadingPage.ResultState.STATE_SUCCESS;
                }
            }
        }
        return LoadingPage.ResultState.STATE_ERROR;
    }
}
