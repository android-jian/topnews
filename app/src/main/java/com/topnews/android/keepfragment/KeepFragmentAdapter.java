package com.topnews.android.keepfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by dell on 2017/3/28.
 */

public class KeepFragmentAdapter extends FragmentPagerAdapter {

    private final String[] mTabNames;

    public KeepFragmentAdapter(FragmentManager fm) {
        super(fm);

        mTabNames = new String[]{"文章","专题","跟帖"};
    }

    @Override
    public Fragment getItem(int position) {

        BaseKeepFragment fragment=KeepFragmentFactory.creatFragment(position);

        return fragment;
    }

    @Override
    public int getCount() {
        return mTabNames.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTabNames[position];
    }
}
