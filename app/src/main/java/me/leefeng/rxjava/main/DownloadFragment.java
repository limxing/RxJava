package me.leefeng.rxjava.main;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class DownloadFragment extends Fragment {



    public DownloadFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        return view;
    }


}
