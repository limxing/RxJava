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
            loginHuanXin(username);
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
