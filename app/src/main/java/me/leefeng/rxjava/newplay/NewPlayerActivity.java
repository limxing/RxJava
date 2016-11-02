package me.leefeng.rxjava.newplay;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.limxing.library.utils.SharedPreferencesUtil;
import com.limxing.library.utils.ToastUtils;
import com.superplayer.library.SuperPlayer;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.Course;
import me.leefeng.rxjava.player.PlayerItemBean;

/**
 * Created by limxing on 2016/11/2.
 */

public class NewPlayerActivity extends BeidaSwipeActivity {
    private RecyclerView newplayer_recycleview;
    private PlayerListAdapter mAdapter;
    private SuperPlayer player;
    private String url;
    private ArrayList<PlayerItemBean> playerItemBeanList;
    private int current;
    private Course course;

    @Override
    protected void initView() {
        int courseIndex = getIntent().getIntExtra("course", 0);
        course = BeidaApplication.cList.get(courseIndex);


        if (course.getCatelogue().size() == 0) {
            ToastUtils.showShort(this, "本单元暂时没视频");
            return;
        }
        playerItemBeanList = new ArrayList<PlayerItemBean>();
        for (int i = 0; i < course.getCatelogue().size(); i++) {
            List<String> list = course.getVideos().get(i).getValues();
            for (String s : list) {
                playerItemBeanList.add(new PlayerItemBean(s));
            }
        }
        current = SharedPreferencesUtil.getIntData(this, course.getId(), 0);

        playerItemBeanList.get(current).setPlaying(true);


        newplayer_recycleview = (RecyclerView) findViewById(R.id.newplayer_recycleview);
        mAdapter = new PlayerListAdapter(course.getCatelogue(), playerItemBeanList);
        newplayer_recycleview.setAdapter(mAdapter);
        newplayer_recycleview.setLayoutManager(new LinearLayoutManager(this));
        newplayer_recycleview.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));

        initPlayer();
        newplayer_recycleview.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (current==position){

                    ToastUtils.showLong(view.getContext(),"正在播放");
                    return;
                }
                url =course.getVideos().get((int) mAdapter.getHeaderId(position)).getUrl();
                playerItemBeanList.get(current).setPlaying(false);
                current = position;
                playerItemBeanList.get(position).setPlaying(true);
                mAdapter.notifyDataSetChanged();

                player.stop();

                player.setTitle(playerItemBeanList.get(current).getName()).play(url +
                        playerItemBeanList.get(position).getUrl() + "-300K.mp4?wsiphost=local");
            }
        }));

    }

    private void initPlayer() {
        url =course.getVideos().get((int) mAdapter.getHeaderId(current)).getUrl();
        int position = SharedPreferencesUtil.getIntData(this, course.getId() + "play", 0);
        player = (SuperPlayer) findViewById(R.id.newplay_player);
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
                    url =course.getVideos().get((int) mAdapter.getHeaderId(current)).getUrl();
                    playerItemBeanList.get(current).setPlaying(true);
                    player.setTitle(playerItemBeanList.get(current).getName()).play(url +
                            playerItemBeanList.get(current).getUrl() + "-300K.mp4?wsiphost=local");
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
                .setCoverImage(R.drawable.default_player)
                .setUrl(url + playerItemBeanList.get((int) mAdapter.getHeaderId(current)).getUrl() + "-300K.mp4?wsiphost=local")
                .seekTo(position,true)
                .play(500);

        player.setScaleType(SuperPlayer.SCALETYPE_FITPARENT);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置

        if (current == 0) {
            newplayer_recycleview.scrollToPosition(0);

        } else {
            newplayer_recycleview.scrollToPosition(current - 1);

        }

    }

    @Override
    protected int getView() {
        return R.layout.activity_newplay;
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
        SharedPreferencesUtil.saveIntData(this, course.getId() + "", current);
        SharedPreferencesUtil.saveIntData(this, course.getId() + "play", player.getCurrentPosition());
        if (player != null) {
            player.onDestroy();

        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

}
