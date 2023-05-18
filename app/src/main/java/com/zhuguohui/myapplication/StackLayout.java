package com.zhuguohui.myapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

/**
 * Created by zhuguohui
 * Date: 2023/4/20
 * Time: 21:37
 * Desc:
 */
public class StackLayout extends FrameLayout {

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLayoutParamsForChild();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (params != null) {
            FrameLayout.LayoutParams params1 = (LayoutParams) params;
            params1.gravity = Gravity.CENTER;
        }
        child.setOnTouchListener(new OnItemTouchListener(getContext()));
        super.addView(child, index, params);

    }

    private void setLayoutParamsForChild() {
        float scaleStep = 0.05f;
        float translationZStep = dp2px(5);
        float translationXStep = dp2px(15);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            float scale = (float) (1.0 - (count - i - 1) * scaleStep);
            float translationX = (count - i - 1) * translationXStep;
            float translationZ = (i + 1) * translationZStep;
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationX(translationX);
            view.setTranslationZ(translationZ);
        }

    }

    private float dp2px(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT) + 0.5f;
    }

    private static class OnItemTouchListener extends GestureDetector.SimpleOnGestureListener implements OnTouchListener {
        private GestureDetectorCompat mDetector;
        private View view;

        public OnItemTouchListener(Context context) {
            mDetector = new GestureDetectorCompat(context, this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            this.view = v;
            return mDetector.onTouchEvent(event);
        }

        float lastX;
        float lastY;
        enum  Direction{
            horizontal,vertical
        }
        Direction direction=null;

        @Override
        public boolean onDown(MotionEvent e) {
            //必须返回ture。否则其他事件无法感知
            lastX = e.getRawX();
            lastY = e.getRawY();
            direction=null;
            return true;
        }



        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("zzz", "onScroll() called with:  distanceX = [" + distanceX + "], distanceY = [" + distanceY + "]");
            float mx = e2.getRawX() - lastX;
            float my = e2.getRawY() - lastY;
            lastX=e2.getRawX();
            lastY=e2.getRawY();
            //将变化约束在水平和垂直两个方向
            if(direction==null) {
                if (Math.abs(mx) > Math.abs(my)) {
                   direction=Direction.horizontal;
                } else {
                    direction=Direction.vertical;
                }
            }
            if(direction==Direction.horizontal){
                my=0;
            }else{
                mx=0;
            }

            float translationX = view.getTranslationX();
            view.setTranslationX(translationX + mx);
            float translationY = view.getTranslationY();
            view.setTranslationY(translationY + my);
            return false;
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("zzz", "onFling() called with: velocityX = [" + velocityX + "], velocityY = [" + velocityY + "]");
            return false;
        }
    }

}
