package com.topnews.android.keepfragment;

import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by dell on 2017/3/28.
 *
 * 用户收藏页-文章
 */

public class KeepOneFragment extends BaseKeepFragment implements View.OnClickListener{

    private View view;

    private List<KeepInfo> mDatas;
    private RecyclerView recyclerView;
    private UserKeepAdapter adapter;

    private boolean isEditable=false;
    private LinearLayout mLinearDelete;
    private ImageView mDeleteIcon;
    private TextView mDeleteText;

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

        View view=View.inflate(UIUtils.getContext(), R.layout.user_keep_one,null);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mLinearDelete = (LinearLayout) view.findViewById(R.id.keep_one_linear);
        mDeleteIcon = (ImageView) view.findViewById(R.id.keep_one_icon);
        mDeleteText = (TextView) view.findViewById(R.id.keep_one_delete);

        mLinearDelete.setOnClickListener(this);

        LinearLayoutManager manager=new LinearLayoutManager(UIUtils.getContext());
        recyclerView.setLayoutManager(manager);

        adapter = new UserKeepAdapter(mDatas,mDeleteIcon,mDeleteText);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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

                    List<KeepInfo> mKeeps=list.get(0).getKeep();

                    if (mKeeps==null){
                        setCurState(DATA_LOAD_NONE);
                    }else if (mKeeps.size()==0){
                        setCurState(DATA_LOAD_NONE);
                    }else {
                        mDatas=mKeeps;
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

                    mLinearDelete.setVisibility(View.GONE);

                }else {
                    adapter.openAnimator(200);
                    item.setTitle("取消");

                    mLinearDelete.setVisibility(View.VISIBLE);
                }

                isEditable=!isEditable;

                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.keep_one_linear:

                List<KeepInfo> mRefreshData=new ArrayList<KeepInfo>();

                for (int i=0;i<mDatas.size();i++){
                    if (!mDatas.get(i).isState()){
                        mRefreshData.add(mDatas.get(i));
                    }
                }

                if (mRefreshData.size()==mDatas.size()){
                    Toast.makeText(UIUtils.getContext(),"亲 请选择删除的条目",Toast.LENGTH_SHORT).show();
                    return;

                }else if (mRefreshData.size()==0){
                    refreshData(mRefreshData);
                    setCurState(DATA_LOAD_NONE);

                }else {

                    refreshData(mRefreshData);
                    mDatas.clear();

                    for (int i=0;i<mRefreshData.size();i++){
                        mDatas.add(mRefreshData.get(i));
                    }

                    adapter.notifyDataSetChanged();

                    mDeleteIcon.setImageResource(R.drawable.keep_delete_nomal);
                    mDeleteText.setText("删除");
                    mDeleteText.setTextColor(Color.GRAY);
                }

                break;

            default:
                break;
        }
    }

    /**
     * 刷新数据操作
     */
    private void refreshData(List<KeepInfo> mDatas){

        MyUser bmobUser=BmobUser.getCurrentUser(MyUser.class);

        if (bmobUser!=null){
            MyUser newUser=new MyUser();
			newUser.setValue("keep",mDatas);

            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {

                    if (e==null){

                        Toast.makeText(UIUtils.getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(UIUtils.getContext(),"删除失败 请重试",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
