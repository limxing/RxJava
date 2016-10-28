package me.leefeng.rxjava.player;


import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.limxing.library.utils.FileUtils;
import com.limxing.library.utils.ToastUtils;
import com.limxing.publicc.alertview.AlertView;
import com.limxing.publicc.alertview.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import nz.co.delacour.exposurevideoplayer.ExposureVideoPlayer;

/**
 * Created by limxing on 2016/10/26.
 */

public class PlayerActivity extends BeidaActivity implements AdapterView.OnItemClickListener {
    private JCVideoPlayerStandard player;
    private ExposureVideoPlayer evp;
    private ArrayList<PlayerItemBean> playerItemBeanList;
    private PlayerAdapter adapter;
    private int current;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        int chapter = getIntent().getIntExtra("chapter", 0);
        int courseIndex = getIntent().getIntExtra("courseIndex", 0);
        List<String> list = BeidaApplication.cList.get(courseIndex).getVideos().get(chapter);

        ((TextView) findViewById(R.id.title_name)).setText(BeidaApplication.cList.get(courseIndex)
                .getCatelogue().get(chapter));

        if (list.size() == 0) {
            ToastUtils.showShort(this, "本单元暂时没视频");
            return;
        }
        playerItemBeanList = new ArrayList<PlayerItemBean>();
        for (String s : list) {
            playerItemBeanList.add(new PlayerItemBean(s));
        }
        playerItemBeanList.get(0).setPlaying(true);


        player = (JCVideoPlayerStandard) findViewById(R.id.player);
        player.setUp("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014" + playerItemBeanList.get(0).getUrl() + "-300K.mp4?wsiphost=local"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, list.get(0));
        player.thumbImageView.setImageResource(R.drawable.default_player);
//        http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C01S00P00-300K.mp4
//        http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S02P00-300K.mp4?wsiphost=local

//        http://124.202.166.41/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C02S01P00-300K.mp4?wsiphost=local  2.1
//        http://124.202.166.39/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C02S04P02-300K.mp4?wsiphost=local  2.4.2


//        evp = (ExposureVideoPlayer) findViewById(R.id.evp);
//        evp.init(this);//You must include a Activity here, for the video player will not function correctly.
//        evp.setVideoSource("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S02P00-300K.mp4");
//        // Set video source from raw source, evp.setVideoSource("android.resource://" + getPackageName() + "/"+R.raw.big_buck_bunny);
//        evp.start();
        ListView player_listview = (ListView) findViewById(R.id.player_listview);
        adapter = new PlayerAdapter(playerItemBeanList);
        player_listview.setAdapter(adapter);
        player_listview.setOnItemClickListener(this);
//        if (isWiFiActive(this)) {
        player.startPlayLogic();
//        } else {
//            new AlertView("您当前正在使用移动网络，继续播放将消耗流量", null, "停止播放", null, new String[]{"继续播放"}, this, AlertView.Style.Alert, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position) {
//                    if (position == 0) {
//                        player.startPlayLogic();
//                    }
//                }
//            }).show();
//        }
    }

    @Override
    protected int getView() {
        return R.layout.activity_video;
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (evp != null)
            evp.pause();
        player.startButton.performClick()
        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (evp != null)
            evp.start();
        player.startButton.performClick()
        ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null)
            player.releaseAllVideos();

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        playerItemBeanList.get(current).setPlaying(false);
        current = i;
        playerItemBeanList.get(i).setPlaying(true);

        adapter.notifyDataSetChanged();

        player.onCompletion();
        player.onPrepared();
        player.setUp("http://wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014" +
                        playerItemBeanList.get(i).getUrl() + "-300K.mp4?wsiphost=local"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, playerItemBeanList.get(i).getName());
        player.startPlayLogic();


    }

//    public boolean isWiFiActive(Context inContext) {
//        Context context = inContext.getApplicationContext();
//        WifiManager wifiManager = (WifiManager) context
//                .getSystemService(Context.WIFI_SERVICE);
//        return wifiManager.isWifiEnabled();
//    }
}
