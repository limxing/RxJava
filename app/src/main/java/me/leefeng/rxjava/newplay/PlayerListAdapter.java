package me.leefeng.rxjava.newplay;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.download.DownLoadManager;
import me.leefeng.rxjava.download.DownLoadService;
import me.leefeng.rxjava.download.TaskInfo;

/**
 * Created by limxing on 2016/11/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MasonryView>
        implements StickyRecyclerHeadersAdapter<PlayerListAdapter.MasonryView> {
    private final List<String> catelogue;
    private final ArrayList<PlayerItemBean> playerItemBeanList;
    private ArrayList<String> downloadList;
    private Drawable drawable;
    private Drawable drawable_pre;
    private boolean isDownloadController;
    private OnItemClickListener listener;
    private Drawable check_normal;
    private Drawable checked;


    public PlayerListAdapter(List<String> catelogue, ArrayList<PlayerItemBean> playerItemBeanList) {
        this.catelogue = catelogue;
        this.playerItemBeanList = playerItemBeanList;
          /*获取下载管理器*/
        DownLoadManager downLoadManager = DownLoadService.getDownLoadManager();
        /*断点续传需要服务器的支持，设置该项时要先确保服务器支持断点续传功能*/
        downLoadManager.setSupportBreakpoint(true);
        downloadList = downLoadManager.getAllTaskID();

    }

    @Override
    public long getHeaderId(int position) {
        String s = playerItemBeanList.get(position).getName().substring(0, 2);
        if (s.contains(".")) {
            s = String.valueOf(s.charAt(0));
        }
        return Long.parseLong(s) - 1;
    }

    @Override
    public void onBindHeaderViewHolder(MasonryView holder, int position) {

        holder.textView.setText(catelogue.get((int) getHeaderId(position)));
    }

    @Override
    public MasonryView onCreateHeaderViewHolder(ViewGroup parent) {
//        View text = View.inflate(parent.getContext(), R.layout.textview_item, null);
        TextView text = new TextView(parent.getContext());
        text.setWidth(parent.getResources().getDisplayMetrics().widthPixels);
        text.setBackgroundColor(Color.parseColor("#E0E0E0"));
        text.setTextSize(16);
        text.setPadding(25, 20, 0, 10);
        text.setSingleLine();
        return new MasonryView(text);
    }


    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {

//        View text = View.inflate(parent.getContext(), R.layout.textview_item, null);
        TextView text = new TextView(parent.getContext());
        if (drawable == null) {
            drawable = parent.getResources().getDrawable(R.drawable.play_normal);
            drawable_pre = parent.getResources().getDrawable(R.drawable.play_pressed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            drawable_pre.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            check_normal = parent.getResources().getDrawable(R.drawable.check_normal);
            checked = parent.getResources().getDrawable(R.drawable.checked);
            check_normal.setBounds(0, 0, check_normal.getMinimumWidth(), check_normal.getMinimumHeight());
            checked.setBounds(0, 0, check_normal.getMinimumWidth(), check_normal.getMinimumHeight());
        }
        text.setWidth(parent.getResources().getDisplayMetrics().widthPixels);
        text.setCompoundDrawablePadding(16);
        text.setGravity(Gravity.CENTER_VERTICAL);
        if (isDownloadController) {
            text.setCompoundDrawables(check_normal, null, null, null);
        } else {
            text.setCompoundDrawables(drawable, null, null, null);
        }
        text.setFocusable(true);
        text.setBackgroundResource(R.drawable.textview_bac);
        text.setTextSize(14);
        text.setPadding(60, 40, 0, 40);
        text.setSingleLine();

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, (Integer) view.getTag());
                }
            }
        });
        return new MasonryView(text);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
        PlayerItemBean playerItemBean = playerItemBeanList.get(position);
        holder.textView.setText(playerItemBean.getName());
        holder.textView.setTag(position);
        //判断是否存在这个下载任务
        if (isDownloadController&&(downloadList.contains(playerItemBean.getUrl())
                || new File(playerItemBean.getPath()).exists())) {
            holder.textView.setTextColor(Color.GRAY);
            playerItemBean.setChecked(false);
            holder.textView.setCompoundDrawables(checked, null, null, null);
            return;
        }
        if (isDownloadController) {
            holder.textView.setTextColor(Color.BLACK);
            if (playerItemBean.isChecked()) {
                holder.textView.setCompoundDrawables(checked, null, null, null);
            } else {
                holder.textView.setCompoundDrawables(check_normal, null, null, null);

            }
            return;
        }
        if (playerItemBean.isPlaying()) {
            holder.textView.setTextColor(Color.parseColor("#b21117"));
            holder.textView.setCompoundDrawables(drawable_pre, null, null, null);
        } else {
            holder.textView.setTextColor(Color.BLACK);
            holder.textView.setCompoundDrawables(drawable, null, null, null);
        }

    }

    @Override
    public int getItemCount() {
        return playerItemBeanList.size();
    }


    public static class MasonryView extends RecyclerView.ViewHolder {

        TextView textView;


        public MasonryView(View itemView) {
            super(itemView);
//            textView = (TextView) itemView.findViewById(R.id.textview_item_tv);
            textView = (TextView) itemView;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setDownloadController(boolean downloadController) {
        isDownloadController = downloadController;
    }
}
