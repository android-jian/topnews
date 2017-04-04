package com.topnews.android.keepfragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.topnews.android.R;
import com.topnews.android.adapter.UserKeepAdapter;
import com.topnews.android.gson.KeepInfo;
import com.topnews.android.gson.MyUser;
import com.topnews.android.gson.TopInfo;
import com.topnews.android.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by dell on 2017/3/28.
 *
 * 用户收藏页-文章
 */

public class KeepOneFragment extends BaseKeepFragment {

    private View view;

    private List<KeepInfo> mDatas;
    private RecyclerView recyclerView;
    private UserKeepAdapter adapter;

    private boolean isEditable=false;

    @Override
    public void dataLoad() {

        MyUser myUser= BmobUser.getCurrentUser(MyUser.class);

        if (myUser!=null){
            String name=myUser.getUsername();
            findUserKeep(name);
        }
    }

    @Override
    public View onSuccessView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.user_keep,null);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        LinearLayoutManager manager=new LinearLayoutManager(UIUtils.getContext());
        recyclerView.setLayoutManager(manager);

        adapter = new UserKeepAdapter(mDatas);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * 查询用户收藏
     */
    private void findUserKeep(String name) {
        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
        query.addWhereEqualTo("username", name);
        query.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> list, BmobException e) {

                if (e==null){

                    List<TopInfo> mKeeps=list.get(0).getKeep();

                    if (mKeeps==null){
                        setCurState(DATA_LOAD_NONE);
                    }else {
                        mDatas=protocolData(mKeeps);
                        setCurState(DATA_LOAD_SUCCESS);
                    }
                }else {
                    setCurState(DATA_LOAD_ERROR);
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.keep_one,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.user_edit:

                if (isEditable){
                    adapter.closeAnimator(200);
                    item.setTitle("编辑");

                }else {
                    adapter.openAnimator(200);
                    item.setTitle("取消");
                }

                isEditable=!isEditable;

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 解析数据操作
     * @param mData
     * @return
     */
    private List<KeepInfo> protocolData(List<TopInfo> mData){

        ArrayList<KeepInfo> mInfos=new ArrayList<KeepInfo>();

        try {

            for (int i=0;i<mData.size();i++){
                KeepInfo mKeep=new KeepInfo();
                mKeep.setTitle(mData.get(i).title);
                mKeep.setContentUri(mData.get(i).ContentUrl);
                mKeep.setState(false);

                mInfos.add(mKeep);
            }
            return mInfos;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
