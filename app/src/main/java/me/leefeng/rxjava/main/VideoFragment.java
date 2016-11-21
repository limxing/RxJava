package me.leefeng.rxjava.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.limxing.library.utils.IOUtils;
import com.limxing.library.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.newplay.NewPlayerActivity;
import me.leefeng.rxjava.video.VideoListActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.observers.Observers;

/**
 * Created by limxing on 2016/10/26.
 */

public class VideoFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    private VideoAdapter adapter;
    private MainView mainView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainView = null;
    }

    public void setMainView(MainView view) {
        this.mainView = view;

        Observable.create(new Observable.OnSubscribe<List<Course>>() {
            @Override
            public void call(Subscriber<? super List<Course>> subscriber) {
                try {
                    subscriber.onNext(JSON.parseArray(JSON.parseObject(IOUtils.streamToString(new FileInputStream(
                            new File(((Activity) mainView).getCacheDir(), "course.json")))).getString("courses"), Course.class)
                    );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Course>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(List<Course> courses) {
                        BeidaApplication.cList = courses;
                        adapter.setList(courses);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public VideoFragment() {
        BeidaApplication.cList = new ArrayList<Course>();
        adapter = new VideoAdapter(BeidaApplication.cList);

    }

    public static VideoFragment getInstance(MainView mainView) {
        VideoFragment videoFragment = new VideoFragment();
        videoFragment.setMainView(mainView);
        return videoFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ListView listView = (ListView) view.findViewById(R.id.f_video_listview);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), VideoListActivity.class);
        intent.putExtra("title", "C语言课程目录");
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), NewPlayerActivity.class);
        intent.putExtra("course", i);
        startActivity(intent);

    }
}
