package me.leefeng.rxjava.view;

import android.content.Context;
import android.graphics.Matrix;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.limxing.library.R;
import com.limxing.library.utils.LogUtils;

import java.lang.ref.SoftReference;

/**
 * Created by limxing on 16/1/7.
 */
public class LoadView extends ImageView {
    private MyRunable runnable;
    private int width;
    private int height;
    private Drawable drawable;
    private Matrix max;
    private Runnable run;

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (runnable == null) {
            runnable = new MyRunable(this);
        }
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init() {
        setScaleType(ScaleType.MATRIX);
        drawable = getDrawable();
        if (drawable == null) {
            drawable = getResources().getDrawable(R.drawable.svpload);
            setImageDrawable(drawable);
        }
        measure(0, 0);
        width = getMeasuredWidth() / 2;
        height = getMeasuredHeight() / 2;
        if (runnable == null) {
            runnable = new MyRunable(this);
        }
//        runnable.run();
        max = new Matrix();
        run = new Runnable() {
            public float degrees;

            @Override
            public void run() {
                degrees += 30f;
                max.setRotate(degrees, width, height);
                setImageMatrix(max);
                if (degrees == 360) {
                    degrees = 0;
                }
                postDelayed(run, 80);

            }
        };
        run.run();

    }


    static class MyRunable implements Runnable {
        private boolean flag;
        private SoftReference<LoadView> loadingViewSoftReference;
        private float degrees = 0f;
        private Matrix max;

        public MyRunable(LoadView loadingView) {
            loadingViewSoftReference = new SoftReference<LoadView>(loadingView);
            max = new Matrix();
        }

        @Override
        public void run() {
            if (loadingViewSoftReference.get().runnable != null && max != null) {
                degrees += 30f;
                max.setRotate(degrees, loadingViewSoftReference.get().width, loadingViewSoftReference.get().height);
                loadingViewSoftReference.get().setImageMatrix(max);
                if (degrees == 360) {
                    degrees = 0;
                }
                loadingViewSoftReference.get().postDelayed(loadingViewSoftReference.get().getRun(), 80);
            }
        }

    }

    private Runnable getRun() {
        if (runnable == null) {
            runnable = new MyRunable(this);
        }
        return runnable;
    }
}