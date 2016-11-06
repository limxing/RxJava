package me.leefeng.rxjava;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by limxing on 2016/10/21.
 */

public interface Leefeng {
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<MainActivity.Contributor>> contributors(
            @Path("owner") String owner,
            @Path("repo") String repo);

    // Observable 代表的是RxJava的执行
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<MainActivity.Contributor>> repoContributors2(
            @Path("owner") String owner,
            @Path("repo") String repo);


    @GET
    Observable<ResponseBody> getResponse(@Url String fileUrl);


    // Observable 代表的是RxJava的执行
    @GET("search")
    Observable<String> getString(@Query("q") String query);



    @GET("search")
    Observable<List<Api>> search(@Query("q") String query);

    public class Api {
        int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Api{" +
                    "id=" + id +
                    '}';
        }
    }


}
