package me.leefeng.rxjava.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limxing.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.chat.ItemOnClickListener;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/11/22 10:19
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactView> {
    private List<String> mList;
    private ItemOnClickListener itemOnClickListener;

    public ContactsAdapter() {
        mList = new ArrayList<>();
    }

    @Override
    public ContactsAdapter.ContactView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.contactsadapter_item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    int i = Integer.parseInt(v.getTag().toString());
                    itemOnClickListener.onItemClick(mList.get(i));
                }
            }
        });

        return new ContactView(view);
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ContactView holder, int position) {
        holder.view.setTag(position);
        holder.name.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<String> strings) {
        this.mList = strings;
        notifyDataSetChanged();
    }

    /**
     * 获取某条=内容
     *
     * @param position
     * @return
     */
    public String getItem(int position) {
        return mList.get(position);
    }


    public class ContactView extends RecyclerView.ViewHolder {
        TextView name;
        View view;

        public ContactView(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.contacts_item_name);
        }
    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

}
