package me.leefeng.rxjava.main;

import me.leefeng.rxjava.main.bean.Version;

/**
 * Created by limxing on 2016/10/28.
 */

public interface MainView {
    void showLoading(String s);

    void showInfoWithStatus(String s);

    void showErrorWithStatus(String s);

    void showSuccessWithStatus(String s);

    void updateDialog(String s, String updateString);

    void updateCourse();

    void updateApp(Version version);

    void openApk();
}
