package me.leefeng.rxjava.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.chat.ChatPagerAdapter;

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
