package me.leefeng.rxjava.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.limxing.publicc.alertview.OnItemClickListener;

import me.leefeng.rxjava.Beidadata;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.down.DownActivity;
import me.leefeng.rxjava.main.bean.Version;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/26.
 */

public class MainActivity extends BeidaSwipeActivity implements MainView, BottomNavigationBar.OnTabSelectedListener, View.OnClickListener {

    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    //    private DownloadFragment downloadFragment;
    public static String name;
    public static String pic;
    public static String bmh;
    public static String xh;
    public static String xf;
    private TextView title_name;
    private MainPreImp mainPre;
    private ChatFragment chatFragment;
    private View title_right_image;
    private boolean isPhone;

    @Override
    protected void initView() {
//        设置主界面不能够被滑动
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false);
        SwipeBackHelper.getCurrentPage(this).setDisallowInterceptTouchEvent(true);
        isPhone = getIntent().getBooleanExtra("isPhone", false);
        name = getIntent().getStringExtra("name");
        pic = getIntent().getStringExtra("pic");
        bmh = getIntent().getStringExtra("bmh");
        xh = getIntent().getStringExtra("xh");
        xf = getIntent().getStringExtra("xf");
        if (!isPhone) {
            ToastUtils.showLong(mContext, "欢迎" + name + "同学");
        }
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_image = findViewById(R.id.title_right_image);
        title_right_image.setOnClickListener(this);
        findViewById(R.id.title_back).setVisibility(View.GONE);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .setActiveColor(R.color.bac);
//                .setInActiveColor(R.color.bac)
//                .setBarBackgroundColor("#ECECEC");
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        if (isPhone) {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.chat, "课程讨论"))
                    .addItem(new BottomNavigationItem(R.drawable.video, "课程学习")).initialise();
        } else {
            bottomNavigationBar
                    .addItem(new BottomNavigationItem(R.drawable.home, "个人信息"))
                    .addItem(new BottomNavigationItem(R.drawable.chat, "课程讨论"))
                    .addItem(new BottomNavigationItem(R.drawable.video, "课程学习"))
//                .addItem(new BottomNavigationItem(R.drawable.download, "离线视频"))
                    .initialise();
        }
        bottomNavigationBar.setTabSelectedListener(this);
        onTabSelected(0);
        mainPre = new MainPreImp(this);

        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        if (!SharedPreferencesUtil.getBooleanData(mContext, "isNickname", false)) {
            Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    subscriber.onNext(EMClient.getInstance().updateCurrentUserNick(name));

                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            SharedPreferencesUtil.saveBooleanData(mContext, "isNickname", aBoolean);
                            if (aBoolean.booleanValue()) {
                                LogUtils.i(mContext, EMClient.getInstance().getCurrentUser());

                            }
                        }
                    });
        }

        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                ToastUtils.showShort(mContext, "好友请求被同意:" + username);
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
                ToastUtils.showShort(mContext, "好友请求被拒绝:" + username);
            }

            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                ToastUtils.showShort(mContext, "到好友邀请:" + username + '=' + reason);
            }

            @Override
            public void onContactDeleted(String username) {
                //被删除时回调此方法
                ToastUtils.showShort(mContext, "被删除:" + username);
            }


            @Override
            public void onContactAdded(String username) {
                //增加了联系人时回调此方法
                ToastUtils.showShort(mContext, "增加了联系人:" + username);
            }
        });

        title_name.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBroadcast(new Intent("me.leefeng.login"));
            }
        }, 2000);
    }


    @Override
    protected int getView() {
        return R.layout.activity_main;
    }


    @Override
    public void onTabSelected(int position) {
        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        if (isPhone) {
            position++;
        }
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                transaction.replace(R.id.tb, homeFragment);
                title_name.setText("个人信息");
                title_right_image.setVisibility(View.GONE);
                break;
            case 1:
                if (chatFragment == null) {
                    chatFragment = ChatFragment.getInstance(this);
                }
                transaction.replace(R.id.tb, chatFragment);
                title_name.setText("课程讨论");
                title_right_image.setVisibility(View.GONE);
                break;
            case 2:
                if (videoFragment == null) {
                    videoFragment = VideoFragment.getInstance(this);
                }
                transaction.replace(R.id.tb, videoFragment);
                title_name.setText("课程学习");
                title_right_image.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("limxing", "onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }

    // 设置再按一次退出程序
    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {
        goOut();
    }

    /**
     * 退出确认
     */
    void goOut() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            ToastUtils.showShort(this, "再按一次退出");
            touchTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPre.destory();
        mainPre = null;
        chatFragment.destory();
    }

    @Override
    protected void doReceiver(String action) {

    }

    @Override
    public void showLoading(String s) {
        svProgressHUD.showLoading(s);
    }

    @Override
    public void showInfoWithStatus(String s) {
        svProgressHUD.showInfoWithStatus(s);
    }

    @Override
    public void showErrorWithStatus(String s) {
        svProgressHUD.showErrorWithStatus(s);
    }

    @Override
    public void showSuccessWithStatus(String s) {
        svProgressHUD.showSuccessWithStatus(s);
    }

    @Override
    public void updateDialog(String s, String updateString) {
        new AlertView(s, updateString, "暂不更新", null, new String[]{"开始更新"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    mainPre.upDateCouse();
                }
            }
        }).show();
    }

    @Override
    public void updateCourse() {
        if (videoFragment != null) {
            videoFragment.setMainView(this);
        }
    }

    @Override
    public void updateApp(final Version version) {
        new AlertView(version.getTitle(), version.getValue(), "暂不更新", null, new String[]{"开始更新"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    mainPre.updateAppThread(version);
                }
            }
        }).show();
    }

    @Override
    public void openApk() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(Beidadata.FILE_DOWN_APK),
                "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return getContext();
    }

    /**
     * 缓存按钮
     *
     * @param view
     */
    public void toDownload(View view) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_image:
                Intent intent = new Intent(this, DownActivity.class);
                startActivity(intent);
                break;
        }
    }


    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        ToastUtils.showLong(mContext, "帐号已经被移除");
                        // 显示帐号已经被移除
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登录
                        ToastUtils.showLong(mContext, "帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(mContext)) {
                            ToastUtils.showLong(mContext, "连接不到聊天服务器");
                        }
                        //连接不到聊天服务器
                        else {
                            ToastUtils.showLong(mContext, "当前网络不可用，请检查网络设置");
                        }
                        //当前网络不可用，请检查网络设置
                    }
                }
            });
        }
    }
}
