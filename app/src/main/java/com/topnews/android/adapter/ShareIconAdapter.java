package com.topnews.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.topnews.android.R;
import com.topnews.android.gson.ShareIconInfo;

import java.util.ArrayList;

/**
 * Created by dell on 2017/3/14.
 */

public class ShareIconAdapter extends RecyclerView.Adapter<ShareIconAdapter.ViewHolder>{

    private ArrayList<ShareIconInfo> mDatas;
    private ImageView mImage;
    private EditText et_input;

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_cutoon;
        ImageView iv_select;

        View itemView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.itemView=itemView;

            iv_cutoon=  (ImageView) itemView.findViewById(R.id.iv_cutoon);
            iv_select= (ImageView) itemView.findViewById(R.id.iv_select);
        }
    }

    public ShareIconAdapter(ArrayList<ShareIconInfo> mDatas, ImageView imageView , EditText editText){

        this.mDatas=mDatas;
        this.mImage=imageView;
        this.et_input=editText;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.share_recycler_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (ShareIconInfo iconBean:mDatas) {
                    iconBean.isSelected=false;
                }

                int position=holder.getAdapterPosition();
                mDatas.get(position).isSelected=true;
                notifyDataSetChanged();
                mImage.setImageResource(mDatas.get(position).mIconId);
                et_input.setText(mDatas.get(position).des);

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShareIconInfo mIcon=mDatas.get(position);

        holder.iv_cutoon.setImageResource(mIcon.mIconId);
        holder.iv_select.setImageResource(R.drawable.select);

        if(mIcon.isSelected){
            holder.iv_select.setVisibility(View.VISIBLE);
        }else{
            holder.iv_select.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
