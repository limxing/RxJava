package me.leefeng.rxjava.main.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by limxing on 2016/10/29.
 */

public class ContactsAdapter extends BaseAdapter {
    private  List<String> list;

    public ContactsAdapter(List<String> list) {
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
        TextView tv=new TextView(viewGroup.getContext());
        tv.setText(list.get(i));
        return tv;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
