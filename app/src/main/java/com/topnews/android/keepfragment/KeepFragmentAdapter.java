package com.topnews.android.keepfragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by dell on 2017/3/28.
 */

public class KeepFragmentAdapter extends FragmentPagerAdapter {

    BaseKeepFragment fragment=null;
    private final String[] mTabNames;

    public KeepFragmentAdapter(FragmentManager fm) {
        super(fm);

        mTabNames = new String[]{"文章","专题","跟帖"};
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:
                fragment=new KeepOneFragment();
                break;

            case 1:
                fragment=new KeepTwoFragment();
                break;

            case 2:
                fragment=new KeepThreeFragment();
                break;

            default:
                break;
        }

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
