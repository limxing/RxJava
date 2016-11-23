package me.leefeng.rxjava.singlechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.StringUtils;
import com.limxing.library.utils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.gmariotti.recyclerview.itemanimator.SlideInOutBottomItemAnimator;
import it.gmariotti.recyclerview.itemanimator.SlideInOutTopItemAnimator;
import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/11/18 11:13
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */

public class SingleChatActivity extends BeidaSwipeActivity implements SingleChatView,
        View.OnLayoutChangeListener, View.OnTouchListener, TextView.OnEditorActionListener {
    @BindView(R.id.title_name)
    TextView titleName;
    @BindView(R.id.title_right_image)
    ImageView titleRightImage;
    @BindView(R.id.singlechat_bottom)
    LinearLayout singlechatBottom;
    @BindView(R.id.singlechat_contain)
    RelativeLayout singlechatContain;
    @BindView(R.id.singlechat_list)
    RecyclerView singlechatListView;
    @BindView(R.id.singlechat_edittext)
    EditText singlechatEdittext;
    private ChatListAdapter adapter;
    private String id;
    private LinearLayoutManager linearLayoutManager;
    private IntentFilter intentFilter;
    private NewMessageBroadcastReceiver msgReceiver;

    @Override
    protected void initView() {
    }

    @Override
    protected int getView() {
        return R.layout.activity_singlechat;
    }

    @Override
    protected void doReceiver(String action) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
        singlechatListView.scrollToPosition(adapter.getItemCount() - 1);
        msgReceiver = new NewMessageBroadcastReceiver();
        intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
//        EMChatManager.getInstance().registerEventListener(msgListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(msgReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EMChatManager.getInstance().unregisterEventListener(msgListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(msgReceiver);
    }

    private void init() {
        id = getIntent().getStringExtra("id");
        titleName.setText("与" + id + "聊天中");
        adapter = new ChatListAdapter(id);
        linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
        singlechatListView.setLayoutManager(linearLayoutManager);

        singlechatListView.setAdapter(adapter);
        singlechatListView.addOnLayoutChangeListener(this);
        singlechatListView.setOnTouchListener(this);
        singlechatListView.setItemAnimator(new SimpleItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateAdd(RecyclerView.ViewHolder holder) {
                return false;
            }

            @Override
            public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
                return false;
            }

            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromLeft, int fromTop, int toLeft, int toTop) {
                return false;
            }

            @Override
            public void runPendingAnimations() {

            }

            @Override
            public void endAnimation(RecyclerView.ViewHolder item) {

            }

            @Override
            public void endAnimations() {

            }

            @Override
            public boolean isRunning() {
                return false;
            }
        });
        singlechatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (linearLayoutManager.findFirstVisibleItemPosition()==0){
                    adapter.loadMoreMessage();
                }
            }
        });

        singlechatEdittext.setOnEditorActionListener(this);
        singlechatEdittext.setHorizontallyScrolling(false);
        singlechatEdittext.setMaxLines(3);
    }

    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //消息id
            String msgId = intent.getStringExtra("msgid");
            //发消息的人的username(userid)
            String msgFrom = intent.getStringExtra("from");
            //消息类型，文本、图片、语音消息等，这里返回的值为msg.type.ordinal()。
            //所以消息type实际为是enum类型
            int msgType = intent.getIntExtra("type", 0);
            LogUtils.i(getClass(), "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
            //更方便的方法是通过msgId直接获取整个message
            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            if (msgFrom.equals(id)) {
                if (linearLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 2) {
                    singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
                adapter.notifyItemInserted(adapter.getItemCount() - 1);
            }
        }
    }

    EMEventListener msgListener = new EMEventListener() {
        @Override
        public void onEvent(EMNotifierEvent emNotifierEvent) {
            EMMessage message = (EMMessage) emNotifierEvent.getData();
            switch (emNotifierEvent.getEvent()) {
                case EventDeliveryAck:
                    //已发送
                    ToastUtils.showShort(mContext, "发送成功");
                    adapter.notifyDataSetChanged();
                    break;
                case EventNewCMDMessage:
                    break;
                case EventNewMessage:
                    //新消息
                    if (message.getFrom().equals(id)) {
                        if (linearLayoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 2) {
                            singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
                        }
                        adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    }
                    break;
                case EventReadAck:
                    //一读
                    ToastUtils.showShort(mContext, "消息一读");
                    break;
            }
        }
    };

    /**
     * 监听键盘弹出没
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        int keyHeight = getResources().getDisplayMetrics().heightPixels / 3;
//现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight) && adapter.getItemCount() > 0) {
            singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {

        }
    }

    /**
     * RecycleView的触摸事件
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        closeInput();
        return false;
    }

    /**
     * 键盘发送按钮
     *
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            String s = singlechatEdittext.getText().toString().trim();
            if (!StringUtils.isEmpty(s)) {
                final EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                TextMessageBody txtBody = new TextMessageBody(s);
                message.addBody(txtBody);
                message.setTo(id);
                message.setReceipt(id);
                message.setFrom(EMChatManager.getInstance().getCurrentUser());
                adapter.addMessage(message);
                EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        LogUtils.i(getClass(), "发送成功onSuccess" + Thread.currentThread());
//                        message.status = EMMessage.Status.SUCCESS;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {
//                        message.status = EMMessage.Status.FAIL;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }

                    @Override
                    public void onProgress(int i, String s) {
                        message.status = EMMessage.Status.INPROGRESS;
                    }
                });
                singlechatEdittext.setText("");
                singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
//                singlechatListView.scrollToPosition(adapter.getItemCount() - 1);
                return true;
            }
        }
        return false;
    }


}
