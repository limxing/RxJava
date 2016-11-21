package me.leefeng.rxjava.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.ToastUtils;

import java.util.List;
import java.util.Map;

import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.chat.ChatPagerAdapter;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/26.
 */

public class ChatFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private static ChatFragment chatFragment;
    private static ChatPagerAdapter chatPagerAdapter;
    private View view;




    public ChatFragment() {
//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();


    }

    public static ChatFragment getInstance(FragmentManager fragmentManager) {
         chatFragment = new ChatFragment();
        chatPagerAdapter = new ChatPagerAdapter(fragmentManager);
        return chatFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_chat, container, false);
            ViewPager chat_viewpager = (ViewPager) view.findViewById(R.id.chat_viewpager);
            chat_viewpager.setOnPageChangeListener(this);
            chat_viewpager.setAdapter(chatPagerAdapter);
        }
        return view;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void destory() {
        chatPagerAdapter.destory();
    }
}
