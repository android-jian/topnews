package com.topnews.android.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;

import com.topnews.android.R;
import com.topnews.android.pager.BasePagerFactory;
import com.topnews.android.pager.BasePagerFragment;
import com.topnews.android.view.NoScrollViewPager;


public class HomeActivity extends AppCompatActivity {

    private NoScrollViewPager vp_content;

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

        MyFragmentAdapter adapter=new MyFragmentAdapter(getSupportFragmentManager());
        vp_content.setAdapter(adapter);
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

    }

    public class MyFragmentAdapter extends FragmentPagerAdapter {

        public MyFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BasePagerFragment fragment= BasePagerFactory.creatFragment(position);

            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

}
