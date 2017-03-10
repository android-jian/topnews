package com.topnews.android.view;

import android.content.Context;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.topnews.android.R;
import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/2/28.
 *
 * 根据当前状态来显示不同页面的自定义控件
 *
 * 页面状态：未加载、正在加载、数据为空、加载失败、加载成功
 */

public abstract class LoadingPage extends FrameLayout {

    private static final int PAGE_LOAD_UNDO=1;
    private static final int PAGE_LOAD_LOADING=2;
    private static final int PAGE_LOAD_NONE=3;
    private static final int PAGE_LOAD_FAIL=4;
    private static final int PAGE_LOAD_SUCCESS=5;

    private int currentState=PAGE_LOAD_UNDO;       //当前状态

    private View pageLoading;
    private View pageError;
    private View pageEmpty;
    private View pageSuccess;

    public LoadingPage(Context context) {
        super(context);

        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int currentState) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initView();
    }

    /**
     * 初始化布局
     * @return
     */
    public void initView(){

        if(pageLoading==null){                   //初始化加载中的布局
            pageLoading = View.inflate(UIUtils.getContext(), R.layout.page_loading,null);
            addView(pageLoading);
        }

        if(pageError==null){                     //初始化页面加载失败的布局
            pageError = View.inflate(UIUtils.getContext(), R.layout.page_error,null);

            Button btn_error= (Button) pageError.findViewById(R.id.btn_error);
            btn_error.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoad();
                }
            });

            addView(pageError);
        }

        if (pageEmpty==null){                   //初始化加载数据为空的布局
            pageEmpty=View.inflate(UIUtils.getContext(),R.layout.page_empty,null);
            addView(pageEmpty);
        }

        showRightPage();

    }

    /**
     * 根据当前状态决定显示哪个布局
     */
    public void showRightPage(){

        pageLoading.setVisibility((currentState==PAGE_LOAD_UNDO || currentState==PAGE_LOAD_LOADING)?View.VISIBLE:View.GONE);

        pageEmpty.setVisibility((currentState==PAGE_LOAD_NONE)?View.VISIBLE:View.GONE);

        pageError.setVisibility((currentState==PAGE_LOAD_FAIL)?View.VISIBLE:View.GONE);

        if (pageSuccess==null && currentState==PAGE_LOAD_SUCCESS){      //初始化加载数据成功的布局
            pageSuccess=onCreateSuccessView();

            if (pageSuccess!=null){
                addView(pageSuccess);
            }
        }

        if (pageSuccess!=null){
            pageSuccess.setVisibility((currentState==PAGE_LOAD_SUCCESS)?View.VISIBLE:View.GONE);
        }
    }

    /**
     * 数据加载
     */
    public void onLoad(){

        if (currentState!=PAGE_LOAD_LOADING){        //如果当前没有正在加载数据，就开始加载数据
            currentState=PAGE_LOAD_LOADING;
            new Thread(){

                @Override
                public void run() {
                    final ResultState mState=dataLoad();

                    UIUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mState!=null){
                                currentState=mState.getState();
                                showRightPage();
                            }
                        }
                    });
                }
            }.start();
        }
    }

    /**
     * 初始化加载数据成功的布局
     * @return
     */
    public abstract View onCreateSuccessView();

    /**
     * 数据加载  返回值表示请求网络结束后的状态
     * @return
     */
    public abstract ResultState dataLoad();

    public enum ResultState{

        STATE_SUCCESS(PAGE_LOAD_SUCCESS),STATE_ERROR(PAGE_LOAD_FAIL),STATE_EMPTY(PAGE_LOAD_NONE);

        private int state;
        private ResultState(int state){
            this.state=state;
        }

        public int getState(){
            return this.state;
        }
    }

}
