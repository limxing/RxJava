package me.leefeng.rxjava.main.chat;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/29.
 */

public class ChatPagerAdapter extends PagerAdapter {

    private ContactsAdapter chatsAdapter;
    private ContactsAdapter contactsAdapter;
    private ArrayList<String> chatsList;
    private ArrayList<String> contactsList;

    public ChatPagerAdapter() {

        contactsList = new ArrayList<String>();
        chatsList = new ArrayList<String>();
        contactsAdapter = new ContactsAdapter(contactsList);
        chatsAdapter = new ContactsAdapter(chatsList);


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
        ListView view;
        if (position == 0) {
            view = new ListView(container.getContext());
            view.setAdapter(contactsAdapter);
        } else {
            view = new ListView(container.getContext());
            view.setAdapter(chatsAdapter);
        }
        container.addView(view);
        return view;
    }
}
