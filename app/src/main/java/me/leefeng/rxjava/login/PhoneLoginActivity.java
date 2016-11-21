package me.leefeng.rxjava.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.MainActivity;

/**
 * Created by 李利锋 on 2016/11/6.
 */

public class PhoneLoginActivity extends BeidaSwipeActivity {
    private MaterialEditText phone;
    private MaterialEditText phone_pw;
    private int time;
    private Timer timer;
    private String phoneNum;

    @Override
    protected void initView() {
        timer = new Timer();
        phone = (MaterialEditText) findViewById(R.id.phone);
        phone_pw = (MaterialEditText) findViewById(R.id.phone_pw);
        ((TextView) findViewById(R.id.title_name)).setText("手机号码登录");
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, final Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        //提交验证码成功
                        timer.cancel();
                        registHX(phone.getText().toString().trim());
//                        Intent intent = new Intent(mContext, MainActivity.class);
//                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                svProgressHUD.showInfoWithStatus("获取验证码成功");
                            }});

                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    try {
                        final String message = JSON.parseObject(((Throwable) data).getMessage()).getString("detail");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                svProgressHUD.showErrorWithStatus(message);
                                button.setEnabled(true);
                                button.setText("获取验证码");
                                timer.cancel();
                            }
                        });
                    }catch (Exception e){}

                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected int getView() {
        return R.layout.activity_phonelogin;
    }

    @Override
    protected void doReceiver(String action) {
        if (action.equals("me.leefeng.login")) {
            finish();
        }
    }

    public void phoneLoginButton(View view) {
        closeInput();
        registHX(phone.getText().toString().trim());
//        String pw = phone_pw.getText().toString();
//        if (StringUtils.isEmpty(phoneNum)) {
//            svProgressHUD.showErrorWithStatus("请输入手机号");
//            return;
//        }
//        if (StringUtils.isEmpty(pw)) {
//            svProgressHUD.showErrorWithStatus("请输入验证码");
//            return;
//        }
//        SMSSDK.submitVerificationCode("86", phoneNum, pw.trim());
//        svProgressHUD.showLoading("正在登录");
    }

    private TextView button;

    public void phoneLogin_get(View view) {
        closeInput();
        button = (TextView) view;
        phoneNum = phone.getText().toString();
        if (StringUtils.isEmpty(phoneNum)) {
            svProgressHUD.showErrorWithStatus("请输入手机号");
            return;
        }
        SMSSDK.getVerificationCode("86", phoneNum.trim());
        button.setEnabled(false);
        time = 60;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button.setText(time + "(s)");
                        time--;
                        if (time == 0) {
                            button.setEnabled(true);
                            button.setText("获取验证码");
                            timer.cancel();
                        }
                    }
                });
            }
        }, 100, 1000);
    }

    private void registHX(final String username) {

        EMClient.getInstance().login(username, username, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                goMain(username);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(final int code, String message) {
                Log.d("onError:", "登录聊天服务器失败！" + code);
                if (code == 204) {
                    registUser(username);
                }else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            svProgressHUD.showErrorWithStatus("登录失败请重试"+code);
                        }
                    });
                }
            }
        });

    }

    /**
     * 自动注册
     *
     * @param
     * @param str
     */
    private void registUser(String str) {
        try {
            EMClient.getInstance().createAccount(str, str);//同步方法
        } catch (HyphenateException e) {
            e.printStackTrace();
            LogUtils.i(this, "注册失败：" + e.getErrorCode());
        }
        goMain(str);
    }

    private void goMain(String username) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                svProgressHUD.dismissImmediately();
            }
        });
        SharedPreferencesUtil.saveStringData(mContext, "username", username);
        SharedPreferencesUtil.saveStringData(mContext, "password", username);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isPhone", true);
        startActivity(intent);
    }

}
