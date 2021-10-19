package com.panszzz.newsight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.panszzz.newsight.utils.SpeechUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class WriteActivity extends AppCompatActivity {

    private MediaPlayer mPlayer = null;
    private static final String TAG = "Search";
    private int ret = 0;    // 函数调用返回值


    // 语音听写对象
    private SpeechRecognizer mIat;

    private View search_gesture;
    private TextView mResultText;
    private Button recognize, stop, cancel;


    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResult = new LinkedHashMap<>();
    private StringBuffer buffer = new StringBuffer();

    private String mEngineType = SpeechConstant.TYPE_CLOUD; // 引擎类型
    private String resultType = "json"; // 语音听写返回类型

    private FragmentManager mFragmentManager;
    private Fragment mFragment = null;
    private SpeechUtil mSpeechUtil;
    private LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


        // 初始化识别无UI识别对象。使用SpeechRecognizer对象，可根据回调消息自定义界面
        mIat = SpeechRecognizer.createRecognizer(WriteActivity.this, mInitListener);
        mSpeechUtil = SpeechUtil.getInstance(getApplicationContext());

        //设置参数
        setParam();

        // 初始化界面
        initView();

        mSpeechUtil.speaking("双击屏幕下方开始语音识别");

//        mPlayer = MediaPlayer.create(this,R.raw.music);
        mPlayer.setLooping(false);

    }



    private void initView() {



        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeechUtil.speaking(getString(R.string.long_click_to_exit));
            }
        });
        findViewById(R.id.btn_back).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mSpeechUtil.speaking(getString(R.string.click_to_exit));
                finish();
                return true;
            }
        });

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.start();
                startRecognize();
            }
        });



        search_gesture = findViewById(R.id.search_gesture);
        search_gesture.setOnTouchListener(new MyClickListener
                (new MyClickListener.MyClickCallBack() {
                    @Override
                    public void oneClick() {
                        // 单击，语音合成
                        mSpeechUtil.speaking("双击开始语音识别");
                    }

                    @Override
                    public void doubleClick() {
//                        mSpeechUtil.speaking("开始识别");
                        // 双击，语音识别
                        startRecognize();
                        if (mResultText.getText() != null) {
                            // 更新搜索结果
                        }
                    }
                }));

        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            Log.i(TAG, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }

        mResultText = findViewById(R.id.tv_result);


    }

    private void startRecognize() {
        buffer.setLength(0);
        mResultText.setText(null);
        mIatResult.clear();

        // 不显示听写对话框
        ret = mIat.startListening(mRecognizeListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.i(TAG, "听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
        } else {
            Log.i(TAG, "请开始说话…");
        }
    }

    /**
     * 听写监听器
     */
    private RecognizerListener mRecognizeListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {
            Toast.makeText(WriteActivity.this, "开始播放", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            Toast.makeText(WriteActivity.this, "播放结束", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResult(RecognizerResult results, boolean b) {
            Log.i(TAG, results.getResultString());
            printResult(results);
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResult.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResult.keySet()) {
            resultBuffer.append(mIatResult.get(key)+",");
        }

        mResultText.setText(resultBuffer.toString());

//        mResultText.setSelection(mResultText.length());
    }

    /**
     * 设置参数
     */
    private void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎。TYPE_LOCAL表示本地，TYPE_CLOUD表示云端，TYPE_MIX 表示混合
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);

        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "5000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "5000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/recognize.wav");

    }


    /**
     * 初始化监听器
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Log.i(TAG, "初始化失败，错误码：" + code);
            }
        }
    };
}