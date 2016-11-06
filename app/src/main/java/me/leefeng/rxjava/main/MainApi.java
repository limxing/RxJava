package me.leefeng.rxjava.main;

import me.leefeng.rxjava.main.bean.Version;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by limxing on 2016/10/28.
 */

public interface MainApi {
    // Observable 代表的是RxJava的执行
    @GET("/beida/course.json")
    Observable<ResponseBody> getCourse();

    @GET("/beida/version.json")
    Observable<Version> getVersion();


    @GET
    Observable<ResponseBody> getApp(@Url String url);
}
