package com.topnews.android.fragment;

import java.util.HashMap;

/**
 * Created by dell on 2017/2/25.
 */

public class FragmentFactory {

    private static HashMap<Integer, BaseFragment> maps=new HashMap<Integer, BaseFragment>();
    /**
     * 创建相应位置的fragment对象
     * @param position
     * @return
     */
    public static BaseFragment creatFragment(int position){

        BaseFragment fragment=maps.get(position);
        if(fragment==null){
            switch (position) {
                case 0:
                    fragment=new TopFragment();
                    break;
                case 1:
                    fragment=new ImporNewsFragment();
                    break;
                case 2:
                    fragment=new PlayFragment();
                    break;
                case 3:
                    fragment=new SportFragment();
                    break;
                case 4:
                    fragment=new MoneyFragment();
                    break;
                case 5:
                    fragment=new TechFragment();
                    break;
                case 6:
                    fragment=new GameFragment();
                    break;

                default:
                    break;
            }
            maps.put(position, fragment);
        }

        return fragment;
    }
}

