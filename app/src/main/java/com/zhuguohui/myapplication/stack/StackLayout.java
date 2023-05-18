package com.zhuguohui.myapplication.stack;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhuguohui.myapplication.TRSFunction4;

/**
 * Created by zhuguohui
 * Date: 2023/4/20
 * Time: 21:37
 * Desc:
 */
public class StackLayout extends FrameLayout {

    private ItemTouchListener onItemTouchListener;

    public StackLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLayoutParamsForChild();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        onItemTouchListener = new ItemTouchListener(getContext(), getWidth(), getHeight(), v -> {
            removeView(v);
            setLayoutParamsForChild();
            return null;
        }, new TRSFunction4<MotionEvent, MotionEvent, Float, Float, Void>() {
            @Override
            public Void call(MotionEvent motionEvent, MotionEvent motionEvent2, Float aFloat, Float aFloat2) {
                return null;
            }
        });
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (params != null) {
            FrameLayout.LayoutParams params1 = (LayoutParams) params;
            params1.gravity = Gravity.CENTER;
        }

        super.addView(child, index, params);
    }

    private void setLayoutParamsForChild() {
        float scaleStep = 0.05f;
        float translationZStep = dp2px(5);
        float translationXStep = dp2px(15);
        int count = getChildCount();
        View lastView = null;
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            float scale = (float) (1.0 - (count - i - 1) * scaleStep);
            float translationX = (count - i - 1) * translationXStep;
            float translationZ = (i + 1) * translationZStep;
            view.setScaleX(scale);
            view.setScaleY(scale);
            view.setTranslationX(translationX);
            view.setTranslationZ(translationZ);
            view.setOnTouchListener(null);
            lastView = view;
        }
        if (lastView != null) {
            lastView.setOnTouchListener(onItemTouchListener);
        }

    }

    private float dp2px(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT) + 0.5f;
    }


}
