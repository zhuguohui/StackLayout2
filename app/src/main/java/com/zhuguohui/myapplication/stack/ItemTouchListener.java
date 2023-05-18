package com.zhuguohui.myapplication.stack;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Scroller;

import androidx.core.view.GestureDetectorCompat;

import com.zhuguohui.myapplication.TRSFunction;
import com.zhuguohui.myapplication.TRSFunction4;

/**
 * <pre>
 * Created by zhuguohui
 * Date: 2023/5/18
 * Time: 17:10
 * Desc:
 * </pre>
 */
class ItemTouchListener extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private GestureDetectorCompat mDetector;
    private View view;
    private Scroller scroller;
    private int pw, ph;

    private TRSFunction<View, Void> moveViewCallBack;
    //向下滑动监听
    private TRSFunction4<MotionEvent, MotionEvent, Float, Float, Void> moveDownCallBack;

    public ItemTouchListener(Context context, int pw, int ph,
                             TRSFunction<View, Void> moveViewCallBack,
                             TRSFunction4<MotionEvent, MotionEvent, Float, Float, Void> moveDownCallBack) {
        mDetector = new GestureDetectorCompat(context, this);
        scroller = new Scroller(context);
        this.pw = pw;
        this.ph = ph;
        this.moveViewCallBack = moveViewCallBack;
        this.moveDownCallBack = moveDownCallBack;
    }

    private boolean callFling;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.view = v;

        boolean handle = mDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!callFling) {
                onFling(null, null, 0, 0);
            }
        }
        return handle;
    }

    float lastX;
    float lastY;

    enum Direction {
        Left, Top, Right, Bottom;

        public boolean isHorizontal() {
            return this == Left || this == Right;
        }
    }

    Direction initDirection = null;

    @Override
    public boolean onDown(MotionEvent e) {
        //必须返回ture。否则其他事件无法感知
        lastX = e.getRawX();
        lastY = e.getRawY();
        initDirection = null;
        callFling = false;
        return true;
    }


    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        float mx = e2.getRawX() - lastX;
        float my = e2.getRawY() - lastY;
        lastX = e2.getRawX();
        lastY = e2.getRawY();
        //将变化约束在水平和垂直两个方向
        if (initDirection == null) {
            if (Math.abs(mx) > Math.abs(my)) {
                initDirection = mx > 0 ? Direction.Right : Direction.Left;
            } else {
                initDirection = my > 0 ? Direction.Bottom : Direction.Top;
            }
        }
        if (initDirection.isHorizontal()) {
            my = 0;
        } else {
            mx = 0;
        }
        if (initDirection == Direction.Bottom) {
            if (moveDownCallBack != null) {
                moveDownCallBack.call(e1, e2, distanceX, distanceY);
            }
            return true;
        }
        float translationX = view.getTranslationX();
        view.setTranslationX(translationX + mx);

        float translationY = view.getTranslationY();
        translationY += my;
        //限制向下移动
        translationY = Math.min(0, translationY);
        view.setTranslationY(translationY);
        setViewAlphaByTranslation();
        return false;
    }

    private void setViewAlphaByTranslation() {
        float tx = Math.abs(view.getTranslationX());
        float ty = Math.abs(view.getTranslationY());

        float translation = Math.max(tx, ty);
        float size = tx > ty ? view.getWidth() : view.getHeight();
        float offset = translation / size;
        offset = Math.min(1, offset);
        view.setAlpha(1 - offset);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        callFling = true;
        //判断滑动距离，如果超过view的一半宽度就消失，否则就回位
        boolean moveOut = false;
        Direction outDirection = initDirection;
        if (initDirection == null) {
            return true;
        }
        if (initDirection.isHorizontal()) {
            float translationX = view.getTranslationX();
            boolean distanceMath = Math.abs(translationX) > view.getWidth() / 2;
            boolean velocityMatch = Math.abs(velocityX) > 500;
            if (distanceMath || velocityMatch) {
                moveOut = true;
                outDirection = translationX > 0 ? Direction.Right : Direction.Left;

            }
        } else {
            float translationY = view.getTranslationY();
            boolean distanceMath = Math.abs(translationY) > view.getHeight() / 2;
            boolean velocityMatch = Math.abs(velocityY) > 500;
            if (translationY != 0 && (distanceMath || velocityMatch)) {
                moveOut = true;
                outDirection = translationY > 0 ? Direction.Top : Direction.Bottom;
            }
        }
        int from = 0;
        int to = 0;
        int duration = 200;
        boolean changeX = false;
        boolean callMoveOut = moveOut;
        if (moveOut) {
            float velocity = 1;
            switch (outDirection) {
                case Top:
                    from = (int) view.getTranslationY();
                    to = (ph + view.getHeight()) / 2;
                    velocity = Math.abs(velocityY);
                    changeX = false;
                    break;
                case Bottom:
                    from = (int) view.getTranslationY();
                    to = -(ph + view.getHeight()) / 2;
                    velocity = Math.abs(velocityY);
                    changeX = false;
                    break;
                case Right:
                    from = (int) view.getTranslationX();
                    to = (pw + view.getWidth()) / 2;
                    velocity = Math.abs(velocityX);
                    changeX = true;
                    break;
                case Left:
                    from = (int) view.getTranslationX();
                    to = -(pw + view.getWidth()) / 2;
                    velocity = Math.abs(velocityX);
                    changeX = true;
                    break;
            }

            int moveDistance = Math.abs(to - from);
            duration = (int) (moveDistance * 1000 / velocity);

        } else {
            if (outDirection.isHorizontal()) {
                from = (int) view.getTranslationX();
                to = 0;
                changeX = true;
            } else {
                from = (int) view.getTranslationY();
                to = 0;
                changeX = false;
            }
        }

        duration = Math.min(200, duration);
        startAnimation(from, to, changeX, duration, callMoveOut);
        return false;
    }

    private void startAnimation(int from, int to, boolean changeX, int duration, boolean callMoveOut) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(from, to);

        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            Integer translationValue = (Integer) animation.getAnimatedValue();
            if (changeX) {
                view.setTranslationX(translationValue);
            } else {
                view.setTranslationY(translationValue);
            }
            setViewAlphaByTranslation();
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // super.onAnimationEnd(animation);'
                if (moveViewCallBack != null && callMoveOut) {
                    moveViewCallBack.call(view);
                }
            }
        });


        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }
}
