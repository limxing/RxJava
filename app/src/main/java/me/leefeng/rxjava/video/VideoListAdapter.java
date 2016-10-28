package me.leefeng.rxjava.video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class VideoListAdapter extends BaseAdapter {
    private final List<String> list;

    public VideoListAdapter(List<String> list) {
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View v= View.inflate(viewGroup.getContext(), R.layout.video_list_item,null);
        TextView tv= (TextView) v.findViewById(R.id.video_list_tv);
        tv.setText(list.get(i));
        return v;
    }
}
