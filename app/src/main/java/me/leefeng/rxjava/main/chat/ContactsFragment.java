package me.leefeng.rxjava.main.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.limxing.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.rxjava.singlechat.SingleChatActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/11/18.
 */

public class ContactsFragment extends Fragment implements ItemOnClickListener, LFRecyclerView.LFRecyclerViewListener {
    static ContactsFragment contactsFragment;
    private ContactsAdapter contactsAdapter;
    private List<EMConversation> contactsList;
    private LFRecyclerView recyclerView;
    private NewMessageBroadcastReceiver msgReceiver;
    private IntentFilter intentFilter;

    public ContactsFragment() {
        contactsList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(contactsList);
        contactsAdapter.setItemOnClickListener(this);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msgReceiver = new NewMessageBroadcastReceiver();
        intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
        intentFilter.setPriority(3);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(getClass(), "onResume");
        getContext().registerReceiver(msgReceiver, intentFilter);

        Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
            @Override
            public void call(Subscriber<? super List<EMConversation>> subscriber) {
                try {
                    List<String> usernames = EMChatManager.getInstance().getContactUserNames();
                    List<EMConversation> list = new ArrayList<EMConversation>();
                    Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
                    for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
                        list.add(entry.getValue());
                    }
                    subscriber.onNext(list);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EMConversation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<EMConversation> s) {
                        contactsAdapter.setList(s);
                        LogUtils.i("huoqudao:" + s.size());
                        contactsAdapter.notifyDataSetChanged();
                    }
                });

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.i(getClass(), "onPause");
        getContext().unregisterReceiver(msgReceiver);
    }

    public static ContactsFragment getInstance() {
        LogUtils.i("====getInstance");
        if (contactsFragment == null) {
            LogUtils.i("====new ContactsFragment");
            contactsFragment = new ContactsFragment();
        }
        return contactsFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtils.i("====onCreateView");
//        View view = inflater.inflate(R.layout.lfrecycleview, null);
//        recyclerView=(LFRecyclerView) view.findViewById(R.id.lfrecycleview);
        recyclerView = new LFRecyclerView(container.getContext());
//        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(contactsAdapter);
        recyclerView.setLoadMore(false);
//        contactsAdapter.notifyDataSetChanged();
        recyclerView.setLFRecyclerViewListener(this);
        return recyclerView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.i("====onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.i("====onDestroy");
//        contactsAdapter.destory();
    }


    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(getActivity(), SingleChatActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
            @Override
            public void call(Subscriber<? super List<EMConversation>> subscriber) {
                try {
                    List<String> usernames = EMChatManager.getInstance().getContactUserNames();
                    List<EMConversation> list = new ArrayList<EMConversation>();
                    Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
                    for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
                        list.add(entry.getValue());
                    }
                    subscriber.onNext(list);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<EMConversation>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.stopRefresh(false);
                    }

                    @Override
                    public void onNext(List<EMConversation> s) {
                        contactsAdapter.setList(s);
                        LogUtils.i("huoqudao:" + s.size());
                        contactsAdapter.notifyDataSetChanged();
                        recyclerView.stopRefresh(true);
                    }
                });

    }

    @Override
    public void onLoadMore() {

    }


    /**
     * 接收消息
     */
    private class NewMessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
//            //消息id
//            String msgId = intent.getStringExtra("msgid");
//            //发消息的人的username(userid)
//            String msgFrom = intent.getStringExtra("from");
//            //消息类型，文本、图片、语音消息等，这里返回的值为msg.type.ordinal()。
//            //所以消息type实际为是enum类型
//            int msgType = intent.getIntExtra("type", 0);
//            Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:" + msgType);
//            //更方便的方法是通过msgId直接获取整个message
//            EMMessage message = EMChatManager.getInstance().getMessage(msgId);
            Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
                @Override
                public void call(Subscriber<? super List<EMConversation>> subscriber) {
                    try {
                        List<String> usernames = EMChatManager.getInstance().getContactUserNames();
                        List<EMConversation> list = new ArrayList<EMConversation>();
                        Hashtable<String, EMConversation> conversations = EMChatManager.getInstance().getAllConversations();
                        for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
                            list.add(entry.getValue());
                        }
                        subscriber.onNext(list);
                    } catch (EaseMobException e) {
                        e.printStackTrace();
                    }
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<EMConversation>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<EMConversation> s) {
                            contactsAdapter.setList(s);
                            LogUtils.i("huoqudao:" + s.size());
                            contactsAdapter.notifyDataSetChanged();
                        }
                    });

        }
    }


    public class ConversationCompare implements Comparator<EMConversation> {


        @Override
        public int compare(EMConversation emConversation, EMConversation t1) {
            return (int) (emConversation.getLastMessage().getMsgTime() - t1.getLastMessage().getMsgTime());
        }
    }
}
