package me.leefeng.rxjava.newplay;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.limxing.library.utils.DisplayUtil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.ArrayList;
import java.util.List;

import me.leefeng.rxjava.R;
import me.leefeng.rxjava.player.PlayerItemBean;

/**
 * Created by limxing on 2016/11/2.
 */

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MasonryView>
        implements StickyRecyclerHeadersAdapter<PlayerListAdapter.MasonryView> {
    private final List<String> catelogue;
    private final ArrayList<PlayerItemBean> playerItemBeanList;
    private Drawable drawable;
    private Drawable drawable_pre;


    public PlayerListAdapter(List<String> catelogue, ArrayList<PlayerItemBean> playerItemBeanList) {
        this.catelogue = catelogue;
        this.playerItemBeanList = playerItemBeanList;

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
        int f = (int) parent.getResources().getDisplayMetrics().density;
        TextView text = new TextView(parent.getContext());
        text.setWidth(parent.getResources().getDisplayMetrics().widthPixels);
        text.setBackgroundColor(Color.WHITE);
        text.setTextSize(6 * f);
        text.setPadding(10 * f, 10 * f, 0, 5 * f);
        text.setSingleLine();
        return new MasonryView(text);
    }


    @Override
    public MasonryView onCreateViewHolder(ViewGroup parent, int viewType) {
        int f = (int) parent.getResources().getDisplayMetrics().density;
        TextView text = new TextView(parent.getContext());
        if (drawable == null) {
            drawable = parent.getResources().getDrawable(R.drawable.play_normal);
            drawable_pre = parent.getResources().getDrawable(R.drawable.play_pressed);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            drawable_pre.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }

        text.setCompoundDrawablePadding(8 * f);
        text.setGravity(Gravity.CENTER);
        text.setCompoundDrawables(drawable, null, null, null);
        text.setBackgroundColor(Color.WHITE);
        text.setTextSize(5 * f);
        text.setPadding(25 * f, 10 * f, 0, 20 * f);
        text.setSingleLine();
        return new MasonryView(text);
    }

    @Override
    public void onBindViewHolder(MasonryView holder, int position) {
       PlayerItemBean playerItemBean= playerItemBeanList.get(position);
        holder.textView.setText(playerItemBean.getName());
        if (playerItemBean.isPlaying()){
            holder.textView.setTextColor(Color.parseColor("#b21117"));
            holder.textView.setCompoundDrawables(drawable_pre,null,null,null);
        }else{
            holder.textView.setTextColor(Color.BLACK);
            holder.textView.setCompoundDrawables(drawable,null,null,null);
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
            textView = (TextView) itemView;
        }

    }
}
