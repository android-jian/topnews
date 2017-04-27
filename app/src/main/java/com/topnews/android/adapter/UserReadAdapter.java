package com.topnews.android.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.topnews.android.R;
import com.topnews.android.gson.KeepInfo;
import com.topnews.android.gson.ReadInfo;
import com.topnews.android.ui.KeepDetailActivity;
import com.topnews.android.utils.UIUtils;

import org.litepal.crud.DataSupport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/4/6.
 */

public class UserReadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnDateSelectedListener{

    private static final int TYPE_HEADER=0;              //顶部HeaderView

    private static final int TYPE_SECOND=1;              //日期显示view

    private static final int TYPE_ITEM=2;                //普通Item View

    private static final DateFormat FORMATTER = SimpleDateFormat.getDateInstance();

    private static MaterialCalendarView calendarView;
    private static TextView mDate;

    private List<KeepInfo> mDatas;
    private Context context;

    public UserReadAdapter(Context context){

        this.context=context;
        mDatas=new ArrayList<KeepInfo>();

        SimpleDateFormat formatter =new SimpleDateFormat("yyyyMMdd");
        Date curDate=new Date(System.currentTimeMillis());  //获取当前时间
        String date=formatter.format(curDate);

        loadData(date);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if (viewType==TYPE_HEADER){

            View headerView=LayoutInflater.from(parent.getContext()).inflate(R.layout.read_header,parent,false);
            HeaderViewHolder headerViewHolder=new HeaderViewHolder(headerView);

            calendarView.setOnDateChangedListener(this);

            Calendar instance1 = Calendar.getInstance();
            instance1.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH)-1, 1);
            Calendar instance2 = Calendar.getInstance();
            instance2.set(instance2.get(Calendar.YEAR), instance2.get(Calendar.MONTH), instance2.get(Calendar.DATE));

            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setMaximumDate(instance2.getTime())
                    .commit();

            Calendar calendar=Calendar.getInstance();
            calendarView.setSelectedDate(calendar);

            return headerViewHolder;

        }else if (viewType==TYPE_SECOND){

            View secondView=LayoutInflater.from(parent.getContext()).inflate(R.layout.read_second,parent,false);
            SecondViewHolder secondViewHolder=new SecondViewHolder(secondView);

            mDate.setText(getSelectedDatesString());

            return secondViewHolder;

        }else if (viewType==TYPE_ITEM){

            View itemView=LayoutInflater.from(parent.getContext()).inflate(R.layout.read_item,parent,false);
            final ItemViewHolder itemViewHolder=new ItemViewHolder(itemView);

            itemViewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=itemViewHolder.getAdapterPosition();

                    Intent intent=new Intent(UIUtils.getContext(), KeepDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("keep_info",mDatas.get(position-2));
                    UIUtils.getContext().startActivity(intent);
                }
            });

            itemViewHolder.mTitle.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    int position=itemViewHolder.getAdapterPosition();
                    readDelete(position);

                    return true;
                }
            });

            return itemViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

       if (holder instanceof HeaderViewHolder){

       }else if (holder instanceof SecondViewHolder){

       }else if (holder instanceof ItemViewHolder){

           ItemViewHolder itemViewHolder= (ItemViewHolder) holder;
           itemViewHolder.mTitle.setText(mDatas.get(position-2).getTitle());
       }
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0){

            return TYPE_HEADER;

        }else if(position==1){

            return TYPE_SECOND;

        }else {

            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+2;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);

            calendarView = (MaterialCalendarView) itemView.findViewById(R.id.calendarView);
        }
    }

    public static class SecondViewHolder extends RecyclerView.ViewHolder{

        public SecondViewHolder(View itemView) {
            super(itemView);

            mDate = (TextView) itemView.findViewById(R.id.tv_date);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        private final TextView mTitle;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.tv_read_title);
        }
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        mDate.setText(getSelectedDatesString());

        SimpleDateFormat formatter =new SimpleDateFormat("yyyyMMdd");
        Date curDate=new Date(calendarView.getSelectedDate().getDate().toString());  //获取当前选中时间
        String selectedDate=formatter.format(curDate);

        loadData(selectedDate);
        notifyDataSetChanged();
    }

    public String getSelectedDatesString() {
        CalendarDay date = calendarView.getSelectedDate();
        if (date == null) {
            return "No Selection";
        }
        return FORMATTER.format(date.getDate());
    }

    /**
     * 加载日期对应阅读记录
     * @param date
     */
    private void loadData(String date){

        mDatas.clear();

        List<ReadInfo> mInfos= DataSupport.where("date=?",date).find(ReadInfo.class);

        for (ReadInfo info:mInfos){

            KeepInfo keepInfo=new KeepInfo();
            keepInfo.setTitle(info.getTitle());
            keepInfo.setContentUri(info.getContentUri());
            keepInfo.setState(false);

            mDatas.add(keepInfo);
        }
    }

    /**
     * 删除阅读记录
     */
    private void readDelete(final int position){

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle("删除阅读记录");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String title=mDatas.get(position-2).getTitle();
                DataSupport.deleteAll(ReadInfo.class,"title=?",title);

                mDatas.remove(position-2);
                notifyItemRemoved(position);
            }
        });

        builder.show();
    }
}