package me.leefeng.rxjava.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.NetUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.limxing.publicc.alertview.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.Beidadata;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.addperson.AddPersonActivity;
import me.leefeng.rxjava.down.DownActivity;
import me.leefeng.rxjava.main.bean.Version;
import me.leefeng.rxjava.main.chat.ChatFragment;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/26.
 */

public class MainActivity extends BeidaSwipeActivity implements MainView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.title_back)
    ImageView titleBack;
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.tb)
    FrameLayout tb;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.bottom_nav)
    RadioGroup bottomNav;
    @BindView(R.id.main_bottom_home)
    RadioButton mainBottomHome;
    @BindView(R.id.title_right_text)
    TextView titleRightText;
    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private MainPreImp mainPre;
    private ChatFragment chatFragment;
    private boolean isPhone;
    private ContactsFragment contactsFragment;
    private Fragment currentFragment;

    @Override
    protected void initView() {


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getView() {
        return R.layout.activity_main;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fm = getSupportFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (checkedId) {
            case R.id.main_bottom_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                }
                currentFragment = homeFragment;

                titleName.setText("个人信息");
                titleRightImage.setVisibility(View.GONE);
                titleRightText.setVisibility(View.GONE);
                break;
            case R.id.main_bottom_chat:
                if (chatFragment == null) {
                    chatFragment = ChatFragment.getInstance();
                }
                currentFragment = chatFragment;
                titleName.setText("课程讨论");
                titleRightImage.setVisibility(View.GONE);
                titleRightText.setVisibility(View.GONE);
                break;
            case R.id.main_bottom_contacts:
                if (contactsFragment == null) {
                    contactsFragment = ContactsFragment.getInstance();
                }
                currentFragment = contactsFragment;
                titleName.setText("联系人");
                titleRightImage.setVisibility(View.GONE);
                titleRightText.setVisibility(View.VISIBLE);
                break;
            case R.id.main_bottom_video:
                if (videoFragment == null) {
                    videoFragment = VideoFragment.getInstance(this);
                }
                currentFragment = videoFragment;
                titleName.setText("课程学习");
                titleRightImage.setVisibility(View.VISIBLE);
                titleRightText.setVisibility(View.GONE);
                break;
        }
        // 事务提交
        transaction.replace(R.id.tb, currentFragment);
        transaction.commit();
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
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(Beidadata.FILE_DOWN_APK),
                "application/vnd.android.package-archive");
        startActivity(intent);
        finish();
    }

    @Override
    public Context getContext() {
        return getContext();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_right_image:
                Intent intent = new Intent(this, DownActivity.class);
                startActivity(intent);
                break;
            case R.id.title_right_text:
//                intent = new Intent(this, AddPersonActivity.class);
//                startActivity(intent);

                final EditText editText = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("请输入联系人ID")
                        .setIcon(R.mipmap.ic_launcher)
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final String s = editText.getText().toString();
                                Observable.create(new Observable.OnSubscribe<Boolean>() {
                                    @Override
                                    public void call(Subscriber<? super Boolean> subscriber) {
                                        boolean b = false;
                                        try {
                                            EMContactManager.getInstance().addContact(s, "");//需异步处理
                                            b = true;
                                        } catch (EaseMobException e) {
                                            e.printStackTrace();
                                            b = false;
                                        }
                                        subscriber.onNext(b);
                                    }
                                }).subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Action1<Boolean>() {
                                            @Override
                                            public void call(Boolean aBoolean) {
                                                if (aBoolean) {
                                                    svProgressHUD.showSuccessWithStatus("请求成功");
                                                } else {
                                                    svProgressHUD.showErrorWithStatus("添加失败，请重试");
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        String username = SharedPreferencesUtil.getStringData(mContext, "username", "");
        if (username.length() == 11) {
            isPhone = true;
        }
        titleRightImage.setOnClickListener(this);
        titleRightText.setOnClickListener(this);
        findViewById(R.id.title_back).setVisibility(View.GONE);

        bottomNav.setOnCheckedChangeListener(this);
        mainPre = new MainPreImp(this);

        if (isPhone) {
            bottomNav.check(R.id.main_bottom_chat);
            mainBottomHome.setVisibility(View.GONE);
        } else {
            bottomNav.check(R.id.main_bottom_home);
        }
        //注册一个监听连接状态的listener
        EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
        EMContactManager.getInstance().setContactListener(new EMContactListener() {

            @Override
            public void onContactAgreed(String username) {
                //好友请求被同意
                svProgressHUD.showSuccessWithStatus("添加" + username + "成功");
                if (currentFragment == contactsFragment) {
                    contactsFragment.notifyDataChanged();
                }
            }

            @Override
            public void onContactRefused(String username) {
                //好友请求被拒绝
            }

            @Override
            public void onContactAdded(List<String> list) {

            }

            @Override
            public void onContactInvited(final String username, String reason) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMChatManager.getInstance().acceptInvitation(username);//需异步处理
                        } catch (EaseMobException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //收到好友邀请

            }

            @Override
            public void onContactDeleted(List<String> usernameList) {
                //被删除时回调此方法
            }


        });
        EMChat.getInstance().setAppInited();
        titleName.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendBroadcast(new Intent("me.leefeng.login"));
            }
        }, 2000);
    }


    //实现ConnectionListener接口
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
                        // 显示帐号已经被移除
                    } else if (error == EMError.CONNECTION_CONFLICT) {
                        // 显示帐号在其他设备登录
                        ToastUtils.showShort(mContext, "帐号在其他设备登录");
                    } else {
                        if (NetUtils.hasNetwork(MainActivity.this)) {
                            //连接不到聊天服务器
                        } else {
                            //当前网络不可用，请检查网络设置
                        }
                    }
                }
            });
        }
    }
}
