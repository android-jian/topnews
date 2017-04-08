package com.topnews.android.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.topnews.android.utils.SwipeLayoutManager;

/**
 * Created by dell on 2017/4/5.
 */

public class SwipeLayout extends FrameLayout{

    private View contentView;     //item内容区域的view
    private View deleteView;      //delete区域的view
    private int deleteWidth;      //delete区域的宽度
    private int deleteHeight;     //delete区域的高度
    private int contentWidth;
    private int contentHeight;
    private ViewDragHelper dragHelper;

    public static final int STATE_CLOSE=0;
    public static final int STATE_OPEN=1;
    private int currentState=STATE_CLOSE;

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    private void init(){
        dragHelper= ViewDragHelper.create(this,callback);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        contentView = getChildAt(0);
        deleteView = getChildAt(1);
    }

    //此方法在measure方法后调用
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        deleteHeight = deleteView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        contentView.layout(0,0,contentWidth,contentHeight);
        deleteView.layout(contentView.getRight(),0,contentView.getRight()+deleteWidth,deleteHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean result=dragHelper.shouldInterceptTouchEvent(ev);

        //如果当前有打开的，则需要直接拦截，交给onTouchEvent处理  打开的swipeLayout和当前layout不是同一个
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)){
            //关闭已经打开的swipeLayout
            SwipeLayoutManager.getInstance().closeCurrentLayout();
            result=true;
        }

        if (currentState==STATE_OPEN){
            requestDisallowInterceptTouchEvent(true);
        }

        return result;
    }

    private float downX,downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //如果当前有打开的，则下面的逻辑不能执行
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)){

            requestDisallowInterceptTouchEvent(true);
            return true;
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX=event.getX();
                downY=event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                //获取x和y方向移动的距离
                float moveX=event.getX();
                float moveY=event.getY();
                float delatX=moveX-downX;
                float delatY=moveY-downY;
                if (Math.abs(delatX)>Math.abs(delatY)){
                    //表示移动是偏水平方向的，请求父控件不要拦截
                    requestDisallowInterceptTouchEvent(true);
                }

                downX=moveX;
                downY=moveY;
                break;

            case MotionEvent.ACTION_UP:
                break;
        }

        dragHelper.processTouchEvent(event);
        return true;
    }

    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child==contentView || child==deleteView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {

            //限制边界
            if (child==contentView){

                if (left>0) left=0;
                if (left<-deleteWidth) left=-deleteWidth;

            }else if (child==deleteView){

                if(left<contentWidth-deleteWidth) left=contentWidth-deleteWidth;
                if(left>contentWidth) left=contentWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            //将contentView 和deleteView 绑定在一起
            if (changedView==contentView){
                deleteView.layout(deleteView.getLeft()+dx,deleteView.getTop()+dy,
                        deleteView.getRight()+dx,deleteView.getBottom()+dy);
            }else if (changedView==deleteView){
                contentView.layout(contentView.getLeft()+dx,contentView.getTop()+dy,
                        contentView.getRight()+dx,contentView.getBottom()+dy);
            }

            if (contentView.getLeft()==-deleteWidth && currentState!=STATE_OPEN){
                setCurrentState(STATE_OPEN);
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);

            }else if (contentView.getLeft()==0 && currentState!=STATE_CLOSE){
                setCurrentState(STATE_CLOSE);
                SwipeLayoutManager.getInstance().clearSwipeLayout();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);

            if (contentView.getLeft()<-deleteWidth/2){
                //打开
                open();

            }else {
                //关闭
                close();
            }
        }
    };

    /**
     * 打开
     */
    public void open(){
        dragHelper.smoothSlideViewTo(contentView,-deleteWidth,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    /**
     * 关闭
     */
    public void close(){
        dragHelper.smoothSlideViewTo(contentView,0,contentView.getTop());
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

}
