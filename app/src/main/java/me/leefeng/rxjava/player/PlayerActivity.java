package me.leefeng.rxjava.player;


import android.content.Context;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.limxing.library.utils.DisplayUtil;
import com.limxing.library.utils.FileUtils;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.superplayer.library.SuperPlayer;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import nz.co.delacour.exposurevideoplayer.ExposureVideoPlayer;

/**
 * Created by limxing on 2016/10/26.
 */

public class PlayerActivity extends BeidaActivity implements AdapterView.OnItemClickListener {
    //    private JCVideoPlayerStandard player;
    private ExposureVideoPlayer evp;
    private ArrayList<PlayerItemBean> playerItemBeanList;
    private PlayerAdapter adapter;
    private int current;
    //    private UniversalVideoView mVideoView;
//    private UniversalMediaController mMediaController;
    private View mVideoLayout;
    private ListView player_listview;
    private View video_title;
    private String url;
    private SuperPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        int chapter = getIntent().getIntExtra("chapter", 0);
        int courseIndex = getIntent().getIntExtra("courseIndex", 0);
        List<String> list = BeidaApplication.cList.get(courseIndex).getVideos().get(chapter).getValues();
        url = BeidaApplication.cList.get(courseIndex).getVideos().get(chapter).getUrl();
        ((TextView) findViewById(R.id.title_name)).setText(BeidaApplication.cList.get(courseIndex)
                .getCatelogue().get(chapter));
        video_title = findViewById(R.id.video_title);
        if (list.size() == 0) {
            ToastUtils.showShort(this, "本单元暂时没视频");
            return;
        }
        playerItemBeanList = new ArrayList<PlayerItemBean>();
        for (String s : list) {
            playerItemBeanList.add(new PlayerItemBean(s));
        }
        playerItemBeanList.get(0).setPlaying(true);

//        initPlayer(list);
//        player = (JCVideoPlayerStandard) findViewById(R.id.player);
//        player.setJcPlayerListener(this);
//        player.setUp("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014" + playerItemBeanList.get(0).getUrl() + "-300K.mp4?wsiphost=local"
//                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, list.get(0));
//
//        player.thumbImageView.setImageResource(R.drawable.default_player);
//        http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C01S00P00-300K.mp4

//        http://124.202.166.41/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C02S01P00-300K.mp4?wsiphost=local  2.1
//        http://124.202.166.39/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C02S04P02-300K.mp4?wsiphost=local  2.4.2


//        evp = (ExposureVideoPlayer) findViewById(R.id.evp);
//        evp.init(this);//You must include a Activity here, for the video player will not function correctly.
//        evp.setVideoSource("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S02P00-300K.mp4");
//        // Set video source from raw source, evp.setVideoSource("android.resource://" + getPackageName() + "/"+R.raw.big_buck_bunny);
//        evp.start();
        player_listview = (ListView) findViewById(R.id.player_listview);
        adapter = new PlayerAdapter(playerItemBeanList);
        player_listview.setAdapter(adapter);
        player_listview.setOnItemClickListener(this);
//        player.startPlayLogic();
        initPlayer(list);

    }

//    private void initPlayer(final List<String> list) {
//        mVideoView = (UniversalVideoView) findViewById(R.id.videoView);
//        mMediaController = (UniversalMediaController) findViewById(R.id.media_controller);
//        mVideoView.setMediaController(mMediaController);
//        mVideoLayout = findViewById(R.id.video_layout);
////        final View mBottomLayout = findViewById(R.id.bottom);
////        final View mBottomLayout = findViewById(R.id.bottom);
//
//        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                playerItemBeanList.get(current).setPlaying(false);
//                current++;
//                if (current < list.size()) {
//                    playerItemBeanList.get(current).setPlaying(true);
//
//                    mVideoView.setVideoPath(url +
//                            playerItemBeanList.get(current).getUrl() + "-300K.mp4?wsiphost=local");
//                    mMediaController.setTitle(playerItemBeanList.get(current).getName());
//                    mVideoView.start();
//                } else {
//                    mVideoView.setFullscreen(false);
//                }
//                adapter.notifyDataSetChanged();
//            }
//        });
//        mVideoView.setVideoViewCallback(new UniversalVideoView.VideoViewCallback() {
//
//
//            public static final String TAG = "limxing";
//            public final int cachedHeight = DisplayUtil.dip2px(mContext, 205);
//            public boolean isFullscreen;
//
//            @Override
//            public void onScaleChange(boolean isFullscreen) {
//                this.isFullscreen = isFullscreen;
//                if (isFullscreen) {
//                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//                    mVideoLayout.setLayoutParams(layoutParams);
//                    //GONE the unconcerned views to leave room for video and controller
//                    player_listview.setVisibility(View.GONE);
//                    video_title.setVisibility(View.GONE);
//                } else {
//                    ViewGroup.LayoutParams layoutParams = mVideoLayout.getLayoutParams();
//                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                    layoutParams.height = this.cachedHeight;
//                    mVideoLayout.setLayoutParams(layoutParams);
//                    player_listview.setVisibility(View.VISIBLE);
//                    video_title.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onPause(MediaPlayer mediaPlayer) { // Video pause
//                Log.d(TAG, "onPause UniversalVideoView callback");
//            }
//
//            @Override
//            public void onStart(MediaPlayer mediaPlayer) { // Video start/resume to play
//                Log.d(TAG, "onStart UniversalVideoView callback");
//            }
//
//            @Override
//            public void onBufferingStart(MediaPlayer mediaPlayer) {// steam start loading
//                Log.d(TAG, "onBufferingStart UniversalVideoView callback");
//            }
//
//            @Override
//            public void onBufferingEnd(MediaPlayer mediaPlayer) {// steam end loading
//                Log.d(TAG, "onBufferingEnd UniversalVideoView callback");
//            }
//
//        });
//        mVideoView.setVideoPath(url +
//                playerItemBeanList.get(0).getUrl() + "-300K.mp4?wsiphost=local");
//        mMediaController.setTitle(list.get(0));
//        mVideoView.start();
//    }

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
        setVolumeControlStream(AudioManager.STREAM_MUSIC); //让音量键固定为媒体音量控制
        player = (SuperPlayer) findViewById(R.id.view_super_player);
        player.setScaleType(SuperPlayer.SCALETYPE_FITXY);
        player.setPlayerWH(0, player.getMeasuredHeight());//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置

        player.setNetChangeListener(true)//设置监听手机网络的变化
//                .setOnNetChangeListener(this)//实现网络变化的回调
                .setShowNavIcon(false)
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
                        playerItemBeanList.get(0).getUrl() + "-300K.mp4?wsiphost=local",true);//开始播放视频
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
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            video_title.setVisibility(View.VISIBLE);
        } else {
            video_title.setVisibility(View.GONE);
        }
    }
}
//http://60.207.246.58/wms2.pkudl.cn/ggkc/36081014/video/300k/Vc36081014C01S05P00-300K.mp4?wsiphost=local
//http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S02P00-300K.mp4?wsiphost=local
//http://124.202.166.39/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C01S00P00-300K.mp4?wsiphost=local