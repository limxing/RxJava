package me.leefeng.rxjava;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.limxing.library.utils.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

//import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;
//import im.fir.sdk.FIR;
import me.leefeng.rxjava.download.DownLoadService;
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
    static Context appContext;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        startService(new Intent(this, DownLoadService.class));
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

//talkingdata统计
        TCAgent.init(this, "334836D7C79148F2ABE6078B09FF97BC", "QQ");

//短信
        SMSSDK.initSDK(this, "189b617b10eb4", "bbdaa1208d663cbc3b8b4f628ef39fd8");

//        Bmob.initialize(this, "0b9dc7e47883f0a6010b86fa5b9afb18");
        //bugly
        CrashReport.initCrashReport(getApplicationContext(), "c8b974cf55", false);
//必须首先执行这部分代码, 如果在":TCMSSevice"进程中，无需进行云旺（OpenIM）和app业务的初始化，以节省内存;
//        SysUtil.setApplication(this);
//        if (SysUtil.isTCMSServiceProcess(this)) {
//            return;
//        }
////第一个参数是Application Context
////这里的APP_KEY即应用创建时申请的APP_KEY，同时初始化必须是在主进程中
//        if (SysUtil.isMainProcess()) {
//            YWAPI.init(this, getString(R.string.Ali_APP_KEY));
//        }


        //环信初始化
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
//        options.setNumberOfMessagesLoaded(1);
//        ...
//初始化
        EMClient.getInstance().init(appContext, options);
//在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

//    /**
//     * 初始化OPENIM
//     * @param userID
//     */
//    public static void initOpenIm(String userID) {
//        imCore = YWAPI.createIMCore(userID, appContext.getString(R.string.Ali_APP_KEY));
//    }

    public static Context getContext() {
        return appContext;
    }
}
