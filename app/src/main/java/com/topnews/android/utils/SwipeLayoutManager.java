package com.topnews.android.utils;

import com.topnews.android.view.SwipeLayout;

/**
 * Created by dell on 2017/4/7.
 */

public class SwipeLayoutManager {

    private static SwipeLayoutManager manager=null;

    private SwipeLayoutManager(){}

    public static SwipeLayoutManager getInstance(){

        if (manager==null){
            synchronized (SwipeLayoutManager.class){
                if (manager==null){
                    manager=new SwipeLayoutManager();
                }
            }
        }

        return manager;
    }

    //用来记录当前打开的SwipeLayout
    private SwipeLayout currentLayout;

    public void setSwipeLayout(SwipeLayout layout){

        this.currentLayout=layout;
    }

    public SwipeLayout getSwipeLayout(){

        return currentLayout;
    }

    public void clearSwipeLayout(){

        this.currentLayout=null;
    }

    /**
     * 关闭当前已经打开的swipeLayout
     */
    public void closeCurrentLayout(){
        if (currentLayout!=null){
            currentLayout.close();
        }
    }

    /**
     * 判断当前是否能够滑动 如果没有打开的，则可以滑动
     * 如果有打开的 则判断打开的swipeLayout和当前layout是否是同一个
     * @param layout
     * @return
     */
    public boolean isShouldSwipe(SwipeLayout layout){

        if(currentLayout==null){
            return true;
        }else{
            return currentLayout==layout;
        }
    }
}
