package com.topnews.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.topnews.android.R;
import com.topnews.android.utils.SwipeLayoutManager;
import com.topnews.android.view.SwipeLayout;

import java.util.List;

/**
 * Created by dell on 2017/4/6.
 */

public class KeepTwoAdapter extends RecyclerView.Adapter<KeepTwoAdapter.ViewHolder>{

    private List<String> mDatas;

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mContent;
        private final SwipeLayout mSwipe;
        private final TextView mDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            mContent = (TextView) itemView.findViewById(R.id.tv_content);
            mSwipe = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
            mDelete= (TextView) itemView.findViewById(R.id.tv_delete);
        }
    }

    public KeepTwoAdapter(List<String> mDatas){

        this.mDatas=mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_keep_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

        holder.mContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeLayoutManager.getInstance().closeCurrentLayout();
            }
        });

        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwipeLayoutManager.getInstance().closeCurrentLayout();
                SwipeLayoutManager.getInstance().clearSwipeLayout();
                holder.mSwipe.setCurrentState(SwipeLayout.STATE_CLOSE);
                mDatas.remove(holder.getAdapterPosition());

                notifyItemRemoved(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String data = mDatas.get(position);
        holder.mContent.setText(data);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

}