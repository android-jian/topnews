package com.topnews.android.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.topnews.android.R;
import com.topnews.android.adapter.TopFragmentAdapter;
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

    private List<String> mDatas;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recycler_view;
    private LinearLayoutManager layoutManager;

    private int lastVisibleItem;

    /**
     * 如果加载数据成功 就回调此方法
     * @return
     */
    @Override
    public View onCreateSuccessView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.top_fragment,null);

        swipe_refresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);

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

                        try {
                            Thread.sleep(2000);

                            for (int i=0;i<5;i++){
                                mDatas.add(0,"这是新增的第"+i+"条数据");
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
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

                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i< 5; i++) {
                                int index = i +1;
                                newDatas.add("more item" + index);
                            }
                            mDatas.addAll(newDatas);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    adapter.notifyDataSetChanged();
                                    adapter.changeMoreStatus(TopFragmentAdapter.PULLUP_LOAD_MORE);
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

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mDatas = new ArrayList<String>();

        for (int i=0;i<20;i++){
            mDatas.add("这是第"+i+"条数据");
        }

        return LoadingPage.ResultState.STATE_SUCCESS;
    }
}
