package me.leefeng.rxjava.main;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.limxing.library.SVProgressHUD.SVProgressHUD;
import com.limxing.library.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import me.leefeng.rxjava.BeidaApplication;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
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
    private String updateString;

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
                    .observeOn(Schedulers.io())
                    .map(new Func1<ResponseBody, Integer>() {
                        @Override
                        public Integer call(ResponseBody responseBody) {
                            Integer integer = null;
                            try {
                                JSONObject obj = JSON.parseObject(responseBody.string());
                                integer = obj.getIntValue("course");
                                updateString = obj.getString("value");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return integer;
                        }
                    }).subscribeOn(Schedulers.io())
                    .map(new Func1<Integer, Boolean>() {
                        @Override
                        public Boolean call(Integer integer) {
                            Boolean b = null;
                            try {
                                b = JSON.parseObject(IOUtils.streamToString(new FileInputStream(couseFile)))
                                        .getIntValue("courseVersion") == integer.intValue();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                            return b;
                        }
                    }).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean != null && !aBoolean.booleanValue()) {
                                mainView.updateDialog("课程有更新", updateString);
                            } else {

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
}
