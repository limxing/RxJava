package me.leefeng.rxjava.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class HomeFragment extends Fragment {


    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity activity = (MainActivity) getActivity();
        TextView home_name = (TextView) view.findViewById(R.id.home_name);
        home_name.setText(getResources().getString(R.string.home_name) + activity.name);
        TextView home_bmh = (TextView) view.findViewById(R.id.home_bmh);
        home_bmh.setText(getResources().getString(R.string.home_bmh) + activity.bmh);
        TextView home_xh = (TextView) view.findViewById(R.id.home_xh);
        home_xh.setText(getResources().getString(R.string.home_xh) + activity.xh);
        ImageView home_image = (ImageView) view.findViewById(R.id.home_image);
        Glide
                .with(activity)
                .load(activity.pic)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(home_image);
        return view;
    }


}
