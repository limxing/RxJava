package me.leefeng.rxjava.main.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by limxing on 2016/10/29.
 */

public class ChatPagerAdapter extends FragmentPagerAdapter {

    public ChatPagerAdapter(FragmentManager fragmentManager ) {
        super(fragmentManager);

    }

    @Override
    public int getCount() {
        return 1;
    }


    @Override
    public Fragment getItem(int position) {

        return ContactsFragment.getInstance();
    }


    public void destory() {

    }


}
