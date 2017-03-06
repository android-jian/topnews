package com.topnews.android.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.topnews.android.R;
import com.topnews.android.pager.BasePager;
import com.topnews.android.pager.HomePager;
import com.topnews.android.pager.SettingPager;
import com.topnews.android.pager.TalkingPager;
import com.topnews.android.pager.TopicPager;
import com.topnews.android.view.NoScrollViewPager;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private NoScrollViewPager vp_content;

    private ArrayList<BasePager> mPagers;
    private RadioGroup rg_group;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        rg_group = (RadioGroup) findViewById(R.id.rg_group);
        vp_content = (NoScrollViewPager) findViewById(R.id.vp_content);

        setSupportActionBar(toolbar);

        mPagers=new ArrayList<BasePager>();

        //添加四个标签页
        mPagers.add(new HomePager(getSupportFragmentManager()));
        mPagers.add(new TalkingPager());
        mPagers.add(new TopicPager());
        mPagers.add(new SettingPager());

        vp_content.setAdapter(new ContentAdapter());
        vp_content.setOffscreenPageLimit(3);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.btn_home:
                        vp_content.setCurrentItem(0);
                        break;

                    case R.id.btn_talking:
                        vp_content.setCurrentItem(1);
                        break;

                    case R.id.btn_topic:
                        vp_content.setCurrentItem(2);
                        break;

                    case R.id.btn_setting:
                        vp_content.setCurrentItem(3);
                        break;

                    default:
                        break;
                }
            }
        });

        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //viewpager页面切换的时候调用   进行数据加载操作
            @Override
            public void onPageSelected(int position) {
                mPagers.get(position).initData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //mPagers.get(0).initData();       //手动加载第一页数据
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager mPager=mPagers.get(position);
            View view=mPager.initView();        //获取当前页面对象的布局

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }

}
