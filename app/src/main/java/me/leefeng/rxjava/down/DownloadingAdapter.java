package me.leefeng.rxjava.down;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.limxing.library.utils.StringUtils;
import com.limxing.library.utils.ToastUtils;

import java.util.ArrayList;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.download.DownLoadListener;
import me.leefeng.rxjava.download.DownLoadManager;
import me.leefeng.rxjava.download.DownLoadService;
import me.leefeng.rxjava.download.TaskInfo;
import me.leefeng.rxjava.download.dbcontrol.SQLDownLoadInfo;

/**
 * Created by limxing on 2016/11/4.
 */

public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.DownItemView> {

    private ArrayList<TaskInfo> taskList;

    public DownloadingAdapter(ArrayList<TaskInfo> taskList) {
        this.taskList = taskList;

    }

    @Override
    public DownItemView onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_downloading_item, null);


        return new DownItemView(convertView);
    }

    @Override
    public void onBindViewHolder(DownItemView holder, int position) {
        TaskInfo taskInfo = taskList.get(position);
        holder.name.setText(taskInfo.getFileName());
        holder.pb.setProgress(taskInfo.getProgress());
        holder.size.setText(StringUtils.formatFileSize(taskInfo.getDownFileSize()) + "/" + StringUtils.formatFileSize(taskInfo.getFileSize()));
        holder.jindu.setText(taskInfo.getProgress() + "%");
        holder.cancle.setTag(position);
        holder.down.setTag(position);

    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


    public class DownItemView extends RecyclerView.ViewHolder {
        Button down;
        TextView name;
        ProgressBar pb;
        TextView size;
        TextView jindu;
        Button cancle;

        public DownItemView(View convertView) {
            super(convertView);
            name = (TextView) convertView.findViewById(R.id.download_item_name);
            size = (TextView) convertView.findViewById(R.id.download_item_size);
            jindu = (TextView) convertView.findViewById(R.id.download_item_jindu);
            pb = (ProgressBar) convertView.findViewById(R.id.download_item_progressbar);
            cancle = (Button) convertView.findViewById(R.id.down_item_cancle);
            down = (Button) convertView.findViewById(R.id.down_item_download);
            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!taskList.get((Integer) view.getTag()).isOnDownloading()) {
                        DownLoadService.getDownLoadManager().startTask(taskList.get((Integer) view.getTag()).getTaskID());
                    } else {
                        ToastUtils.showShort(view.getContext(), "正在下载");
                    }

                }
            });
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DownLoadService.getDownLoadManager().deleteTask(taskList.get((Integer) view.getTag()).getTaskID());
                    taskList.remove(taskList.get((Integer) view.getTag()));
//                    notifyItemMoved((Integer) view.getTag(), taskList.size());
                    notifyDataSetChanged();
                }
            });
        }

    }
}
