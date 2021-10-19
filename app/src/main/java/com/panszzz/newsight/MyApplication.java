package com.panszzz.newsight;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(MyApplication.this, SpeechConstant.APPID + "=5d101f6f");
        super.onCreate();
    }
}
