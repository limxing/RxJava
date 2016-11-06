package me.leefeng.rxjava.login;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.StringUtils;

import java.io.IOException;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.BeidaSwipeActivity;
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

public class LoginActivity extends BeidaSwipeActivity {
    private EditText login_id;
    private EditText login_pw;
    private String username;
    private String password;


    @Override
    protected void initView() {
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText("登录");
        findViewById(R.id.title_back).setVisibility(View.GONE);

        if (!SharedPreferencesUtil.getStringData(mContext, "username", "").isEmpty()) {
            username = SharedPreferencesUtil.getStringData(mContext, "username", "");
            password = SharedPreferencesUtil.getStringData(mContext, "password", "");
            goLogin();
        }

    }

    @Override
    protected int getView() {
        return R.layout.activity_login;

    }

    @Override
    protected void doReceiver(String action) {
        if (action.equals("me.leefeng.login")){
            finish();
        }
    }

    public void login(View view) {
        closeInput();
        username = login_id.getText().toString().trim();
        password = login_pw.getText().toString().trim();
        if (StringUtils.isEmpty(username)) {
            svProgressHUD.showErrorWithStatus("请填写学号或报名号");
            return;
        }
        if (StringUtils.isEmpty(password)) {
            svProgressHUD.showErrorWithStatus("请填写密码");
            return;
        }
        goLogin();
    }

    private void goLogin() {
        svProgressHUD.showLoading("正在登录");
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
                        svProgressHUD.showErrorWithStatus("登录失败");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                if (str.contains("用户名错误")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            svProgressHUD.showErrorWithStatus("登录失败，用户名错误");
                        }
                    });
                    return;
                } else if (str.contains("密码错误")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            svProgressHUD.showErrorWithStatus("登录失败，密码错误");
                        }
                    });
                    return;
                }
                registHX(str);


            }
        });
    }

    private void registHX(final String str) {

        EMClient.getInstance().login(username, username, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                goBeida(str);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("onError:", "登录聊天服务器失败！" + code);
                if (code == 204) {
                    registUser(str);
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
            EMClient.getInstance().createAccount(username, username);//同步方法

        } catch (HyphenateException e) {
            e.printStackTrace();
            LogUtils.i(this, "注册失败：" + e.getErrorCode());

        }
        goBeida(str);
    }

    private void goBeida(String str) {
        String current = "";
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

        final String xf = str.substring(i, j);
        LogUtils.i("pic:" + pic + "=bmh:" + bmh);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                svProgressHUD.dismissImmediately();
                SharedPreferencesUtil.saveStringData(mContext, "username", username);
                SharedPreferencesUtil.saveStringData(mContext, "password", password);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("pic", pic);
                intent.putExtra("bmh", bmh);
                intent.putExtra("xh", xh);
                intent.putExtra("xf", xf);
                startActivity(intent);
                
            }
        });

    }

    public void phoneLogin(View view) {
        Intent intent = new Intent(this, PhoneLoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendBroadcast(new Intent("me.leefeng.login"));
    }
}
