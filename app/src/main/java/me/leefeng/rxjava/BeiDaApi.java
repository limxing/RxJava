package me.leefeng.rxjava;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by limxing on 2016/10/26.
 */

public interface BeiDaApi {


    @FormUrlEncoded
    @POST()
    Observable<ResponseBody> getResponse(@Field("myID") String myID
            , @Field("myPW") String myPW, @Field("usersf") String usersf);

    @GET("/kcxx.asp")
    Observable<ResponseBody> test(@Header("Cookie") String cookie);
    @GET("/kcxx_jk_kcwz.asp")
    Observable<ResponseBody> test1(@Header("Cookie") String cookie,@Query("kcbh") String num);
}
