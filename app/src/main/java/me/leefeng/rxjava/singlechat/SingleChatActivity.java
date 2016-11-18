package me.leefeng.rxjava.singlechat;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

public class SingleChatActivity extends BeidaSwipeActivity implements SingleChatView, View.OnLayoutChangeListener, View.OnTouchListener, TextView.OnEditorActionListener {
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
    }

    private void init() {
        id = getIntent().getStringExtra("id");
        titleName.setText("与" + id + "聊天中");
        adapter = new ChatListAdapter();
        singlechatListView.setLayoutManager(new LinearLayoutManager(this));
        singlechatListView.setAdapter(adapter);
        singlechatListView.addOnLayoutChangeListener(this);
        singlechatListView.setOnTouchListener(this);
        singlechatListView.setItemAnimator(new DefaultItemAnimator());
        Observable.create(new Observable.OnSubscribe<List<EMMessage>>() {
            @Override
            public void call(Subscriber<? super List<EMMessage>> subscriber) {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(id);
                //获取此会话的所有消息
                subscriber.onNext(conversation.getAllMessages());
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EMMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(List<EMMessage> emMessages) {
                        adapter.setList(emMessages);
                        singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
                    }
                });

        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        singlechatEdittext.setOnEditorActionListener(this);
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> list) {

        }

        @Override
        public void onMessageChanged(EMMessage emMessage, Object o) {

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
                EMMessage message = EMMessage.createTxtSendMessage(s, id);
                EMClient.getInstance().chatManager().sendMessage(message);
                adapter.addMessage(message);
                singlechatEdittext.setText("");
//                singlechatListView.smoothScrollToPosition(adapter.getItemCount() - 1);
                return true;
            }
        }
        return false;
    }
}
