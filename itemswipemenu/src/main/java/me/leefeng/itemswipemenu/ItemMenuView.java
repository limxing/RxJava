package me.leefeng.itemswipemenu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by limxing on 2016/11/8.
 */

public class ItemMenuView extends RelativeLayout {
    private View contentView;
    private float downx;
    private GestureDetector gestureDetector;
    private float changeX;
    private float width;
    private float currentX;
//    private boolean b;

    public ItemMenuView(Context context, int res) {
        this(context);
        contentView = View.inflate(getContext(), res, null);
        initView();
    }

    public ItemMenuView(Context context, View res) {
        this(context);
        contentView = res;
        initView();
    }

    private void initView() {
        setBackgroundColor(Color.RED);
        width = 100 * getResources().getDisplayMetrics().density;
//        contentView.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        Log.i("item+changeX:", "=ACTION_DOWN=");
//                        downx = event.getRawX();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        float moveX = event.getRawX();
//                        changeX = (moveX - downx) + currentX;
//                        if (changeX > 0) {
//                            contentView.setX(0);
//                            currentX = 0;
//                            return false;
//                        }
//                        Log.i("item+changeX:", changeX + "==");
//                        contentView.setX(changeX);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (changeX > 0) {
//                            return false;
//                        }
//
//                        if (changeX > -width / 2) {
//                            setAnimX(contentView, (int) changeX, 0);
//                            currentX = 0;
//                            b=true;
//                        } else {
//                            setAnimX(contentView, (int) changeX, (int) -width);
//                            currentX = -width;
//                            b=false;
//                        }
////                        changeX = 0;
////                        downx=0;
//                        break;
//                }
//                if (currentX==0) {
//                    return false;
//                }else{
//                    return true;
//                }
////                return gestureDetector.onTouchEvent(event);
//            }
//        });
        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i("item+changeX:", "onScroll" + distanceX);
                changeX += distanceX;
//                if (distanceX > 0) {
//                    downx += distanceX;
                contentView.setX(-changeX);
//                } else {
//
//                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                downx = e.getX();
                Log.i("item+changeX:", "onDown");
                return true;
            }


            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("item+changeX:", "onFling");
                if (changeX > 0) {
                    setAnimX(contentView, (int) -changeX, (int) -width);
                    changeX = -width;
                } else {
                    setAnimX(contentView, (int) -changeX, 0);
                    changeX = 0;
                }
                return super.onFling(e1, e2, velocityX, velocityY);

            }
        });


        TextView tv = new TextView(getContext());
        tv.setText("删除");
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(Color.RED);
        tv.setTextSize(16);
        tv.setTextColor(Color.WHITE);
        contentView.measure(0, 0);
        LayoutParams layoutParams = new LayoutParams((int) (width), contentView.getMeasuredHeight());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv.setLayoutParams(layoutParams);
        addView(tv);
        addView(contentView);
    }

    public ItemMenuView(Context context) {
        super(context);
    }

    public ItemMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("item+changeX:", "=ACTION_DOWN=");
                downx = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getRawX();
                changeX = (moveX - downx) + currentX;
                if (changeX >= 0) {
                    contentView.setX(0);
                    currentX = 0;
                    return false;
                }
                Log.i("item+changeX:", changeX + "==");
                contentView.setX(changeX);
                break;
            case MotionEvent.ACTION_UP:
                if (changeX > 0) {
                    return false;
                }

                if (changeX > -width / 2) {
                    setAnimX(contentView, (int) changeX, 0);
                    currentX = 0;
//                    b = true;
                } else {
                    setAnimX(contentView, (int) changeX, (int) -width);
                    currentX = -width;
//                    b = false;
                }
//                        changeX = 0;
//                        downx=0;
                break;
            case MotionEvent.ACTION_CANCEL:
                if (changeX > 0) {
                    return false;
                }

                if (changeX > -width / 2) {
                    setAnimX(contentView, (int) changeX, 0);
                    currentX = 0;
//                    b = true;
                } else {
                    setAnimX(contentView, (int) changeX, (int) -width);
                    currentX = -width;
//                    b = false;
                }
                break;
        }
        if (currentX < 0) {
            return true;
        }
//        if (currentX==0) {
//        if (changeX < 0) {
//            return false;
//        }
        return false;

//        }else{
//            return false;
//        }
//        Log.i("item+changeX:", "dispatchTouchEvent");
//            return super.dispatchTouchEvent(event);
//            return !b;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (currentX==0) {
//            return false;
//        }else{
//            return true;
//        }
//        if (changeX==0) {
        if (currentX==0) {
            return true;
        }
        return false;
//        return super.onInterceptTouchEvent(ev);
//        }else{
//            return true;
//        }
    }
        @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("item+changeX:", "onTouchEvent");
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                Log.i("item+changeX:", "=ACTION_DOWN=");
//                downx = event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float moveX = event.getX();
//                changeX += downx - moveX;
//                downx = moveX;
//                Log.i("item+changeX:", changeX + "==");
//                contentView.setX(-changeX*1.2f);
//                break;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return true;
//        return gestureDetector.onTouchEvent(event);
            if (currentX==0) {
                return false;
            }
            return true;
    }

    /**
     * 控件的Y值的动画
     *
     * @param view
     * @param fromY
     * @param toY
     * @param
     */

    public void setAnimX(final View view, int fromY, int toY) {
        ValueAnimator animator = null;
        animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setTarget(view);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
//                recycleParams.setMargins(0, (int) f, 0, 0);
                view.setX(f);
//                view.setLayoutParams(recycleParams);
            }

        });
    }

    public View getContentView() {
        return contentView;
    }
}
