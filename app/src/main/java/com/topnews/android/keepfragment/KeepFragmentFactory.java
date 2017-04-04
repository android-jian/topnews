package com.topnews.android.keepfragment;

/**
 * Created by dell on 2017/4/4.
 */

public class KeepFragmentFactory {

    /**
     * 创建相应位置的fragment对象
     * @param position
     * @return
     */
    public static BaseKeepFragment creatFragment(int position){

        BaseKeepFragment fragment=null;
            switch (position) {
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
}
