package me.leefeng.rxjava;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.exceptions.EaseMobException;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.Permission.CheckPermListener;
import com.limxing.library.Permission.EasyPermissions;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.security.Permission;
import java.security.Permissions;
import java.util.List;

import me.leefeng.rxjava.login.LoginActivity;
import me.leefeng.rxjava.main.*;
import me.leefeng.rxjava.main.MainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by limxing on 2016/10/27.
 */

public class WelcomeActivity extends BeidaSwipeActivity implements
        EasyPermissions.PermissionCallbacks {

    private static final int RC_PERM = 0;
    private String username;
    private String password;


    @Override
    protected void initView() {
//        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {

                }
                checkPermission(new CheckPermListener() {
                    @Override
                    public void superPermission() {
                        next();
                    }
                }, "需要权限处理", Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
        }.start();
    }

    /**
     * OPENIM if username not null
     */
    private void next() {

        if (!SharedPreferencesUtil.getStringData(mContext, "username", "").isEmpty()) {
            username = SharedPreferencesUtil.getStringData(mContext, "username", "");
            password = SharedPreferencesUtil.getStringData(mContext, "password", "");
            if (username.length() == 11 && username.equals(password)) {
                loginHuanXin(username);
            } else {
                goLogin();
            }
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);

                }
            });
        }
    }

    @Override
    protected int getView() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void doReceiver(String action) {
        if (action.equals("me.leefeng.login")) {
            finish();
        }
    }

    private void goLogin() {
        EMChatManager.getInstance().login(username, username, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                        goBeida();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
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
            // 调用sdk注册方法
            EMChatManager.getInstance().createAccountOnServer(username, username);
            goBeida();
        } catch (final EaseMobException e) {
            //注册失败
            int errorCode = e.getErrorCode();
            if (errorCode == EMError.NONETWORK_ERROR) {
                Toast.makeText(getApplicationContext(), "网络异常，请检查网络！", Toast.LENGTH_SHORT).show();
            } else if (errorCode == EMError.USER_ALREADY_EXISTS) {
                Toast.makeText(getApplicationContext(), "用户已存在！", Toast.LENGTH_SHORT).show();
            } else if (errorCode == EMError.UNAUTHORIZED) {
                Toast.makeText(getApplicationContext(), "注册失败，无权限！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "注册失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
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

                final String xf = str.substring(i, j);

                LogUtils.i("pic:" + pic + "=bmh:" + bmh + "=xf=" + xf);
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

                    }
                });


            }
        });
    }

    @Override
    public void onBackPressed() {


    }


    private void checkPermissionToMain() {

    }


    /**
     * 权限回调接口
     */
    private CheckPermListener mListener;

    /**
     * 检查权限
     *
     * @param listener  全县坚挺
     * @param resString 全县提示
     * @param mPerms    全县内容
     */
    public void checkPermission(CheckPermListener listener, String resString, String... mPerms) {
        mListener = listener;
        if (EasyPermissions.hasPermissions(this, mPerms)) {
            if (mListener != null)
                mListener.superPermission();
        } else {
            EasyPermissions.requestPermissions(this, resString,
                    RC_PERM, mPerms);
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode  请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //同意了全部权限
    @Override
    public void onPermissionsAllGranted() {
        if (mListener != null)
            mListener.superPermission();//同意了全部权限的回调
    }

    //权限被拒绝
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

        EasyPermissions.checkDeniedPermissionsNeverAskAgain(this,
                getString(com.limxing.library.R.string.perm_tip),
                com.limxing.library.R.string.setting, com.limxing.library.R.string.cancel, null, perms);
    }

    //同意了某些权限可能不是全部
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }


    /**
     * 登录环信的方法
     *
     * @param username
     */
    private void loginHuanXin(String username) {
        if (EMChat.getInstance().isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            return;
        }
        EMChatManager.getInstance().login(username, username, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance().loadAllConversations();
                        Log.d("main", "登录聊天服务器成功！");
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("isPhone", true);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
