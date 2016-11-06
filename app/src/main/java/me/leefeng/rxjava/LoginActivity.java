package me.leefeng.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.limxing.library.BaseActivity;
import com.limxing.library.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/26.
 */

public class LoginActivity extends AppCompatActivity {
    private WebView webView;
    private BeiDaApi beiDaApi;
//    private OkHttpClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        webView = (WebView) findViewById(R.id.webView);
//        webView.getSettings() .setJavaScriptEnabled(true);





        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://learn.pkudl.cn")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(BeidaApplication.okHttpClient)
                .build();
        beiDaApi = retrofit.create(BeiDaApi.class);

        okhttpTest(BeidaApplication.okHttpClient);


    }

    public void kcxx(View view) {

        List<Cookie> cookies=BeidaApplication.okHttpClient.cookieJar().loadForRequest(HttpUrl.parse("http://learn.pkudl.cn/"));
        beiDaApi.test1(cookieHeader(cookies),"08281014")

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("onError" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String str = readInStream(responseBody.byteStream());
                        LogUtils.i("onNext" + str);
                        webView.loadDataWithBaseURL("http://learn.pkudl.cn", str, "text/html", "utf-8",
                                null);
                    }
                });
    }

    /**
     * 登录
     * @param client
     */
    void okhttpTest(OkHttpClient client) {
        RequestBody formBody = new FormBody.Builder()
                .add("myID", "616390000002")
                .add("myPW", "woaifeng521")
                .add("usersf", "1")
                .build();

        Call call = client.newCall(new Request.Builder()
                .url("http://learn.pkudl.cn")
                .post(formBody)
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("limxingg", "onFailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadDataWithBaseURL("http://learn.pkudl.cn", str.toString(), "text/html", "utf-8",
                                null);
                    }
                });


                Log.i("limxingg", str);
            }
        });
    }

    void test1(BeiDaApi beiDaApi) {
        beiDaApi.getResponse("616390000002", "woaifeng521", "1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("onError" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String str = readInStream(responseBody.byteStream());
                        LogUtils.i("onNext" + str);
                        webView.loadDataWithBaseURL("http://learn.pkudl.cn", str, "text/html", "utf-8",
                                null);
                    }
                });
    }

    void test2(BeiDaApi beiDaApi) {
        beiDaApi.test("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.i("onError" + e.toString());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        LogUtils.i("onNext" + readInStream(responseBody.byteStream()));
                    }
                });
    }

    private String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        for (int i = 0, size = cookies.size(); i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }
}
