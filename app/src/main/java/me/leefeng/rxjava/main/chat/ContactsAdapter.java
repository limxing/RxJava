package me.leefeng.rxjava.main.chat;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.limxing.library.utils.LogUtils;

import java.util.List;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/29.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {
    private List<String> list;
    private ItemOnClickListener itemOnClickListener;

    public ContactsAdapter(List<String> list) {
        this.list = list;
    }


    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.contactsadapter_item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    itemOnClickListener.onItemClick(v.getTag().toString());
                    LogUtils.i("点击了条目-监听不为空");
                }
            }
        });

        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        holder.view.setTag(list.get(position));
        holder.item_name.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void destory() {

    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        TextView item_name;
        View view;

        public ContactsHolder(View itemView) {
            super(itemView);
            view = itemView;
            item_name = (TextView) itemView.findViewById(R.id.contacts_item_name);
        }

    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

}
