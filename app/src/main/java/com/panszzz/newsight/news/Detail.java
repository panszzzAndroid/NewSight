package com.panszzz.newsight.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.panszzz.newsight.MyClickListener;
import com.panszzz.newsight.R;
import com.panszzz.newsight.utils.SpeechUtil;

/*
单击屏幕控制当前播放的暂停继续
双击屏幕重新开始播放
按返回键退出当前新闻界面
*/

public class Detail extends AppCompatActivity {
    public static final String TAG = "Detail";
    private SpeechUtil mSpeechUtil;

    private Button speak, pause;
    private TextView mTextView;
    private View detail_gesture;

    private String news;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        // 初始化讯飞语音
        mSpeechUtil = SpeechUtil.getInstance(getApplicationContext());

        initView();
    }

    private void initView() {
        Intent i = getIntent();
        news = i.getStringExtra("news_3");

        mTextView = findViewById(R.id.news_detail);
        mTextView.setText(news);

        mSpeechUtil.speaking(news);

        detail_gesture = findViewById(R.id.detail_gesture);
        detail_gesture.setOnTouchListener(new MyClickListener
                (new MyClickListener.MyClickCallBack() {
                    @Override
                    public void oneClick() {
                        if (SpeechUtil.isSpeaking()) {
                            if (!isPaused) {
                                mSpeechUtil.pause();
                                isPaused = true;
                            } else {
                                mSpeechUtil.resume();
                                isPaused = false;
                            }
                        }
                    }

                    @Override
                    public void doubleClick() {
                        mSpeechUtil.speaking(news);
                        isPaused = false;
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        SpeechUtil.stopSpeaking();
        super.onDestroy();
    }

}
