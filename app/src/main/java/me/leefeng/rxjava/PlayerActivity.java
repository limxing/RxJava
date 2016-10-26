package me.leefeng.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by limxing on 2016/10/26.
 */

public class PlayerActivity extends AppCompatActivity {
    private JCVideoPlayerStandard player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        player = (JCVideoPlayerStandard) findViewById(R.id.player);
        player.setUp("http://119.90.20.36/wms2.pkudl.cn/jsj/08281014/video/300k/Vc08281014C03S01P00-300K.mp4?wsiphost=local"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "什么玩意");
        player.thumbImageView.setImageResource(R.mipmap.ic_launcher);
    }
    @Override
    public void onBackPressed() {
        if (player.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        player.releaseAllVideos();
    }
}
