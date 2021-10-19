package com.panszzz.newsight.utils;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

/**
 * 采用单例模式
 */
public class SpeechUtil {
    private static final String TAG = "SpeechUtil";
    private static SpeechSynthesizer mTts;
    private Context mContext;
    private volatile static SpeechUtil sSpeechUtil;

    /**
     * 合成回调监听
     */
    private static SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "开始播放");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.d(TAG, "缓冲: " + i);
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            Log.d(TAG, "合成: " + i);
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            if (speechError == null) {
                Log.d(TAG, "播放完成");
            } else {
                Log.d(TAG, speechError.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    /**
     * 构造方法
     */
    private SpeechUtil(Context context) {
        mContext = context;
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int i) {
                if (i != ErrorCode.SUCCESS) {
                    Log.d("fjj", "初始化失败: " + i);
                } else {
                    Log.d("fjj", "初始化成功: " + i);
                }
            }
        });
        // 初始化参数
        setParam();
    }

    public static SpeechUtil getInstance(Context context) {
        if (sSpeechUtil == null) {
            synchronized (SpeechUtil.class) {
                if (sSpeechUtil == null) {
                    sSpeechUtil = new SpeechUtil(context);
                }
            }
        }
        return sSpeechUtil;
    }

    /**
     * 停止语音播报
     */
    public static void stopSpeaking() {
        if (null != mTts && mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
    }

    /**
     * 判断当前有没有说话
     */
    public static boolean isSpeaking() {
        if (mTts != null) {
            return mTts.isSpeaking();
        } else {
            return false;
        }
    }

    /**
     * 开始合成，并播报出来
     */
    public void speaking(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        int code = mTts.startSpeaking(text.trim(), mTtsListener);
        Log.d("fjj", "------" + code + "---------");

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                Toast.makeText(mContext, "没有安装语音 code = " + code, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "语音合成失败，错误码: " + code, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 暂停合成
     */
    public void pause() {
        mTts.pauseSpeaking();
    }

    /**
     * 继续合成
     */
    public void resume() {
        mTts.resumeSpeaking();
    }

    /**
     * 初始化参数
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 引擎类型 --- 这里采用云端
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        // 设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        // 设置语速
        mTts.setParameter(SpeechConstant.SPEED, "60");
        // 设置音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        // 设置音量
        mTts.setParameter(SpeechConstant.VOLUME, "100");
        // 设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
    }
}