package com.topnews.android.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.topnews.android.R;
import com.topnews.android.gson.ReadInfo;
import com.topnews.android.gson.TopInfo;
import com.topnews.android.ui.NewsDetailActivity;
import com.topnews.android.utils.SharPreUtil;
import com.topnews.android.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by dell on 2017/3/5.
 */

public class TopFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int  PULLUP_LOAD_MORE=0;        //上拉加载更多

    public static final int  LOADING_MORE=1;            //玩命加载中

    public static final int  LOAD_MORE_FAIL=2;          //加载失败

    public static final int  LOAD_MORE_NONE=3;          //没有更多数据了

    private int load_more_status=PULLUP_LOAD_MORE;      //默认状态为上拉加载更多

    private static final int TYPE_ITEM = 0;             //普通Item View

    private static final int TYPE_FOOTER = 1;           //底部FootView

    private static final int TYPE_HEADER=2;             //顶部HeaderView

    private List<TopInfo> mDatas;

    private ArrayList<ImageView> mListIcons;

    private int[] mIconIds;

    private int mPreviousPos;           //上一个圆点被选中的位置
    private HeaderViewHolder headerViewHolder;

    public TopFragmentAdapter(List<TopInfo> mDatas){

        this.mDatas=mDatas;

        mIconIds=new int[]{R.drawable.icon_01,R.drawable.icon_02,R.drawable.icon_03,R.drawable.icon_04};

        mListIcons=new ArrayList<ImageView>();

        for (int i=0;i<4;i++){

            ImageView imageView=new ImageView(UIUtils.getContext());
            imageView.setImageResource(mIconIds[i]);
            mListIcons.add(imageView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //进行判断显示类型，来创建返回不同的View
        if (viewType==TYPE_ITEM){

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
            final ItemViewHolder itemViewHolder=new ItemViewHolder(view);

            itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position =itemViewHolder.getAdapterPosition();

                    String readIds= SharPreUtil.getString(UIUtils.getContext(),"readIds","");
                    if (!readIds.contains(mDatas.get(position-1).id)){
                        SharPreUtil.setString(UIUtils.getContext(),"readIds",readIds+mDatas.get(position-1).id+",");
                    }
                    itemViewHolder.tv_title.setTextColor(Color.GRAY);

                    //Toast.makeText(v.getContext(),"你点击了第"+position+"条数据",Toast.LENGTH_SHORT).show();

                    SimpleDateFormat formatter =new SimpleDateFormat("yyyyMMdd");
                    Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                    String date=formatter.format(curDate);

                    //保存阅读记录
                    ReadInfo mInfo=new ReadInfo();
                    mInfo.setDate(date);
                    mInfo.setTitle(mDatas.get(position-1).title);
                    mInfo.setContentUri(mDatas.get(position-1).ContentUrl);
                    mInfo.save();

                    Intent intent=new Intent(UIUtils.getContext(), NewsDetailActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("top_info",mDatas.get(position-1));
                    UIUtils.getContext().startActivity(intent);
                }
            });

            return itemViewHolder;

        }else if (viewType==TYPE_FOOTER){

            View foot_view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_foot,parent,false);

            //这边可以做一些属性设置，甚至事件监听绑定
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
            return footViewHolder;

        }else if (viewType==TYPE_HEADER){

            View header_view=LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_header,parent,false);
            headerViewHolder = new HeaderViewHolder(header_view);

            for (int i=0;i<mListIcons.size();i++){

                ImageView imView=new ImageView(UIUtils.getContext());
                LinearLayout.LayoutParams llp= new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

                if (i==0){
                    imView.setImageResource(R.drawable.point_selected);
                }else{
                    imView.setImageResource(R.drawable.point_nomal);
                    llp.leftMargin=UIUtils.dip2px(4);
                }
                imView.setLayoutParams(llp);
                headerViewHolder.linear_layout_points.addView(imView);
            }

            headerViewHolder.vp_icon_show.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    int pos=position%mListIcons.size();

                    // 将当前圆点设置为选中样式
                    ImageView view= (ImageView) headerViewHolder.linear_layout_points.getChildAt(pos);
                    view.setImageResource(R.drawable.point_selected);

                    if (pos!=mPreviousPos){
                        // 将上一个圆点设置为默认样式
                        ImageView prView= (ImageView) headerViewHolder.linear_layout_points.getChildAt(mPreviousPos);
                        prView.setImageResource(R.drawable.point_nomal);
                    }

                    mPreviousPos=pos;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            new RunnableTask().start();

            return headerViewHolder;

        }

        return null;
    }

    /**
     * 数据的绑定显示
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder){

            Glide.with(UIUtils.getContext()).load(mDatas.get(position-1).imgeUrl).placeholder(R.drawable.icon)
                    .error(R.drawable.load_error).into(((ItemViewHolder) holder).iv_icon);

            ((ItemViewHolder) holder).tv_title.setText(mDatas.get(position-1).title);
            ((ItemViewHolder) holder).tv_source.setText(mDatas.get(position-1).source);

            String readIds=SharPreUtil.getString(UIUtils.getContext(),"readIds","");
            if (readIds.contains(mDatas.get(position-1).id)){
                ((ItemViewHolder) holder).tv_title.setTextColor(Color.GRAY);
            }else {
                ((ItemViewHolder) holder).tv_title.setTextColor(Color.BLACK);
            }

            holder.itemView.setTag(position);

        }else if (holder instanceof FootViewHolder){

            FootViewHolder footViewHolder=(FootViewHolder)holder;
            switch (load_more_status){

                case PULLUP_LOAD_MORE:
                    footViewHolder.tv_foot.setText("上拉加载更多");
                    footViewHolder.progress_bar.setVisibility(View.GONE);
                    break;

                case LOADING_MORE:
                    footViewHolder.tv_foot.setText("玩命加载中...");
                    footViewHolder.progress_bar.setVisibility(View.VISIBLE);
                    break;

                case LOAD_MORE_FAIL:
                    footViewHolder.tv_foot.setText("加载失败 请重试");
                    footViewHolder.progress_bar.setVisibility(View.GONE);
                    break;

                case LOAD_MORE_NONE:
                    footViewHolder.tv_foot.setText("加载完成 没有更多数据了亲");
                    footViewHolder.progress_bar.setVisibility(View.GONE);
                    break;
            }

        }else if (holder instanceof HeaderViewHolder){

            final HeaderViewHolder headerViewHolder= (HeaderViewHolder) holder;

            MyPagerAdapter mAdapter= new MyPagerAdapter(mListIcons);
            headerViewHolder.vp_icon_show.setAdapter(mAdapter);

            headerViewHolder.vp_icon_show.setCurrentItem(mListIcons.size()*10000);

            //holder.itemView.setTag(position);

        }
    }

    @Override
    public int getItemViewType(int position) {

        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;

        }else if (position==0){
            return TYPE_HEADER;

        }else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size()+2;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View itemView;

        ImageView iv_icon;
        TextView tv_title;
        TextView tv_source;

        public ItemViewHolder(View itemView) {
            super(itemView);

            this.itemView=itemView;

            iv_icon= (ImageView) itemView.findViewById(R.id.iv_icon);
            tv_title= (TextView) itemView.findViewById(R.id.tv_title);
            tv_source= (TextView) itemView.findViewById(R.id.tv_source);
        }
    }

    public static class FootViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_foot;
        private ProgressBar progress_bar;

        public FootViewHolder(View itemView) {
            super(itemView);

            tv_foot= (TextView) itemView.findViewById(R.id.tv_foot);
            progress_bar= (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        private ViewPager vp_icon_show;
        private LinearLayout linear_layout_points;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            vp_icon_show= (ViewPager) itemView.findViewById(R.id.vp_icon_show);
            linear_layout_points= (LinearLayout) itemView.findViewById(R.id.linear_layout_points);
        }
    }

    /**
     * 更新加载更多状态
     * @param status
     */
    public void changeMoreStatus(int status){
        load_more_status=status;
        notifyDataSetChanged();
    }

    /**
     * 获取当前加载更多状态
     * @return
     */
    public int getLoadMoreStatus(){

        return load_more_status;
    }


    public class MyPagerAdapter extends PagerAdapter{

        private ArrayList<ImageView> mListIcons;

        public MyPagerAdapter(ArrayList<ImageView> mListIcons){

            this.mListIcons=mListIcons;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            position=position%mListIcons.size();

            ImageView imageView=mListIcons.get(position);

            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }

    /**
     * 自动轮播效果
     */
    class RunnableTask implements Runnable{

        public void start(){

            // 移除之前遗留的任务(handler只有一个,但HomeFragment有可能多次被创建,从而导致消息被重复发送,所以需要先把之前的消息移除掉)
            UIUtils.getHandler().removeCallbacksAndMessages(null);

            //发送延时2秒的任务
            UIUtils.getHandler().postDelayed(this,2000);
        }

        @Override
        public void run() {

            int currentItem=headerViewHolder.vp_icon_show.getCurrentItem();
            currentItem++;
            headerViewHolder.vp_icon_show.setCurrentItem(currentItem);

            // 继续发送延时两秒的任务, 形成闭环, 达到循环执行的效果
            UIUtils.getHandler().postDelayed(this,2000);
        }
    }

}