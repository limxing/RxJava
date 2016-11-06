package me.leefeng.rxjava.player;

import android.content.res.Configuration;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.limxing.library.utils.ToastUtils;
import com.superplayer.library.SuperPlayer;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.PlayerBaseActivity;
import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class PlayerActivity extends PlayerBaseActivity implements AdapterView.OnItemClickListener {
    private ArrayList<PlayerItemBean> playerItemBeanList;
    private PlayerAdapter adapter;
    private int current;
    private ListView player_listview;
    private String url;
    private SuperPlayer player;


    @Override
    protected void initView() {
        int chapter = getIntent().getIntExtra("chapter", 0);
        int courseIndex = getIntent().getIntExtra("courseIndex", 0);
        List<String> list = BeidaApplication.cList.get(courseIndex).getVideos().get(chapter).getValues();
        url = BeidaApplication.cList.get(courseIndex).getVideos().get(chapter).getUrl();

        if (list.size() == 0) {
            ToastUtils.showShort(this, "本单元暂时没视频");
            return;
        }
        playerItemBeanList = new ArrayList<PlayerItemBean>();
        for (String s : list) {
            playerItemBeanList.add(new PlayerItemBean(s));
        }
        playerItemBeanList.get(0).setPlaying(true);

        player_listview = (ListView) findViewById(R.id.player_listview);
        adapter = new PlayerAdapter(playerItemBeanList);
        player_listview.setAdapter(adapter);
        player_listview.setOnItemClickListener(this);
        initPlayer(list);


    }


    @Override
    protected int getView() {
        return R.layout.activity_video;
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        playerItemBeanList.get(current).setPlaying(false);
        current = i;
        playerItemBeanList.get(i).setPlaying(true);
        adapter.notifyDataSetChanged();
        player.stop();
        player.setTitle(playerItemBeanList.get(current).getName()).play(url +
                playerItemBeanList.get(i).getUrl() + "-300K.mp4?wsiphost=local");
//        mVideoView.setVideoPath(url +
//                playerItemBeanList.get(i).getUrl() + "-300K.mp4?wsiphost=local");
//        mMediaController.setTitle(playerItemBeanList.get(i).getName());
//        mVideoView.start();

//        player.onCompletion();
//        player.onPrepared();
//        player.setUp("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014" +
//                        playerItemBeanList.get(i).getUrl() + "-300K.mp4?wsiphost=local"
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, playerItemBeanList.get(i).getName());
//        player.startPlayLogic();

    }

    private void initPlayer(List<String> list) {
        player = (SuperPlayer) findViewById(R.id.view_super_player);
//        player.setScaleType(SuperPlayer.SCALETYPE_FITPARENT);
//        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置

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

                    player.setTitle(playerItemBeanList.get(current).getName()).play(url +
                            playerItemBeanList.get(current).getUrl() + "-300K.mp4?wsiphost=local");
                } else {
                    player.toggleFullScreen();
                }
                adapter.notifyDataSetChanged();
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
        }).setTitle(playerItemBeanList.get(0).getName())//设置视频的titleName
                .setCoverImage(R.drawable.default_player)
                .play(url +
                        playerItemBeanList.get(0).getUrl() + "-300K.mp4?wsiphost=local", true);//开始播放视频

        player.setScaleType(SuperPlayer.SCALETYPE_FITPARENT);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置


    }


//    public boolean isWiFiActive(Context inContext) {
//        Context context = inContext.getApplicationContext();
//        WifiManager wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        return wifiManager.isWifiEnabled();
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        if (player != null) {
//            player.onConfigurationChanged(newConfig);
//        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true);
        } else {
            SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        }
    }
}
//http://60.207.246.58/wms2.pkudl.cn/ggkc/36081014/video/300k/Vc36081014C01S05P00-300K.mp4?wsiphost=local
//http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S02P00-300K.mp4?wsiphost=local
//http://124.202.166.39/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C01S00P00-300K.mp4?wsiphost=local