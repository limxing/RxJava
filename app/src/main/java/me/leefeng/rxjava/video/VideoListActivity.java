package me.leefeng.rxjava.video;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import me.leefeng.rxjava.BeidaActivity;
import me.leefeng.rxjava.player.PlayerActivity;
import me.leefeng.rxjava.R;

/**
 * Created by limxing on 2016/10/26.
 */

public class VideoListActivity extends BeidaActivity implements AdapterView.OnItemClickListener {
    private String str = "[[\"导言 \"],[\"1.1 C语言发展历史 \",\"1.2 C语言的特点\",\"1.3 简单的C程序介绍\",\"1.4 C程序的上机步骤 \",\"补充知识点 \"],[\" 2.1 算法的概念\",\"2.2 简单算法举例\",\"2.3 算法的特性\",\"2.4.1 用自然语言表示算法\",\"2.4.2 用流程图表示算法\",\"2.4.3 三种基本结构和改进的流程图 \",\"2.4.4 用N-S流程图表示算法\",\"2.4.5 用位代码表示算法\",\" 2.5 结构化程序设计方法\"],[\"3.1 C的数据类型\",\"3.2 常量与变量 \",\"3.3 整型数据 \",\"3.4 浮点型数据运行\",\"3.5 字符型数据 \",\"3.6 变量赋初值 \",\"3.7 各类数值型数据间的混合运算 \",\"3.8 算术运算符和算术表达式\",\"3.9 赋值运算符和赋值表达式\",\"3.10 逗号运算符和逗号表达式\"],[\"4.1 C语句概述\",\"4.2 赋值语句\",\"4.3 数据输入输出的概念及在C语言中的实现\",\"4.4 字符数据的输入输出 \",\"4.5 格式输入与输出\",\"4.6 顺序结构程序设计举例 \"],[\"5.1 关系运算符和关系表达式 \",\"5.2 逻辑运算符和逻辑表达式\",\"5.3 if语句 \",\"5.4 switch语句\",\"5.5 程序举例 \"],[\"6.1 概述\",\"6.2 goto语句以及用goto语句构成循环 \",\"6.3 用while语句实现循环 \",\"6.4 用do-while语句实现循环\",\"6.5 用for语句实现循环\",\"6.6 循环的嵌套\",\"6.7 几种循环的比较\",\"6.8 break语句和continue语句\",\"6.9 程序举例 \",\"本章小结 \"]]";
    private JSONArray jsonArray;

    @Override
    protected void initView() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(getIntent().getStringExtra("title"));
        ListView video_list_listview = (ListView) findViewById(R.id.video_list_listview);
        String[] list = getResources().getStringArray(R.array.language_c);
        video_list_listview.setAdapter(new VideoListAdapter(list));
        video_list_listview.setOnItemClickListener(this);
        jsonArray = JSON.parseArray(str);
    }

    @Override
    protected int getView() {
        return R.layout.activity_videolist;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("index",i);
        intent.putExtra("list", jsonArray.get(i).toString());
        startActivity(intent);
    }
}
