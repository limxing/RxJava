package me.leefeng.rxjava.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.adapter.EMAConversation;
import com.hyphenate.exceptions.HyphenateException;
import com.limxing.library.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.singlechat.SingleChatActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/11/18.
 */

public class ContactsFragment extends Fragment implements ItemOnClickListener {
    static ContactsFragment contactsFragment;
    private ContactsAdapter contactsAdapter;
    private List<EMConversation> contactsList;
    private RecyclerView recyclerView;

    public ContactsFragment() {
        contactsList = new ArrayList<>();
        contactsAdapter = new ContactsAdapter(contactsList);
        contactsAdapter.setItemOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(getClass(), "onResume");
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
            @Override
            public void call(Subscriber<? super List<EMConversation>> subscriber) {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    List<EMConversation> list = new ArrayList<EMConversation>();
                    Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                    for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
                        list.add(entry.getValue());
                    }
                    subscriber.onNext(list);
                } catch (HyphenateException e) {
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
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
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
        recyclerView = new RecyclerView(container.getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        recyclerView.setAdapter(contactsAdapter);
//        contactsAdapter.notifyDataSetChanged();
//        recyclerView.setLFRecyclerViewListener(this);
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

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
                @Override
                public void call(Subscriber<? super List<EMConversation>> subscriber) {
                    List<EMConversation> list = new ArrayList<EMConversation>();
                    Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
                    for (Map.Entry<String, EMConversation> entry : conversations.entrySet()) {
                        list.add(entry.getValue());
                    }
                    list.sort(new ConversationCompare());
                    subscriber.onNext(list);
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
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    public class ConversationCompare implements Comparator<EMConversation> {


        @Override
        public int compare(EMConversation emConversation, EMConversation t1) {
            return (int) (emConversation.getLastMessage().getMsgTime() - t1.getLastMessage().getMsgTime());
        }
    }
}
