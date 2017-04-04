package com.topnews.android.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topnews.android.R;
import com.topnews.android.gson.KeepInfo;
import com.topnews.android.ui.KeepDetailActivity;
import com.topnews.android.ui.NewsDetailActivity;
import com.topnews.android.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/4/3.
 */

public class UserKeepAdapter extends RecyclerView.Adapter<UserKeepAdapter.ViewHolder>{

    private List<KeepInfo> mDatas;

    private List<ViewHolder> mHolders=new ArrayList<ViewHolder>();

    private static final int ITEM_NORMAL=0;         //正常状态
    private static final int ITEM_EDITABLE=1;       //编辑状态
    private int curState=ITEM_NORMAL;               //默认正常状态

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView mImage;
        private final TextView mContent;

        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_delete);
            mContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    public UserKeepAdapter(List<KeepInfo> mDatas){

        this.mDatas=mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.keep_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        mHolders.add(holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position=holder.getAdapterPosition();

                if (curState==ITEM_NORMAL){
                    Intent intent=new Intent(UIUtils.getContext(), KeepDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("keep_info",mDatas.get(position));
                    UIUtils.getContext().startActivity(intent);

                }else if (curState==ITEM_EDITABLE){

                    mDatas.get(position).setState(!mDatas.get(position).isState());
                    notifyItemChanged(position);
                }

            }
        });

        if (curState==ITEM_NORMAL){

            initAnimator(holder);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mContent.setText(mDatas.get(position).getTitle());

        if(mDatas.get(position).isState()){
            holder.mImage.setImageResource(R.drawable.select_ted);
        }else {
            holder.mImage.setImageResource(R.drawable.select_nomal);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 初始化动画操作
     */
    private void initAnimator( ViewHolder holder){

        ObjectAnimator mItem = ObjectAnimator.ofFloat(holder.itemView, "translationX",0f, -60f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.mImage, "scaleX",1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(holder.mImage, "scaleY",1f, 0f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(mItem).with(scaleX).with(scaleY);
        animSet.setDuration(0);
        animSet.start();
    }

    /**
     * 开启动画 编辑
     */
    public void openAnimator(int time){

        for (int i=0;i<mHolders.size();i++){

            ObjectAnimator mItem = ObjectAnimator.ofFloat(mHolders.get(i).itemView, "translationX",-60f, 0f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mHolders.get(i).mImage, "scaleX",0f, 1f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mHolders.get(i).mImage, "scaleY",0f, 1f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(mItem).with(scaleX).with(scaleY);
            animSet.setDuration(time);
            animSet.start();

            curState=ITEM_EDITABLE;
        }
    }

    /**
     * 关闭动画 正常
     */
    public void closeAnimator(int time){

        for (int i=0;i<mHolders.size();i++){
            ObjectAnimator mItem = ObjectAnimator.ofFloat(mHolders.get(i).itemView, "translationX",0f, -60f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mHolders.get(i).mImage, "scaleX",1f, 0f);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(mHolders.get(i).mImage, "scaleY",1f, 0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.play(mItem).with(scaleX).with(scaleY);
            animSet.setDuration(time);
            animSet.start();

            curState=ITEM_NORMAL;
        }
    }

}
