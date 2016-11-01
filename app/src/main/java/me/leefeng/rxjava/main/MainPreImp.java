package me.leefeng.rxjava.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.limxing.library.SVProgressHUD.SVProgressHUD;
import com.limxing.library.utils.IOUtils;
import com.limxing.library.utils.ToastUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.leefeng.rxjava.BeidaApplication;
import me.leefeng.rxjava.BeidaData;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.main.bean.Version;
import me.leefeng.rxjava.main.down.DownloadApi;
import me.leefeng.rxjava.main.down.DownloadProgressHandler;
import me.leefeng.rxjava.main.down.ProgressHelper;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by limxing on 2016/10/28.
 */

public class MainPreImp implements MainPre {
    private MainView mainView;
    private final String version = "version.json";
    private final String course = "course.json";
    private MainApi mainApi;
    private File couseFile;

    public MainPreImp(MainView mainView) {
        this.mainView = mainView;
        initUpdata();
    }

    private void initUpdata() {
        couseFile = new File(((Activity) mainView).getCacheDir(), course);
        mainApi = new Retrofit.Builder()
                .client(BeidaApplication.okHttpClient)
                .baseUrl("http://leefeng.me")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(MainApi.class);
        if (couseFile.exists()) {

            mainApi.getVersion()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Version>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Version version) {
                            version.setCourse(2);
                            version.setTitle("课程有更新");
                            version.setValue("添加了毛概");
                            version.setUrl("http://www.leefeng.me/download/beidacourse1.1.apk");
                            try {
                                if (version.getVersion() > ((Activity) mainView).getPackageManager().
                                        getPackageInfo(((Activity) mainView).getPackageName(), 0).versionCode) {
                                    mainView.updateApp(version);
                                    return;
                                }
                                if (JSON.parseObject(IOUtils.streamToString(new FileInputStream(couseFile)))
                                        .getIntValue("courseVersion") < version.getCourse())
                                    mainView.updateDialog(version.getTitle(), version.getValue());

                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    });

        } else {
            upDateCouse();
        }

    }

    public void upDateCouse() {
        mainView.showLoading("正在更新课程");
        mainApi.getCourse()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<ResponseBody, Integer>() {
                    @Override
                    public Integer call(ResponseBody responseBody) {
                        int i = 0;
                        try {
                            FileOutputStream fout = new FileOutputStream(couseFile);
//                                openFileOutput(couseFile.toString(), MODE_PRIVATE);
                            byte[] bytes = responseBody.bytes();
                            fout.write(bytes);
                            fout.close();
                            i = 1;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new Integer(i);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mainView.showErrorWithStatus("更新失败，请稍后重试");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.intValue() == 1) {
                            mainView.showSuccessWithStatus("课程更新完成");
                            mainView.updateCourse();
                        } else {
                            mainView.showErrorWithStatus("更新失败，请稍后重试");
                        }
                    }
                });

    }


    @Override
    public void destory() {
        mainView = null;
    }

    public void updateAppThread(Version version) {
        retrofitDownload(version.getUrl());
    }


    /**
     * 请求下载
     *
     * @param url
     */
    //        mainApi.getApp(version.getUrl())
    private void retrofitDownload(String url) {
        //监听下载进度
        final ProgressDialog dialog = new ProgressDialog((Context) mainView);
        dialog.setProgressNumberFormat("%1d KB/%2d KB");
        dialog.setTitle("下载新版本");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setIcon(R.mipmap.ic_launcher);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.show();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.leefeng.me");

        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        DownloadApi retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                dialog.setMax((int) (contentLength / 1024));
                dialog.setProgress((int) (bytesRead / 1024));

                if (done) {
                    dialog.dismiss();
                    mainView.openApk();
                }
            }
        });

        Call<ResponseBody> call = retrofit.retrofitDownload(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    File file = BeidaData.FILE_DOWN_APK;
                    if (file.exists()) {
                        file.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                ToastUtils.showLong((Context) mainView, "下载失败，请稍后再试");
            }
        });

    }

}
