package me.leefeng.rxjava.down;

import android.content.res.Configuration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;
import com.superplayer.library.SuperPlayer;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.download.dbcontrol.FileHelper;
import me.leefeng.rxjava.main.Course;
import me.leefeng.rxjava.newplay.PlayerItemBean;
import me.leefeng.rxjava.newplay.PlayerListAdapter;

/**
 * Created by limxing on 2016/11/4.
 */

public class DownCourseActivity extends BeidaSwipeActivity {
    private PlayerListAdapter mAdapter;
    private RecyclerView newplayer_recycleview;
    private int current = 0;
    private SuperPlayer player;
    private List<PlayerItemBean> playerItemBeanList;
    private File file;
    private String couseId;

    @Override
    protected void initView() {
        couseId = getIntent().getStringExtra("id");
        List<String> catelogue = new ArrayList<>();

        for (Course course : BeidaApplication.cList) {
            if (course.getId().equals(couseId)) {
                catelogue.addAll(course.getCatelogue());
//                for (String s : course.getCatelogue()) {
//                    catelogue.add(new String(s));
//                }
                break;
            }
        }
        playerItemBeanList = new ArrayList<>();


        findViewById(R.id.fab).setVisibility(View.GONE);
        findViewById(R.id.newplayer_down_bottom).setVisibility(View.GONE);
        findViewById(R.id.newplay_title).setVisibility(View.GONE);
        player = (SuperPlayer) findViewById(R.id.newplay_player);
        player.measure(0, 0);
        newplayer_recycleview = (RecyclerView) findViewById(R.id.newplayer_recycleview);
        RelativeLayout.LayoutParams recycleParams = (RelativeLayout.LayoutParams) newplayer_recycleview.getLayoutParams();
        recycleParams.setMargins(0, player.getMeasuredHeight(), 0, 0);
        newplayer_recycleview.setLayoutParams(recycleParams);


        mAdapter = new PlayerListAdapter(catelogue, playerItemBeanList);
        newplayer_recycleview.setAdapter(mAdapter);
        newplayer_recycleview.setLayoutManager(new LinearLayoutManager(this));
        newplayer_recycleview.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
        initListData();
        initPlayer();
        mAdapter.setOnItemClickListener(new PlayerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if (current == position) {
                    ToastUtils.showLong(view.getContext(), "正在播放");
                    return;
                }
                playerItemBeanList.get(current).setPlaying(false);
                current = position;
                playerItemBeanList.get(position).setPlaying(true);
                mAdapter.notifyDataSetChanged();

                player.stop();
                player.setTitle(playerItemBeanList.get(current).getName());
                player.play(playerItemBeanList.get(current).getPath());
            }
        });

    }

    @Override
    protected int getView() {
        return R.layout.activity_newplay;
    }

    private void initPlayer() {
        current = SharedPreferencesUtil.getIntData(this, couseId + "_current", 0);
        int position = SharedPreferencesUtil.getIntData(this, couseId + "_play", 0);
        player.setNetChangeListener(true)//设置监听手机网络的变化
//                .setOnNetChangeListener(this)//实现网络变化的回调
                .setShowNavIcon(true)
                .showCenterControl(true)
                .setCompleteToSmall(false)
                .setOrientationChangeListener(false)
                .onPrepared(new SuperPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared() {
                        /**
                         * 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
                         */
                    }
                }).onComplete(new Runnable() {
            @Override
            public void run() {
                /**
                 * 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
                 */
                playerItemBeanList.get(current).setPlaying(false);
                current++;
                if (current < playerItemBeanList.size()) {
                    playerItemBeanList.get(current).setPlaying(true);
                    player.setTitle(playerItemBeanList.get(current).getName());
                    player.play(playerItemBeanList.get(current).getPath());
                } else {
                    player.toggleFullScreen();
                }
                mAdapter.notifyDataSetChanged();
            }
        }).onInfo(new SuperPlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                /**
                 * 监听视频的相关信息。
                 */

            }
        }).onError(new SuperPlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
                /**
                 * 监听视频播放失败的回调
                 */

            }
        }).setTitle(playerItemBeanList.get(current).getName())//设置视频的titleName
                .setCoverImage(R.drawable.default_player);
        player.setUrl(playerItemBeanList.get(current).getPath());
        player.seekTo(position, true)
                .play(500);
        playerItemBeanList.get(current).setPlaying(true);
        player.setScaleType(SuperPlayer.SCALETYPE_FITPARENT);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置

        newplayer_recycleview.scrollToPosition(current - 1);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtil.saveIntData(this, couseId + "_current", current);
        if (player != null) {
            SharedPreferencesUtil.saveIntData(this, couseId + "_play", player.getCurrentPosition());
            player.onDestroy();

        }
    }

    @Override
    protected void doReceiver(String action) {
        if (action.equals("me.leefeng.down")) {
            initListData();
        }
    }

    private void initListData() {
        playerItemBeanList.clear();
        file = new File(FileHelper.getFileDefaultPath(), couseId);
        File[] files = file.listFiles();
        for (File f : files) {
            File[] items = f.listFiles();
            for (File item : items) {
                playerItemBeanList.add(new PlayerItemBean(item.getName(), item.getAbsolutePath()));
            }
        }
        Collections.sort(playerItemBeanList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true);
            newplayer_recycleview.setVisibility(View.VISIBLE);
        } else {
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
            newplayer_recycleview.setVisibility(View.GONE);
        }
    }


}
