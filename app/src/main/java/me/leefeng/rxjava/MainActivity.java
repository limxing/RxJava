package me.leefeng.rxjava;

import android.Manifest;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.limxing.library.BaseActivity;
import com.limxing.library.Permission.CheckPermListener;
import com.limxing.library.Permission.EasyPermissions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;
import java.util.Scanner;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarDarkMode(true,this);
        setContentView(R.layout.activity_main);
        final ImageView drawable = (ImageView) findViewById(R.id.drawable);
//        简单数组打印
        String[] s = {"Hello", "Android", "JAVA", "OC", "Swift"};

        Observable.from(s)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.i("Main", s);
                    }
                });


//        加载图片

        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                try {
                    Thread.sleep(2000);
                    Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                    subscriber.onNext(drawable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Drawable s) {
                        drawable.setImageDrawable(s);
                    }
                });


//        网络请求
//        final OkHttpClient.Builder client = new OkHttpClient.Builder();
//        client.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                Log.i("leefengg", request.url().toString());
//                Response response = chain.proceed(request);
//                return response;
//            }
//        });
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(client.build())
////                .baseUrl("http://zhuangbi.info/")
//                .baseUrl("http://www.baidu.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//        Leefeng zhuangbiApi = retrofit.create(Leefeng.class);
//        zhuangbiApi.search("1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Leefeng.Api>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("leefengg", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<Leefeng.Api> strings) {
//                        Log.i("leefengg", strings.toString());
//                    }
//                });
////
//        zhuangbiApi.repoContributors2("square", "retrofit")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<List<Contributor>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("leefengg", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(List<Contributor> contributors) {
//                        Log.i("leefengg", contributors.toString());
//                    }
//                });
//
//        zhuangbiApi.getString("1")
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("leefengg", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.i("leefengg", s.toString());
//                    }
//                });


        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {

                    URL url = new URL("http://www.baidu.com");
                    Log.i("leefeng", "call:" + url.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoOutput(true);

                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    String result = readInStream(in);
                    Log.i("leefeng", "Input:" + result.toString());
                    subscriber.onNext(result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<String>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("leefengg", e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("leefengg", s);
                    }
                });

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.baidu.com")
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        retrofit.create(Leefeng.class).getResponse("http://www.baidu.com")
//                .subscribeOn(Schedulers.newThread())
//                .map(new Func1<ResponseBody, String>() {
//                    @Override
//                    public String call(ResponseBody responseBody) {
//
//                        return readInStream(responseBody.byteStream());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onStart() {
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("leefengg", e.toString());
//                    }
//
//                    @Override
//                    public void onNext(String s) {
//                        Log.i("leefengg", s + "?");
//                    }
//                });
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void init() {
        checkPermission(new CheckPermListener() {
            @Override
            public void superPermission() {

            }
        }, "", Manifest.permission.INTERNET);
    }


    public static class Contributor {
        public final String login;
        public final int contributions;

        public Contributor(String login, int contributions) {
            this.login = login;
            this.contributions = contributions;
        }

        @Override
        public String toString() {
            return "Contributor{" +
                    "login='" + login + '\'' +
                    ", contributions=" + contributions +
                    '}';
        }
    }

    private String readInStream(InputStream in) {
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    /**
     * 小米设置颜色
     * @param darkmode
     * @param activity
     */
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
