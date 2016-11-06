package me.leefeng.rxjava.main;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/28.
 */

public class VideoAdapter extends BaseAdapter {

    private List<Course> list;

    public void setList(List<Course> list) {
        this.list = list;
    }

    public VideoAdapter(List<Course> list) {
        this.list = list;
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
        View v = View.inflate(viewGroup.getContext(), R.layout.fragment_video_item, null);
        TextView name = (TextView) v.findViewById(R.id.video_item_name);
        name.setText(list.get(i).getName());
        TextView teacher = (TextView) v.findViewById(R.id.video_item_teacher);
        teacher.setText("讲师：" + list.get(i).getTeacher());
        ImageView imageView = (ImageView) v.findViewById(R.id.video_item_pic);
        Glide.with(viewGroup.getContext())
                .load(list.get(i).getPic())
//                .placeholder(R.drawable.cyuyan)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        return v;
    }
}
