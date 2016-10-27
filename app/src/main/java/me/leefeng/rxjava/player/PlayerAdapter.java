package me.leefeng.rxjava.player;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/27.
 */

public class PlayerAdapter extends BaseAdapter {
    private final List<PlayerItemBean> list;

    public PlayerAdapter(List<PlayerItemBean> playerItemBeanList) {
        this.list=playerItemBeanList;
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
      View v=  View.inflate(viewGroup.getContext(), R.layout.player_item,null);
        TextView player_list_tv = (TextView) v.findViewById(R.id.player_list_tv);
        player_list_tv.setText(list.get(i).getName());
       View vv= v.findViewById(R.id.player_list_state);
        if (list.get(i).isPlaying()){
            vv.setVisibility(View.VISIBLE);
        }else{
            vv.setVisibility(View.GONE);
        }
        return v;
    }
}
