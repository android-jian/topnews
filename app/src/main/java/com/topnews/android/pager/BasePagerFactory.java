package com.topnews.android.pager;

import java.util.HashMap;

/**
 * Created by dell on 2017/3/23.
 */

public class BasePagerFactory {

    private static HashMap<Integer, BasePagerFragment> pagerMaps=new HashMap<Integer, BasePagerFragment>();

    /**
     * 创建相应位置的fragment对象
     * @param position
     * @return
     */
    public static BasePagerFragment creatFragment(int position){

        BasePagerFragment fragment=pagerMaps.get(position);
        if(fragment==null){
            switch (position) {
                case 0:
                    fragment=new HomeFragment();
                    break;
                case 1:
                    fragment=new TalkingFragment();
                    break;
                case 2:
                    fragment=new TopicFragment();
                    break;
                case 3:
                    fragment=new SettingFragment();
                    break;

                default:
                    break;
            }
            pagerMaps.put(position, fragment);
        }

        return fragment;
    }
}
