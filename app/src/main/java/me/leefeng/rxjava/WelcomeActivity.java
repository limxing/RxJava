package me.leefeng.rxjava;

import android.content.Intent;
import android.widget.TextView;

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
                    goLogin(SharedPreferencesUtil.getStringData(mContext, "username", ""),
                            SharedPreferencesUtil.getStringData(mContext, "password", ""));
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

    private void goLogin(final String id, final String pw) {
        RequestBody formBody = new FormBody.Builder()
                .add("myID", id)
                .add("myPW", pw)
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


                LogUtils.i("pic:" + pic + "=bmh:" + bmh);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        svProgressHUD.dismissImmediately();
                        SharedPreferencesUtil.saveStringData(mContext, "username", id);
                        SharedPreferencesUtil.saveStringData(mContext, "password", pw);
                        Intent intent = new Intent(mContext, me.leefeng.rxjava.main.MainActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("pic", pic);
                        intent.putExtra("bmh", bmh);
                        intent.putExtra("xh", xh);
                        startActivity(intent);
                        finish();
                    }
                });


            }
        });
    }

}
