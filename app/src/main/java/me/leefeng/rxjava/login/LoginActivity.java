package me.leefeng.rxjava.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.StringUtils;

import java.io.IOException;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by limxing on 2016/10/26.
 */

public class LoginActivity extends BeidaActivity {
    private EditText login_id;
    private EditText login_pw;

    @Override
    protected void initView() {
        login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("登录");
    }

    @Override
    protected int getView() {
        return R.layout.activity_login;

    }

    public void login(View view) {
        closeInput();
        String id = login_id.getText().toString().trim();
        String pw = login_pw.getText().toString().trim();
        if (StringUtils.isEmpty(id)) {
            svProgressHUD.showErrorWithStatus("请填写学号或报名号");
            return;
        }
        if (StringUtils.isEmpty(pw)) {
            svProgressHUD.showErrorWithStatus("请填写密码");
            return;
        }
        svProgressHUD.showLoading("正在登录");

        RequestBody formBody = new FormBody.Builder()
                .add("myID", "616390000002")
                .add("myPW", "woaifeng521")
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
                        svProgressHUD.showErrorWithStatus("登录失败");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String current="";
                final String str = response.body().string();
                int i = str.indexOf("<div class=\"head_wid1\">");
                final String name = str.substring(i + 23, i + 27).trim();
                i = str.indexOf("http://202.152.177.118/zsphoto");
                final String pic = str.substring(i, i + 67);
                i=str.indexOf("报名号</strong>");
                final String bmh=str.substring(i+13,i+28).trim();
                current="学&nbsp;&nbsp;&nbsp;号</strong>：";
                i=str.indexOf(current);
                final String xh=str.substring(i+current.length(),i+current.length()+14).trim();


                LogUtils.i("pic:" + pic+"=bmh:"+bmh);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        svProgressHUD.dismissImmediately();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
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
