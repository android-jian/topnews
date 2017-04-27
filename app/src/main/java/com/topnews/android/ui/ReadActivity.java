package com.topnews.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.topnews.android.R;
import com.topnews.android.adapter.UserReadAdapter;
import com.topnews.android.utils.UIUtils;

public class ReadActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerRead;
    private LinearLayoutManager manager;
    private boolean isDateShow=false;          //悬浮日期是否应该显示   默认为false不显示
    private TextView readDate;
    private UserReadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        toolbar = (Toolbar) findViewById(R.id.read_toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("阅读日历");

        mRecyclerRead = (RecyclerView) findViewById(R.id.recycler_read);
        readDate = (TextView) findViewById(R.id.read_date);

        manager = new LinearLayoutManager(UIUtils.getContext());
        mRecyclerRead.setLayoutManager(manager);

        adapter = new UserReadAdapter(this);
        mRecyclerRead.setAdapter(adapter);

        initListener();
    }

    /**
     * 注册监听事件
     */
    private void initListener(){

        mRecyclerRead.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem=manager.findFirstVisibleItemPosition();

                if (firstVisibleItem==1 && !isDateShow && dy>0){

                    readDate.setText(adapter.getSelectedDatesString());
                    readDate.setVisibility(View.VISIBLE);
                    isDateShow=true;
                }

                if (firstVisibleItem==0 && isDateShow && dy<0){

                    readDate.setVisibility(View.INVISIBLE);
                    isDateShow=false;
                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
