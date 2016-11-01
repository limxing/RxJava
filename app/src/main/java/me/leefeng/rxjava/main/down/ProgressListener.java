package me.leefeng.rxjava.main.down;

/**
 * Created by limxing on 2016/11/1.
 */

public interface ProgressListener {
    void onProgress(long progress, long total, boolean done);
}
