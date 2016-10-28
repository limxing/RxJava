package me.leefeng.rxjava.video;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import java.util.List;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.main.Course;
import me.leefeng.rxjava.player.PlayerActivity;
import me.leefeng.rxjava.R;


/**
 * Created by limxing on 2016/10/26.
 */

public class VideoListActivity extends BeidaActivity implements AdapterView.OnItemClickListener {

    private int courseIndex;

    @Override
    protected void initView() {
        courseIndex= getIntent().getIntExtra("course",0);
        Course course= BeidaApplication.cList.get(courseIndex);
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(course.getName());
        ListView video_list_listview = (ListView) findViewById(R.id.video_list_listview);
        video_list_listview.setAdapter(new VideoListAdapter(course.getCatelogue()));
        video_list_listview.setOnItemClickListener(this);
    }

    @Override
    protected int getView() {
        return R.layout.activity_videolist;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("chapter", i);
        intent.putExtra("courseIndex", courseIndex);

        startActivity(intent);
    }
}
