package com.topnews.android.ui;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.topnews.android.R;
import com.topnews.android.keepfragment.KeepFragmentAdapter;

public class UserKeepActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_keep);

        mTabLayout = (TabLayout) findViewById(R.id.keep_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.keep_view_pager);

        toolbar = (Toolbar) findViewById(R.id.keep_toolBar);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(new KeepFragmentAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.userkeep,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.backup:

                finish();
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
