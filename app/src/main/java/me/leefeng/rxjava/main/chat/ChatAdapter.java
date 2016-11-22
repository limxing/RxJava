package me.leefeng.rxjava.main.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.limxing.library.utils.LogUtils;


import java.util.List;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.utils.TimeString;

/**
 * Created by limxing on 2016/10/29.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ContactsHolder> {
    private List<EMConversation> list;
    private ItemOnClickListener itemOnClickListener;

    public ChatAdapter(List<EMConversation> list) {
        this.list = list;
    }


    public void setList(List<EMConversation> list) {
        this.list = list;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.chatadapter_item, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemOnClickListener != null) {
                    int i = Integer.parseInt(v.getTag().toString());
                    list.get(i).markAllMessagesAsRead();
                    itemOnClickListener.onItemClick(list.get(i).getUserName());
                    LogUtils.i("点击了条目-监听不为空");
                }
            }
        });

        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        EMConversation conversation = list.get(position);
        holder.view.setTag(position);
        holder.item_name.setText(conversation.getUserName());
        EMMessage message = conversation.getLastMessage();
        if (message.getType() == EMMessage.Type.TXT) {
            String txt = message.getBody().toString();
            holder.lastmsg.setText(txt.substring(txt.indexOf("\"") + 1, txt.length() - 1));
        }
        holder.item_time.setText(TimeString.getTimefromLong(message.getMsgTime()));
        int unRead = conversation.getUnreadMsgCount();
        if (unRead > 0) {
            holder.item_unread.setVisibility(View.VISIBLE);
            if (unRead > 99) {
                holder.item_unread.setText("99+");
            } else {
                holder.item_unread.setText(unRead + "");
            }
        } else {
            holder.item_unread.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void destory() {

    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        TextView item_unread;
        TextView item_time;
        TextView item_name;
        TextView lastmsg;
        View view;

        public ContactsHolder(View itemView) {
            super(itemView);
            view = itemView;
            item_name = (TextView) itemView.findViewById(R.id.contacts_item_name);
            lastmsg = (TextView) itemView.findViewById(R.id.contacts_item_lastmsg);
            item_time = (TextView) itemView.findViewById(R.id.contacts_item_time);
            item_unread = (TextView) itemView.findViewById(R.id.contacts_item_unread);
        }

    }

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }


}
