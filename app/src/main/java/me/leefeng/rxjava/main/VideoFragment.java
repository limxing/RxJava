package me.leefeng.rxjava.main;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.video.VideoListActivity;

/**
 * Created by limxing on 2016/10/26.
 */

public class VideoFragment extends Fragment implements View.OnClickListener {


    public VideoFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        view.findViewById(R.id.video_c).setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getActivity(),VideoListActivity.class);
        intent.putExtra("title","C语言课程目录");
        startActivity(intent);
    }
}
