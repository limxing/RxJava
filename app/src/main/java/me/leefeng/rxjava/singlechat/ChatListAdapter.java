package me.leefeng.rxjava.singlechat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.limxing.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.utils.TimeString;
import me.leefeng.rxjava.view.LoadView;

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
    private final String id;
    private final EMConversation conversation;


    public ChatListAdapter(String id) {
        this.id = id;
        conversation = EMChatManager.getInstance().getConversation(id);
//LogUtils.i(getClass(),conversation.getMsgCount());
    }

    @Override
    public int getItemViewType(int position) {
        if (conversation.getMessage(position) != null &&
                conversation.getMessage(position).getFrom()
                        .equals(EMChatManager.getInstance().getCurrentUser())) {
            return CHATLEFT;
        }
        return CHATRight;
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
        if (position == 0 || position > 0 && (conversation.getMessage(position).getMsgTime()
                - conversation.getMessage(position - 1).getMsgTime())
                > 1000 * 60) {
            holder.time.setVisibility(View.VISIBLE);
        } else {
            holder.time.setVisibility(View.GONE);
        }
        EMMessage message = conversation.getMessage(position);
        holder.chat_item_content.removeAllViews();

        holder.time.setText(TimeString.getTimefromLong(message.getMsgTime()));

        EMMessage.Type type = message.getType();
        if (type == EMMessage.Type.TXT) {
            TextView textView = new TextView(holder.chat_item_content.getContext());
            String txt = message.getBody().toString();
            textView.setText(txt.substring(txt.indexOf("\"") + 1, txt.length() - 1));
            textView.setTextSize(16);
            if (getItemViewType(position) == CHATLEFT) {
                textView.setTextColor(0xffffffff);
                EMMessage.Status status = message.status;
                LogUtils.i("消息：" + textView.getText().toString() + "=状态=" + status);
                if (status == EMMessage.Status.SUCCESS) {
                    holder.loadview.setVisibility(View.INVISIBLE);
                    holder.warnImage.setVisibility(View.INVISIBLE);

                } else if (status == EMMessage.Status.FAIL) {
                    holder.loadview.setVisibility(View.INVISIBLE);
                    holder.warnImage.setVisibility(View.VISIBLE);

                } else {
                    holder.loadview.setVisibility(View.VISIBLE);
                    holder.warnImage.setVisibility(View.INVISIBLE);
                }
            } else {
                textView.setTextColor(0xee000000);
            }
            holder.chat_item_content.addView(textView);
        }

    }

    @Override
    public int getItemCount() {
        return conversation.getMsgCount();
    }

    /**
     * @param message
     * @param isReceive
     */
    public void addMessage(EMMessage message, boolean isReceive) {
        if (isReceive) {
            EMChatManager.getInstance().getConversation(id).markMessageAsRead(message.getMsgId());
        }
        conversation.addMessage(message);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addMessage(List<EMMessage> l) {
        for (EMMessage message : l) {
            EMChatManager.getInstance().getConversation(id).markMessageAsRead(message.getMsgId());
            conversation.addMessage(message);
        }

    }

    /**
     * 消息已送达
     *
     * @param l
     */
    public void messageSendSuccess(List<EMMessage> l) {
        notifyDataSetChanged();

    }

    /**
     * 加载更多的消息
     */
    public void loadMoreMessage() {
        conversation.loadMoreMsgFromDB(conversation.getMessage(0).getMsgId(), 20);
//        notifyDataSetChanged();
    }


    public class ChatListItem extends RecyclerView.ViewHolder {
        ImageView warnImage;
        LoadView loadview;
        RelativeLayout chat_item_content;
        TextView time;

        public ChatListItem(View itemView) {
            super(itemView);
            chat_item_content = (RelativeLayout) itemView.findViewById(R.id.chat_item_content);
            time = (TextView) itemView.findViewById(R.id.chat_item_time);
            loadview = (LoadView) itemView.findViewById(R.id.chat_item_loadview);
            warnImage = (ImageView) itemView.findViewById(R.id.chat_item_warn);
        }
    }
}
