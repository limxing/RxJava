package me.leefeng.rxjava.down;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Iterator;
import java.util.List;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.download.TaskInfo;
import me.leefeng.rxjava.download.dbcontrol.FileHelper;
import me.leefeng.rxjava.main.Course;
import me.leefeng.rxjava.newplay.PlayerItemBean;
import me.leefeng.rxjava.newplay.PlayerListAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by limxing on 2016/11/4.
 */

public class DownCourseActivity extends BeidaSwipeActivity implements View.OnClickListener {
    private PlayerListAdapter mAdapter;
    private RecyclerView newplayer_recycleview;
    private int current = 0;
    private SuperPlayer player;
    private List<PlayerItemBean> playerItemBeanList;
    private File file;
    private String couseId;
    private int titleHeight;
    private RelativeLayout.LayoutParams recycleParams;
    private int down_bottom_height;
    private boolean isDownloadController;

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

        initTopView();

//        player = (SuperPlayer) findViewById(R.id.newplay_player);
//        player.measure(0, 0);
//        newplayer_recycleview = (RecyclerView) findViewById(R.id.newplayer_recycleview);
//        RelativeLayout.LayoutParams recycleParams = (RelativeLayout.LayoutParams) newplayer_recycleview.getLayoutParams();
//        recycleParams.setMargins(0, player.getMeasuredHeight(), 0, 0);
//        newplayer_recycleview.setLayoutParams(recycleParams);


        mAdapter = new PlayerListAdapter(catelogue, playerItemBeanList);
        mAdapter.setDown(true);
        newplayer_recycleview.setAdapter(mAdapter);
        newplayer_recycleview.setLayoutManager(new LinearLayoutManager(this));
        newplayer_recycleview.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));

        initListData();
        initPlayer();
        mAdapter.setOnItemClickListener(new PlayerListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isDownloadController) {
                    if (playerItemBeanList.get(position).isChecked()) {
                        playerItemBeanList.get(position).setChecked(false);
                    } else
                        playerItemBeanList.get(position).setChecked(true);
                    mAdapter.notifyItemChanged(position);
                    return;
                }

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

    private View newplay_title;
    private View newplayer_down_bottom;
    private TextView newplayer_all;
    private FloatingActionButton fab;

    /**
     * 初始化头部
     */
    private void initTopView() {
        newplay_title = findViewById(R.id.newplay_title);
        newplay_title.measure(0, 0);
        titleHeight = newplay_title.getMeasuredHeight();
        newplay_title.setY(-titleHeight);
        ((TextView) findViewById(R.id.title_name)).setText("选择删除");
        ((ImageView) findViewById(R.id.title_back)).setImageDrawable(getResources().getDrawable(R.drawable.close));
        findViewById(R.id.title_back).setOnClickListener(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.manager);
        fab.setOnClickListener(this);
//        findViewById(R.id.newplayer_down_bottom).setVisibility(View.GONE);
//        findViewById(R.id.newplay_title).setVisibility(View.GONE);

        player = (SuperPlayer) findViewById(R.id.newplay_player);
        newplayer_recycleview = (RecyclerView) findViewById(R.id.newplayer_recycleview);
        player.measure(0, 0);

        recycleParams = (RelativeLayout.LayoutParams) newplayer_recycleview.getLayoutParams();
        recycleParams.setMargins(0, player.getMeasuredHeight(), 0, 0);
        newplayer_recycleview.setLayoutParams(recycleParams);

        newplayer_down_bottom = findViewById(R.id.newplayer_down_bottom);
        newplayer_down_bottom.measure(0, 0);
        down_bottom_height = newplayer_down_bottom.getMeasuredHeight();
        newplayer_down_bottom.setTranslationY(down_bottom_height);

        findViewById(R.id.newplayer_down).setOnClickListener(this);
        newplayer_all = (TextView) findViewById(R.id.newplayer_all);
        newplayer_all.setOnClickListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                translationY(view, 0, 300);
                setAnimY(newplayer_recycleview, player.getMeasuredHeight(), titleHeight);
                padingY(newplayer_recycleview, 0, down_bottom_height);
                translationY(newplay_title, -titleHeight, 0);
                translationY(newplayer_down_bottom, down_bottom_height, 0);
                isDownloadController = true;
                player.onPause();
                mAdapter.setDownloadController(true);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.title_back:
                setAnimY(newplayer_recycleview, titleHeight, player.getMeasuredHeight());
                padingY(newplayer_recycleview, down_bottom_height, 0);
                translationY(newplay_title, 0, -titleHeight);
                translationY(newplayer_down_bottom, 0, down_bottom_height);
                translationY(fab, 300, 0);
                view.bringToFront();
                isDownloadController = false;
                player.onResume();
                mAdapter.setDownloadController(false);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.newplayer_down:
                findViewById(R.id.title_back).callOnClick();
                Observable.create(new Observable.OnSubscribe<Integer>() {
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        int i = 0;
                        Iterator<PlayerItemBean> iterator = playerItemBeanList.iterator();
                        while (iterator.hasNext()) {
                            PlayerItemBean playerItemBean = iterator.next();
                            if (playerItemBean.isChecked()) {
                                new File(playerItemBean.getPath()).delete();
                                iterator.remove();
                                playerItemBeanList.remove(playerItemBean);
                                i++;
                            }
                        }
                        subscriber.onNext(i);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Integer>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Integer integer) {
                                if (integer > 0) {
                                    ToastUtils.showLong(mContext, "删除" + integer + "个视频");
                                    if (playerItemBeanList.size() == 0) {
                                        sendBroadcast(new Intent("me.leefeng.down"));
                                        onBackPressed();
                                    }
                                } else {
                                    ToastUtils.showLong(mContext, "没有删除视频");
                                }
                            }
                        });
                break;
            case R.id.newplayer_all:

                for (PlayerItemBean bean : playerItemBeanList) {
                    bean.setChecked(newplayer_all.getText().toString().equals("全选"));
                }
                mAdapter.notifyDataSetChanged();
                if (newplayer_all.getText().toString().equals("全选")) {
                    newplayer_all.setText("全不选");
                } else {
                    newplayer_all.setText("全选");
                }
                break;
        }
    }

    /**
     * 控件的移动动画
     *
     * @param view
     * @param fromY
     * @param toY
     */

    public void translationY(final View view, int fromY, int toY) {
        ValueAnimator animator = null;
        animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
                view.setTranslationY(f);

            }

        });
    }

    /**
     * 控件的移动动画
     *
     * @param view
     * @param fromY
     * @param toY
     */

    public void padingY(final View view, int fromY, int toY) {
        ValueAnimator animator = null;
        animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
                view.setPadding(0, 0, 0, (int) f);

            }

        });
    }

    /**
     * 控件的Y值的动画
     *
     * @param view
     * @param fromY
     * @param toY
     * @param
     */

    public void setAnimY(final View view, int fromY, int toY) {
        ValueAnimator animator = null;
        animator = ValueAnimator.ofFloat(fromY, toY);
        animator.setTarget(view);
        animator.setDuration(500).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (Float) animation.getAnimatedValue();
                recycleParams.setMargins(0, (int) f, 0, 0);
                view.setLayoutParams(recycleParams);
            }

        });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true);
            fab.setVisibility(View.VISIBLE);
            newplayer_recycleview.setVisibility(View.VISIBLE);
        } else {
//            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
            fab.setVisibility(View.GONE);
            newplayer_recycleview.setVisibility(View.GONE);
        }
    }
}
