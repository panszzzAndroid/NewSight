package com.panszzz.newsight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.xubo.linescrollviewlib.LineScrollView;

import java.util.ArrayList;
import java.util.List;


public class TestActivity extends AppCompatActivity {


    Button btn1;
    Button btn2;
    Button btn3;
    LineScrollView linelist_lsv;
    List<String> lineList = new ArrayList<String>();
    List<String> lineList2 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        linelist_lsv = findViewById(R.id.linelist_lsv);
        initData();

        linelist_lsv.setLines(lineList);

    }


    private void initData() {
        lineList.add("手机号为1861****100用户已经中奖");
        lineList.add("手机号为1861****101用户已经中奖");
        lineList.add("手机号为1861****102用户已经中奖");
        lineList.add("手机号为1861****103用户已经中奖");
        lineList.add("手机号为1861****104用户已经中奖");
        lineList.add("手机号为1861****105用户已经中奖");
        lineList.add("手机号为1861****106用户已经中奖");
        lineList.add("手机号为1861****107用户已经中奖");
        lineList.add("手机号为1861****108用户已经中奖");
        lineList.add("手机号为1861****109用户已经中奖");
        lineList.add("手机号为1861****110用户已经中奖");

        lineList2.add("数据要变更啦--变更内容1");
        lineList2.add("数据要变更啦--变更内容2");
        lineList2.add("数据要变更啦--变更内容3");
        lineList2.add("数据要变更啦--变更内容4");
        lineList2.add("数据要变更啦--变更内容5");
        lineList2.add("数据要变更啦--变更内容6");
        lineList2.add("数据要变更啦--变更内容7");
    }
}