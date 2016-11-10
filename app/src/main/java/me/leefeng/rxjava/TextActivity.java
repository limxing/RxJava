package me.leefeng.rxjava;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import me.leefeng.itemswipemenu.ItemMenuView;

/**
 * Created by limxing on 2016/11/8.
 */

public class TextActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        LinearLayout text_root = (LinearLayout) findViewById(R.id.text_root);
        text_root.addView(new ItemMenuView(this,R.layout.activity_downloading_item));
    }
}
