package com.shicimingju.pullscrollview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by chenguoming on 15/7/24.
 */
public class MyScrollView extends ScrollView{
    public static final String TAG = "MyScrollView";
    public float downRawY,lastY,downY;
    private boolean isPulling,refreshAble;
    private float headHeight;
    private onPullToRefreshListener mOnPullToRefreshListener;
    private onRefreshAbleStatusChangedListener onRefreshAbleStatusChangedListener;

    public void setOnRefreshAbleStatusChangedListener(MyScrollView.onRefreshAbleStatusChangedListener onRefreshAbleStatusChangedListener) {
        this.onRefreshAbleStatusChangedListener = onRefreshAbleStatusChangedListener;
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public MyScrollView(Context context) {
        super(context);
        init();
    }
    public onPullToRefreshListener getmOnPullToRefreshListener() {
        return mOnPullToRefreshListener;
    }
    public void init(){
    }
    public void setOnPullToRefreshListener(onPullToRefreshListener mOnPullToRefreshListener) {
        this.mOnPullToRefreshListener = mOnPullToRefreshListener;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                onDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "DY" + downRawY);
                Log.d(TAG,"getRawY"+event.getRawY());
                Log.d(TAG,"getScrollY"+getScrollY());
                if(event.getRawY() - downRawY>0 && getScrollY() == 0){
                    isPulling = true;
                    Log.d(TAG,"intercept_ACTION_MOVE_RETURN:"+"TRUE");
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isPulling()){
                    isPulling = false;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }
    public boolean isPulling(){
        return isPulling;
    }
    public void onDown(MotionEvent event){
        if(getScrollY() == 0){
            lastY =  downRawY = event.getRawY();
            downY = getY();
        }
        Log.d(TAG, "ACTION_DOWN:" + event.getRawY());
    }
    public void onPullMove(MotionEvent event){
        getChildAt(0).setPadding(0,-getHeadHeight ()+(int) ((event.getRawY() - downRawY) * 0.5), 0, 0);
        Log.d(TAG, "ACTION_MOVE:" + event.getRawY());
        lastY = event.getRawY();
        if((event.getRawY() - downRawY) * 0.5 > getHeadHeight()){
            if(!refreshAble){
                if(onRefreshAbleStatusChangedListener!=null){
                    onRefreshAbleStatusChangedListener.onRefreshAble(true);
                }
            }
                refreshAble = true;
        }else{
            if(refreshAble){
                if(onRefreshAbleStatusChangedListener!=null){
                    onRefreshAbleStatusChangedListener.onRefreshAble(false);
                }
            }
                refreshAble=false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(((ViewGroup)getChildAt(0)).getChildAt(0).getPaddingTop() == 0&&!isPulling()){
            getChildAt(0).setPadding(0, -getHeadHeight(), 0, 0);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onDown(event);
                    return true;
                case MotionEvent.ACTION_MOVE:
                    if(isPulling()){
                        onPullMove(event);
                        return true;
                    }else{
                        if(getScrollY() == 0 && event.getRawY() - downRawY>0){
                            isPulling = true;
                            onPullMove(event);
                            return true;
                        }
                    }
                case MotionEvent.ACTION_UP:
                    Log.d(TAG, "ACTION_UP:" + event.getRawY());
                    if (isPulling) {
                        getChildAt(0).setPadding(0,-getHeadHeight(),0,0);
                        isPulling = false;
                        if(refreshAble){
                            if(mOnPullToRefreshListener!=null){
                                mOnPullToRefreshListener.onPullToRefresh();
                            }
                        }
                        return true;
                    }
            }
        return super.onTouchEvent(event);
    }
    public interface onPullToRefreshListener{
        void onPullToRefresh();
    }
    public interface onRefreshAbleStatusChangedListener{
        void onRefreshAble(boolean refreshAble);
    }
    private int getHeadHeight(){
        return ((ViewGroup)getChildAt(0)).getChildAt(0).getMeasuredHeight();
    }
}
