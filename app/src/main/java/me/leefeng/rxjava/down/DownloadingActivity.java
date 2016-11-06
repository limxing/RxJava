package me.leefeng.rxjava.down;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.limxing.library.utils.LogUtils;
import com.limxing.library.utils.ToastUtils;

import java.util.ArrayList;

import me.leefeng.rxjava.BeidaSwipeActivity;
import me.leefeng.rxjava.R;
import me.leefeng.rxjava.download.DownLoadListener;
import me.leefeng.rxjava.download.DownLoadManager;
import me.leefeng.rxjava.download.DownLoadService;
import me.leefeng.rxjava.download.TaskInfo;
import me.leefeng.rxjava.download.dbcontrol.SQLDownLoadInfo;

/**
 * Created by limxing on 2016/11/4.
 */

public class DownloadingActivity extends BeidaSwipeActivity implements DownLoadListener {
    private RecyclerView recycleView;
    private DownloadingAdapter adapter;
    private DownLoadManager downManager;
    private ArrayList<TaskInfo> taskList;

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.title_name)).setText("下载管理");
        recycleView = (RecyclerView) findViewById(R.id.downloading_recycleview);
        downManager = DownLoadService.getDownLoadManager();
        taskList = downManager.getAllTask();
        LogUtils.i(this, taskList.size() + "");
        downManager.setAllTaskListener(this);
        adapter = new DownloadingAdapter(taskList);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected int getView() {
        return R.layout.activity_downloading;
    }


    @Override
    public void onStart(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(true);
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onProgress(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setDownFileSize(sqlDownLoadInfo.getDownloadSize());
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onStop(SQLDownLoadInfo sqlDownLoadInfo, boolean isSupportBreakpoint) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(false);
//                adapter.notifyItemChanged(i);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onError(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.get(i).setOnDownloading(false);
//                adapter.notifyItemChanged(i);
                ToastUtils.showShort(mContext,"文件不存在");
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onSuccess(SQLDownLoadInfo sqlDownLoadInfo) {
        for (int i = 0; i < taskList.size(); i++) {
            if (sqlDownLoadInfo.getTaskID().equals(taskList.get(i).getTaskID())) {
                taskList.remove(i);
//                adapter.notifyItemMoved(i, taskList.size());
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        downManager.removeAllDownLoadListener();
    }

    @Override
    protected void doReceiver(String action) {

    }
}
