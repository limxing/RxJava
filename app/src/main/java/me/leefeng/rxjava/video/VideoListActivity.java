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
    private JSONArray jsonArray;
    private String str = "[[\"导言\"],[\"1.1 C语言发展历史\",\"1.2 C语言的特点\",\"1.3 简单的C程序介绍\",\"1.4 C程序的上机步骤\",\"1.5 补充知识点\"],[\"2.1 算法的概念\",\"2.2 简单算法举例\",\"2.3 算法的特性\",\"2.4.1 用自然语言表示算法\",\"2.4.2 用流程图表示算法\",\"2.4.3 三种基本结构和改进的流程图\",\"2.4.4 用N-S流程图表示算法\",\"2.4.5 用位代码表示算法\",\"2.5 结构化程序设计方法\"],[\"3.1 C的数据类型\",\"3.2 常量与变量\",\"3.3 整型数据\",\"3.4 浮点型数据运行\",\"3.5 字符型数据\",\"3.6 变量赋初值\",\"3.7 各类数值型数据间的混合运算\",\"3.8 算术运算符和算术表达式\",\"3.9 赋值运算符和赋值表达式\",\"3.10 逗号运算符和逗号表达式\"],[\"4.1 C语句概述\",\"4.2 赋值语句\",\"4.3 数据输入输出的概念及在C语言中的实现\",\"4.4 字符数据的输入输出 \",\"4.5 格式输入与输出\",\"4.6 顺序结构程序设计举例\"],[\"5.1 关系运算符和关系表达式\",\"5.2 逻辑运算符和逻辑表达式\",\"5.3 if语句\",\"5.4 switch语句\",\"5.5 程序举例\"],[\"6.1 概述\",\"6.2 goto语句以及用goto语句构成循环\",\"6.3 用while语句实现循环\",\"6.4 用do-while语句实现循环\",\"6.5 用for语句实现循环\",\"6.6 循环的嵌套\",\"6.7 几种循环的比较\",\"6.8 break语句和continue语句\",\"6.9 程序举例\",\"6.10 本章小结\"],[\"7.1.1 一维数组的定义\",\"7.1.2 一维数组元素的引用\",\"7.1.3 一维数组的初始化\",\"7.1.4 一维数组程序举例\",\"7.2.1 二维数组的定义\",\"7.2.2 二维数组的引用\",\"7.2.3 二维数组的引用\",\"7.2.4 二维数组程序举例\",\"7.3.1 字符数组的定义\",\"7.3.2 字符数组的初始化\",\"7.3.3 字符数组的引用\",\"7.3.4 字符串和字符串结束标志\",\"7.3.5 字符数组的输入输出\",\"7.3.6 字符串处理函数\",\"7.3.7 字符数组应用举例\"],[\"8.1 概述\",\"8.2 函数定义的一般形式\",\"8.3.1 形式参数和实际参数\",\"8.3.2 函数的返回值\",\"8.4.1 函数调用的一般形式\",\"8.4.2 函数调用的方式\",\"8.4.3 对被调用函数的声明和函数原型\",\"8.5 函数的嵌套调用\",\"8.6 函数的递归调用\",\"8.7 数组作为函数参数\",\"8.8 局部变量和全局变量\",\"8.9.1 动态存储方式与静态存储方\",\"8.9.2 auto变量\",\"8.9.3 用static声明局部变量\",\"8.9.4 register变量\",\"8.9.5 用extern声明外部变量\",\"8.9.6 用static声明外部变量\",\"8.9.7 关于变量的声明和定义\",\"8.9.8 存储类别小结\",\"8.10 内部函数和外部函数\"],[\"9.0 基本概念\",\"9.1 宏定义\",\"9.2 “文件包含”处理\",\"9.3 条件编译\"],[\"10.1 地址和指针的概念\",\"10.2.1 变量的指针和指向变量的指针变量(上)\",\"10.2.2 变量的指针和指向变量的指针变量(中)\",\"10.2.3 变量的指针和指向变量的指针变量（下）\",\"10.3.1 数组与指针（上） \",\"10.3.2 数组与指针（中） \",\"10.3.3 数组与指针（下） \",\"10.4 字符串与指针\",\"10.5 指向函数的指针\",\"10.6 返回指针值的函数\",\"10.7.1 指针数组和指向指针的指针（上）\",\"10.7.2 指针数组和指向指针的指针（下）\",\"10.8 有关指针的数据类型和指针运算的小结\"],[\"11.1 概述\",\"11.2 定义结构体类型变量的方法\",\"11.3 结构体变量的引用\",\"11.4 结构体变量的初始化\",\"11.5 结构体数组\",\"11.6 指向结构体类型数据的指针\",\"11.7.1 用指针处理链表（上）\",\"11.7.2 用指针处理链表（中）\",\"11.7.3 用指针处理链表（下）\",\"11.8 共用体\",\"11.9 枚举类型\",\"11.10 用typedef定义类型\"]]";
    private String[] list;

    @Override
    protected void initView() {
        TextView title_name = (TextView) findViewById(R.id.title_name);
        title_name.setText(getIntent().getStringExtra("title"));
        ListView video_list_listview = (ListView) findViewById(R.id.video_list_listview);
        list = getResources().getStringArray(R.array.language_c);
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
        intent.putExtra("index", i);
        intent.putExtra("title", list[i]);
        intent.putExtra("list", jsonArray.get(i).toString());
        startActivity(intent);
    }
}
