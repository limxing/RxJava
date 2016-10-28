package me.leefeng.rxjava.main;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.limxing.library.utils.IOUtils;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.limxing.publicc.alertview.OnItemClickListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.Subject;

/**
 * Created by limxing on 2016/10/26.
 */

public class MainActivity extends BeidaActivity implements MainView, BottomNavigationBar.OnTabSelectedListener {

    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    //    private DownloadFragment downloadFragment;
    public static String name;
    public static String pic;
    public static String bmh;
    public static String xh;
    private TextView title_name;
    private MainPreImp mainPre;
    private ChatFragment chatFragment;
    private View title_right_image;

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        pic = getIntent().getStringExtra("pic");
        bmh = getIntent().getStringExtra("bmh");
        xh = getIntent().getStringExtra("xh");
        ToastUtils.showLong(mContext, "欢迎" + name + "同学");
        title_name = (TextView) findViewById(R.id.title_name);
        title_right_image= findViewById(R.id.title_right_image);
        findViewById(R.id.title_back).setVisibility(View.GONE);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .setActiveColor(R.color.bac);
//                .setInActiveColor(R.color.bac)
//                .setBarBackgroundColor("#ECECEC");
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home, "个人信息"))
                .addItem(new BottomNavigationItem(R.drawable.chat, "沟通"))
                .addItem(new BottomNavigationItem(R.drawable.video, "课程学习"))
//                .addItem(new BottomNavigationItem(R.drawable.download, "离线视频"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);

        onTabSelected(0);
        mainPre = new MainPreImp(this);

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
        switch (position) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
//                    .newInstance("位置");
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
                title_name.setText("沟通");
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
//            case 3:
//                if (downloadFragment == null) {
//                    downloadFragment = new DownloadFragment();
//                }
//                transaction.replace(R.id.tb, downloadFragment);
//                title_name.setText("离线视频");
//                break;
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
    public void updateDialog(String s) {
        new AlertView(s, "更新后获取最新的视频课程", "暂不更新", null, new String[]{"开始更新"}, mContext, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    mainPre.upDateCouse();
                }
            }
        }).show();
    }

    /**
     * 缓存按钮
     * @param view
     */
    public void toDownload(View view) {

    }
}
