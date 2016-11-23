package me.leefeng.rxjava.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.limxing.library.utils.LogUtils;

import java.util.List;

import me.leefeng.lfrecyclerview.LFRecyclerView;
import me.leefeng.lfrecyclerview.OnItemClickListener;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.chat.ItemOnClickListener;
import me.leefeng.rxjava.singlechat.SingleChatActivity;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.View.OVER_SCROLL_ALWAYS;
import static android.view.View.OVER_SCROLL_NEVER;

/**
 * 版权：北京航天世景信息技术有限公司
 * <p>
 * 作者：李利锋
 * <p>
 * 创建日期：2016/11/22 10:08
 * <p>
 * 描述：
 * <p>
 * 修改历史：
 */

public class ContactsFragment extends Fragment implements LFRecyclerView.LFRecyclerViewListener, ItemOnClickListener {
    private static ContactsFragment mFragment;
    private ContactsAdapter contactsAdapter;
    private LFRecyclerView recyclerView;

    public static ContactsFragment getInstance() {
        if (mFragment == null) {
            mFragment = new ContactsFragment();
        }
        return mFragment;
    }

    public ContactsFragment() {
        contactsAdapter = new ContactsAdapter();
        contactsAdapter.setItemOnClickListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (recyclerView == null) {
            recyclerView = new LFRecyclerView(container.getContext());
            recyclerView.setAdapter(contactsAdapter);
            recyclerView.setLoadMore(false);
            recyclerView.hideTimeView();
            recyclerView.setBackgroundResource(R.color.list_bac);
            recyclerView.setOverScrollMode(OVER_SCROLL_NEVER);
            recyclerView.setLFRecyclerViewListener(this);
        }
        return recyclerView;
    }

    @Override
    public void onRefresh() {
        getData();
    }


    @Override
    public void onLoadMore() {

    }

    /**
     * 获取数据
     */
    private void getData() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try {
                    subscriber.onNext(EMChatManager.getInstance().getContactUserNames());
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.stopRefresh(false);
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        contactsAdapter.setList(strings);
                        LogUtils.i("联系人个数:" + strings.size());
                        recyclerView.stopRefresh(true);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i(getClass(), "onResume");
        getData();
    }

    /**
     * 条目的点击事件
     *
     * @param id
     */
    @Override
    public void onItemClick(String id) {
        Intent intent = new Intent(getActivity(), SingleChatActivity.class);
        intent.putExtra("id", id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 监听到好有变化
     */
    public void notifyDataChanged() {
        getData();

    }
}
