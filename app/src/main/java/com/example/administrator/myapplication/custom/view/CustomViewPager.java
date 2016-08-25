package com.example.administrator.myapplication.custom.view;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.example.administrator.myapplication.R;
/**
 * Created by lqs on 2016/8/25.
 */
public class CustomViewPager extends ViewPager {


    boolean scrollable;
    public CustomViewPager(Context context) {
        super(context);
    }
    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //return super.onInterceptTouchEvent(ev);
        return  false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return  true;
    }
}