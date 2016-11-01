package me.leefeng.rxjava.main.down;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by ljd on 3/29/16.
 */
public interface DownloadApi {

    @GET
    Call<ResponseBody> retrofitDownload(@Url String url);
}
