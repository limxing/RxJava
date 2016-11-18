package me.leefeng.rxjava.main.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.singlechat.SingleChatActivity;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/29.
 */

public class ChatPagerAdapter extends PagerAdapter implements ItemOnClickListener {

private Context context;
    private ContactsAdapter chatsAdapter;
    private ContactsAdapter contactsAdapter;
    private ArrayList<String> chatsList;
    private ArrayList<String> contactsList;

    public ChatPagerAdapter(Context context) {
        this.context = context;
        contactsList = new ArrayList<String>();
        chatsList = new ArrayList<String>();
        contactsAdapter = new ContactsAdapter(contactsList);
        chatsAdapter = new ContactsAdapter(chatsList);
        contactsAdapter.setItemOnClickListener(this);

        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    subscriber.onNext(usernames);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> s) {
                        contactsAdapter.setList(s);
                        contactsAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        RecyclerView view;
        if (position == 0) {
            view = new RecyclerView(container.getContext());
            view.setAdapter(contactsAdapter);
        } else {
            view = new RecyclerView(container.getContext());
            view.setAdapter(chatsAdapter);
        }
        view.setLayoutManager(new LinearLayoutManager(container.getContext()));

        container.addView(view);
        return view;
    }

    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(context, SingleChatActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public void destory() {
        contactsAdapter.destory();
        context=null;
    }
}
