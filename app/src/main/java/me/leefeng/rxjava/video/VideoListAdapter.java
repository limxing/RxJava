package me.leefeng.rxjava.video;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class VideoListAdapter extends BaseAdapter {
    private final String[] list;

    public VideoListAdapter(String[] list) {
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int i) {
        return list[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View v= View.inflate(viewGroup.getContext(), R.layout.video_list_item,null);
        TextView tv= (TextView) v.findViewById(R.id.video_list_tv);
        tv.setText(list[i]);
        return v;
    }
}
