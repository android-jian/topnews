package com.topnews.android.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topnews.android.R;
import com.topnews.android.adapter.IconAdapter;
import com.topnews.android.gson.IconBean;
import com.topnews.android.utils.UIUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.topnews.android.utils.UIUtils.runOnUiThread;

/**
 * Created by dell on 2017/3/23.
 */

public class TalkingFragment extends BasePagerFragment{

    private RecyclerView mRecycler;
    private List<IconBean> mData;
    private IconAdapter adapter;

    private StaggeredGridLayoutManager layoutManager;
    int currentPage=0;    //默认当前页

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=View.inflate(UIUtils.getContext(), R.layout.talking_pager,null);

        mRecycler = (RecyclerView) view.findViewById(R.id.icon_recycler);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecycler.setLayoutManager(layoutManager);

        initData();

        initListener();

        return view;
    }

    /**
     * 初始化数据操作
     */
    private void initData(){

        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer(){

        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url("http://gank.io/api/search/query/listview/category/福利/count/20/page/1").build();
        Call call=mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData=response.body().string();

                if (processIconData(responseData)!=null){
                    mData=processIconData(responseData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter = new IconAdapter(UIUtils.getContext(),mData);
                            mRecycler.setAdapter(adapter);
                        }
                    });
                }
            }
        });
    }

    /**
     * 解析服务器数据
     * @param response
     * @return
     */
    private List<IconBean> processIconData(String response){

        List<IconBean> mData=new ArrayList<IconBean>();

        try {
            JSONObject object=new JSONObject(response);
            JSONArray mArray=object.getJSONArray("results");

            for (int i=0;i<mArray.length();i++){
                JSONObject obj=mArray.getJSONObject(i);

                IconBean iconBean=new IconBean();
                iconBean.setIconUri(obj.getString("url"));
                iconBean.setIconDes(obj.getString("desc"));

                mData.add(iconBean);
            }

            return mData;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 加载更多数据
     * @param page
     * @param count
     */
    private void getMoreData(int page, int count){

        OkHttpClient mOkHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url("http://gank.io/api/search/query/listview/category/福利/count/"+count+"/page/"+page).build();
        Call call=mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.changeMoreStatus(IconAdapter.LOAD_MORE_FAIL);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String responseData=response.body().string();

                if (processIconData(responseData)!=null) {
                    List<IconBean> moreData = processIconData(responseData);
                    mData.addAll(moreData);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            adapter.notifyDataSetChanged();
                            adapter.changeMoreStatus(IconAdapter.PULLUP_LOAD_MORE);
                        }
                    });
                }else {
                    adapter.changeMoreStatus(IconAdapter.LOAD_MORE_NONE);
                }
            }
        });
    }

    /**
     * 注册监听事件
     */
    private void initListener(){

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });

        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener(){
            //用来标记是否正在向最后一个滑动，既是否向下滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int[] lastVisiblePositions = manager.findLastVisibleItemPositions(new int[manager.getSpanCount()]);
                    int lastVisiblePos = getMaxElem(lastVisiblePositions);
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部
                    if (lastVisiblePos == (totalItemCount -1) && isSlidingToLast && adapter.getLoadMoreStatus()!=IconAdapter.LOADING_MORE) {

                        //加载更多功能的代码
                        adapter.changeMoreStatus(IconAdapter.LOADING_MORE);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                currentPage++;
                                getMoreData(currentPage,10);
                            }
                        }).start();


                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if(dy > 0){
                    //大于0表示，正在向下滚动
                    isSlidingToLast = true;
                }else{
                    //小于等于0 表示停止或向上滚动
                    isSlidingToLast = false;
                }

            }
        });


    }

    private int getMaxElem(int[] arr) {
        int size = arr.length;
        int maxVal = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            if (arr[i]>maxVal)
                maxVal = arr[i];
        }
        return maxVal;
    }
}
