package me.leefeng.rxjava;

import android.app.Application;
import android.util.Log;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.bumptech.glide.Glide;
import com.limxing.library.utils.LogUtils;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent.UMAnalyticsConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import im.fir.sdk.FIR;
import me.leefeng.rxjava.main.Course;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by limxing on 2016/10/26.
 */

public class BeidaApplication extends Application {

    public static OkHttpClient okHttpClient;
    public static List<Course> cList;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.cookieJar(new CookieJar() {
            private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                for (Cookie cookie : cookies)
                    LogUtils.i(url + "==" + cookie.toString());
                cookieStore.put(url, cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        });
        builder.addInterceptor(new Interceptor() {
                                   @Override
                                   public Response intercept(Chain chain) throws IOException {
                                       Request request = chain.request();
                                       Log.i("limxingg", request.url().toString());
                                       Response response = chain.proceed(request);
                                       return response;
                                   }
                               }

        );
        okHttpClient = builder.build();

        FIR.init(this);
        TCAgent.init(this, "334836D7C79148F2ABE6078B09FF97BC", "QQ");


        final String APP_KEY = "23498343";
//必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
        SysUtil.setApplication(this);
        if (SysUtil.isTCMSServiceProcess(this)) {
            return;
        }
//第一个参数是Application Context
//这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
        if (SysUtil.isMainProcess()) {
            YWAPI.init((Application) getApplicationContext(), APP_KEY);
        }
    }
}
