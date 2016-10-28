package me.leefeng.rxjava.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class ChatFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private MainView mainView;

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainView = null;
    }

    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    public ChatFragment() {

    }

    public static ChatFragment getInstance(MainView mainView) {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.setMainView(mainView);
        return chatFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ViewPager chat_viewpager = (ViewPager) view.findViewById(R.id.chat_viewpager);
        chat_viewpager.setOnPageChangeListener(this);
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
}
