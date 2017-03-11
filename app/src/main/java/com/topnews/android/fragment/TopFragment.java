package com.topnews.android.fragment;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.topnews.android.R;
import com.topnews.android.adapter.TopFragmentAdapter;
import com.topnews.android.gson.TopInfo;
import com.topnews.android.protocol.TopProtocol;
import com.topnews.android.utils.UIUtils;
import com.topnews.android.view.LoadingPage;
import com.topnews.android.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import static com.topnews.android.utils.UIUtils.runOnUiThread;

/**
 * Created by dell on 2017/2/25.
 *
 * 头条
 */

public class TopFragment extends BaseFragment {

    private List<TopInfo> mDatas;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private LinearLayoutManager layoutManager;

    private int lastVisibleItem;

    private int loadMorePage;      //加载更多页数标记

    private ImageButton ib_back_top;
    private float defaultY;

    /**
     * 如果加载数据成功 就回调此方法
     * @return
     */
    @Override
    public View onCreateSuccessView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.top_fragment,null);

        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

        ib_back_top= (ImageButton) view.findViewById(R.id.ib_back_top);
        defaultY = ib_back_top.getTranslationY();

        layoutManager = new LinearLayoutManager(UIUtils.getContext());
        recycler_view.setLayoutManager(layoutManager);

        final TopFragmentAdapter adapter=new TopFragmentAdapter(mDatas);
        recycler_view.setAdapter(adapter);

        recycler_view.addItemDecoration(new RecycleViewDivider(UIUtils.getContext(), LinearLayoutManager.HORIZONTAL,2,R.color.gray));
        swipe_refresh.setColorSchemeResources(R.color.colorPrimary,R.color.yellow);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final TopProtocol protocol = new TopProtocol();
                        final String data = protocol.getDataFromServer(1);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (data != null) {

                                    ArrayList<TopInfo> mInfos = protocol.processData(data);

                                    for (int i = 0; i < mInfos.size(); i++) {
                                        if (!mDatas.contains(mInfos.get(i))) {
                                            mDatas.add(0, mInfos.get(i));
                                        }
                                    }

                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(UIUtils.getContext(), "刷新成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UIUtils.getContext(), "刷新失败 请重试", Toast.LENGTH_SHORT).show();
                                }

                                swipe_refresh.setRefreshing(false);
                            }
                        });
                    }

                }).start();
            }
        });

        recycler_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==adapter.getItemCount()
                        && adapter.getLoadMoreStatus()!=TopFragmentAdapter.LOADING_MORE) {
                    adapter.changeMoreStatus(TopFragmentAdapter.LOADING_MORE);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            loadMorePage=mDatas.get(mDatas.size()-1).curPage+1;

                            TopProtocol protocol=new TopProtocol();
                            final ArrayList<TopInfo> moreData=protocol.getData(loadMorePage);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    if (moreData!=null){

                                        mDatas.addAll(moreData);
                                        adapter.notifyDataSetChanged();
                                        adapter.changeMoreStatus(TopFragmentAdapter.PULLUP_LOAD_MORE);
                                    }else{
                                        adapter.changeMoreStatus(TopFragmentAdapter.LOAD_MORE_FAIL);
                                    }
                                }
                            });
                        }
                    }).start();

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem =layoutManager.findLastVisibleItemPosition();

                if (layoutManager.findFirstVisibleItemPosition()==6 && dy>0){

                    backTopShow();

                }else if (layoutManager.findFirstVisibleItemPosition()==6 && dy<0){

                    backTopHide();
                }
            }
        });

        ib_back_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recycler_view.smoothScrollToPosition(0);
                backTopHide();
            }
        });

        return view;
    }

    /**
     * 网络加载数据
     * @return
     */
    @Override
    public LoadingPage.ResultState dataLoad() {

        TopProtocol protocol=new TopProtocol();
        mDatas=protocol.getData(1);

        return dataCheck(mDatas);
    }

    /**
     * 显示 返回顶部imagebutton
     */
    private void backTopShow(){

        ObjectAnimator moveIn = ObjectAnimator.ofFloat(ib_back_top, "translationY", defaultY, -200f);
        ObjectAnimator alphaUp= ObjectAnimator.ofFloat(ib_back_top,"alpha",0f,1f);
        AnimatorSet animSet=new AnimatorSet();
        animSet.play(moveIn).with(alphaUp);
        animSet.setDuration(1000);
        animSet.start();
    }

    /**
     * 隐藏 返回顶部imagebutton
     */
    private void backTopHide(){

        ObjectAnimator moveOut = ObjectAnimator.ofFloat(ib_back_top, "translationY", ib_back_top.getTranslationY(),defaultY );
        ObjectAnimator alphaDown= ObjectAnimator.ofFloat(ib_back_top,"alpha",1f,0f);
        AnimatorSet animSet=new AnimatorSet();
        animSet.play(moveOut).with(alphaDown);
        animSet.setDuration(500);
        animSet.start();
    }
}
