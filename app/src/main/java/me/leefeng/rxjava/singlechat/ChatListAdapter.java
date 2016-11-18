package me.leefeng.rxjava.singlechat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.leefeng.rxjava.R;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/11/18 13:35
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListItem> {

    private static final int CHATLEFT = 0;
    private static final int CHATRight = 1;
    private static final int CHATCENTER = 2;
    private final SimpleDateFormat format;

    private List<EMMessage> list;


    public ChatListAdapter() {
        this.list = new ArrayList<>();
        format = new SimpleDateFormat("HH:mm:ss");

    }

    @Override
    public int getItemViewType(int position) {

        if (list.get(position).getUserName().equals(EMClient.getInstance().getCurrentUser())) {
            return CHATRight;
        }
        return CHATLEFT;
    }

    @Override
    public ChatListItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == CHATLEFT) {
            view = View.inflate(parent.getContext(), R.layout.chat_item_left, null);
        } else if (viewType == CHATRight) {
            view = View.inflate(parent.getContext(), R.layout.chat_item_right, null);
        }
        return new ChatListItem(view);
    }

    @Override
    public void onBindViewHolder(ChatListItem holder, int position) {
        if (position == 0 || position > 0 && (list.get(position).getMsgTime() - list.get(position - 1).getMsgTime())
                > 1000 * 60) {
            holder.time.setVisibility(View.VISIBLE);
        } else {
            holder.time.setVisibility(View.GONE);
        }


        EMMessage message = list.get(position);
        LogUtils.i(getClass(), message.toString());
        holder.chat_item_content.removeAllViews();

        holder.time.setText(format.format(new Date(message.getMsgTime())));
        EMMessage.Type type = message.getType();
        if (type == EMMessage.Type.TXT) {
            TextView textView = new TextView(holder.chat_item_content.getContext());
            String txt = message.getBody().toString();
            textView.setText(txt.substring(txt.indexOf("\"") + 1, txt.length() - 1));
            textView.setTextSize(16);
            textView.setTextColor(0xee000000);
            holder.chat_item_content.addView(textView);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<EMMessage> emMessages) {
        this.list = emMessages;
        notifyDataSetChanged();
    }

    public void addMessage(EMMessage message) {
        list.add(message);
        notifyItemInserted(list.size()-1);
    }


    public class ChatListItem extends RecyclerView.ViewHolder {
        RelativeLayout chat_item_content;
        TextView time;

        public ChatListItem(View itemView) {
            super(itemView);
            chat_item_content = (RelativeLayout) itemView.findViewById(R.id.chat_item_content);
            time = (TextView) itemView.findViewById(R.id.chat_item_time);
        }
    }
}
