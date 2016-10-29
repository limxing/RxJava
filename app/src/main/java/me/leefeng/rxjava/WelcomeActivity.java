package me.leefeng.rxjava;

import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;

import java.io.IOException;

import me.leefeng.rxjava.login.*;
import me.leefeng.rxjava.login.LoginActivity;
import me.leefeng.rxjava.main.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by limxing on 2016/10/27.
 */

public class WelcomeActivity extends BeidaActivity {

    private String username;
    private String password;

    @Override
    protected void initView() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {

                }
                if (!SharedPreferencesUtil.getStringData(mContext, "username", "").isEmpty()) {
                    username = SharedPreferencesUtil.getStringData(mContext, "username", "");
                    password = SharedPreferencesUtil.getStringData(mContext, "password", "");
                    goLogin();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    protected int getView() {
        return R.layout.activity_welcome;
    }

    private void goLogin() {
        EMClient.getInstance().login(username, username, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                goBeida();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("onError:", "登录聊天服务器失败！" + code);
                if (code == 204) {
                    registUser();
                }
            }
        });

    }

    /**
     * 自动注册
     *
     * @param
     */
    private void registUser() {
        try {
            EMClient.getInstance().createAccount(username, username);//同步方法
            goBeida();
        } catch (HyphenateException e) {
            e.printStackTrace();
            LogUtils.i(this, "注册失败：" + e.getErrorCode());
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void goBeida() {
        RequestBody formBody = new FormBody.Builder()
                .add("myID", username)
                .add("myPW", password)
                .add("usersf", "1")
                .build();

        Call call = BeidaApplication.okHttpClient.newCall(new Request.Builder()
                .url("http://learn.pkudl.cn")
                .post(formBody)
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String current = "";
                final String str = response.body().string();
                int i = str.indexOf("<div class=\"head_wid1\">");
                final String name = str.substring(i + 23, i + 27).trim();
                i = str.indexOf("http://202.152.177.118/zsphoto");
                final String pic = str.substring(i, i + 67);
                i = str.indexOf("报名号</strong>");
                final String bmh = str.substring(i + 13, i + 28).trim();
                current = "学&nbsp;&nbsp;&nbsp;号</strong>：";
                i = str.indexOf(current);
                final String xh = str.substring(i + current.length(), i + current.length() + 14).trim();
                i = str.indexOf("已经获得");
                int j = str.indexOf("</td>", i);

                final String xf=str.substring(i,j);

                LogUtils.i("pic:" + pic + "=bmh:" + bmh+"=xf="+xf);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        svProgressHUD.dismissImmediately();
//                        SharedPreferencesUtil.saveStringData(mContext, "username", id);
//                        SharedPreferencesUtil.saveStringData(mContext, "password", pw);
                        Intent intent = new Intent(mContext, me.leefeng.rxjava.main.MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("pic", pic);
                        intent.putExtra("bmh", bmh);
                        intent.putExtra("xh", xh);
                        intent.putExtra("xf", xf);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });
    }

    @Override
    public void onBackPressed() {


    }
}
