package me.leefeng.rxjava;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.NoTitleBar.SystemBarTintManager;
import com.limxing.library.SVProgressHUD.SVProgressHUD;
import com.limxing.library.SwipeBack.SwipeBackActivity;
import com.limxing.library.SwipeBack.SwipeBackLayout;


/**
 * Created by limxing on 2016/10/26.
 */

public abstract class BeidaSwipeActivity extends AppCompatActivity {
    protected SVProgressHUD svProgressHUD;
    protected BeidaSwipeActivity mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        swipebackhelper功能实现
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setClosePercent(0.5f)
                .setSwipeBackEnable(true)
                .setSwipeSensitivity(0.5f)
                .setSwipeRelateEnable(true)
                .setScrimColor(0x000000)
                .setSwipeRelateOffset(300);
//        设置顶部状态栏
        SystemBarTintManager.initSystemBar(this);
        setContentView(getView());
        mContext = this;
        svProgressHUD = new SVProgressHUD(this);
        registerBoradcastReceiver();
        initView();
    }

    protected abstract void initView();

    protected abstract int getView();

    protected void closeInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void back(View view) {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(mContext);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(mContext);
    }


    //    swipebackhelper功能实现
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        SwipeBackHelper.onDestroy(this);
    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
           doReceiver(action);
        }

    };

    protected abstract void doReceiver(String action);

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("me.leefeng.down");
        myIntentFilter.addAction("me.leefeng.login");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }
}
