package me.leefeng.rxjava.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.easemob.chat.EMChatManager;
import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.SharedPreferencesUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/26.
 */

public class HomeFragment extends Fragment {


    @BindView(R.id.home_xf)
    TextView homeXf;
    @BindView(R.id.home_image)
    ImageView homeImage;
    @BindView(R.id.home_name)
    TextView homeName;
    @BindView(R.id.home_bmh)
    TextView homeBmh;
    @BindView(R.id.home_xh)
    TextView homeXh;
    private View view;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        LogUtils.i(getClass(), "HomeFragment");
        goBeida();

    }

    private void goBeida() {
        String username = SharedPreferencesUtil.getStringData(getContext(), "username", "");
        String password = SharedPreferencesUtil.getStringData(getContext(), "password", "");
        RequestBody formBody = new FormBody.Builder()
                .add("myID", username)
                .add("myPW", password)
                .add("usersf", "1")
                .build();

        Call call = BeidaApplication.okHttpClient.newCall(new Request.Builder()
                .url("http://learn.pkudl.cn")
                .post(formBody)
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String current = "";
                final String str = response.body().string();
                int i = str.indexOf("<div class=\"head_wid1\">");
                final String name = str.substring(i + 23, i + 27).trim();
                i = str.indexOf("http://202.152.177.118/zsphoto");
                final String pic = str.substring(i, i + 67);
                i = str.indexOf("报名号</strong>");
                final String bmh = str.substring(i + 13, i + 28).trim();
                current = "学&nbsp;&nbsp;&nbsp;号</strong>：";
                i = str.indexOf(current);
                final String xh = str.substring(i + current.length(), i + current.length() + 14).trim();
                i = str.indexOf("已经获得");
                int j = str.indexOf("</td>", i);

                final String xf = str.substring(i, j);

                LogUtils.i("pic:" + pic + "=bmh:" + bmh + "=xf=" + xf);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        homeName.setText(getResources().getString(R.string.home_name) + name);
                        homeBmh.setText(getResources().getString(R.string.home_bmh) + bmh);
                        homeXh.setText(getResources().getString(R.string.home_xh) + xh);
                        homeXf.setText(xf);
                        Glide.with(getContext())
                                .load(pic)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(homeImage);

                    }
                });
                if (!SharedPreferencesUtil.getBooleanData(getActivity(), "isNickname", false)) {
                    Observable.create(new Observable.OnSubscribe<Boolean>() {
                        @Override
                        public void call(Subscriber<? super Boolean> subscriber) {
                            subscriber.onNext(EMChatManager.getInstance().updateCurrentUserNick(name));

                        }
                    }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Boolean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    SharedPreferencesUtil.saveBooleanData(getActivity(), "isNickname", aBoolean);
                                }
                            });
                }


            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.i(getClass(), "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            ButterKnife.bind(this, view);
            homeName = (TextView) view.findViewById(R.id.home_name);
            homeBmh = (TextView) view.findViewById(R.id.home_bmh);
            homeXh = (TextView) view.findViewById(R.id.home_xh);
            homeXf = (TextView) view.findViewById(R.id.home_xf);
            homeImage = (ImageView) view.findViewById(R.id.home_image);
        }
        return view;
    }


}
