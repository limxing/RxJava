package me.leefeng.rxjava.main;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.limxing.library.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class MainActivity extends BeidaActivity implements BottomNavigationBar.OnTabSelectedListener {
    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private DownloadFragment downloadFragment;
    public static String name;
    public static String pic;
    public static String bmh;
    public static String xh;
    private TextView title_name;

    @Override
    protected void initView() {
        name = getIntent().getStringExtra("name");
        pic = getIntent().getStringExtra("pic");
        bmh = getIntent().getStringExtra("bmh");
        xh = getIntent().getStringExtra("xh");
        ToastUtils.showLong(mContext, "欢迎" + name + "同学");
        title_name = (TextView) findViewById(R.id.title_name);
        findViewById(R.id.title_back).setVisibility(View.GONE);
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
//        bottomNavigationBar
//                .setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar
                .setActiveColor(R.color.bac);
//                .setInActiveColor(R.color.bac)
//                .setBarBackgroundColor("#ECECEC");
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home, "个人信息"))
                .addItem(new BottomNavigationItem(R.drawable.video, "课程学习"))
                .addItem(new BottomNavigationItem(R.drawable.download, "离线视频"))
                .initialise();
        bottomNavigationBar.setTabSelectedListener(this);

        onTabSelected(0);
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
                break;
            case 1:
                if (videoFragment == null) {
                    videoFragment = new VideoFragment();
                }
                transaction.replace(R.id.tb, videoFragment);
                title_name.setText("课程学习");
                break;
            case 2:
                if (downloadFragment == null) {
                    downloadFragment = new DownloadFragment();
                }
                transaction.replace(R.id.tb, downloadFragment);
                title_name.setText("离线视频");
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
}
