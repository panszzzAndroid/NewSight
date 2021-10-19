package com.panszzz.newsight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.panszzz.newsight.news.News;
import com.panszzz.newsight.news.NewsAdapter;
import com.panszzz.newsight.news.NewsFragment;
import com.panszzz.newsight.utils.SpeechUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "Search";
    private int ret = 0;    // 函数调用返回值

    private FragmentManager mFragmentManager;
    private Fragment mFragment = null;
    private SpeechUtil mSpeechUtil;
    private LinearLayout linearLayout;

    private ImageView imageView_search;
    // 语音听写对象
    private SpeechRecognizer mIat;

    private View search_gesture;
    private TextView mResultText;
    private TextView textResult;
    private Button recognize, stop, cancel;


    private String url = "http://1.116.213.5:8080/reader/query_by_name";
    private String json;
    private String result;

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> mNewsList;
    private News news;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResult = new LinkedHashMap<>();
    private StringBuffer buffer = new StringBuffer();

    private String mEngineType = SpeechConstant.TYPE_CLOUD; // 引擎类型
    private String resultType = "json"; // 语音听写返回类型

    /**
     * 手机振动器
     */
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        linearLayout = findViewById(R.id.layout);
        imageView_search = findViewById(R.id.search_gesture);
        recyclerView = findViewById(R.id.recycler_view);
        textResult = findViewById(R.id.text_result);

        mNewsList = new ArrayList<>();
        // 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 初始化识别无UI识别对象。使用SpeechRecognizer对象，可根据回调消息自定义界面
        mIat = SpeechRecognizer.createRecognizer(SearchActivity.this, mInitListener);
        mSpeechUtil = SpeechUtil.getInstance(getApplicationContext());

        //设置参数
        setParam();

        // 初始化界面
        initView();

        mSpeechUtil.speaking("双击屏幕下方开始语音识别");




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


        mFragmentManager = getSupportFragmentManager();
        mFragment = mFragmentManager.findFragmentById(R.id.container);

        if (mFragment == null) {
            mFragment = new NewsFragment();
            mFragmentManager.beginTransaction().add(R.id.container, mFragment)
                    .commit();
        }

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
                        //mSpeechUtil.speaking("开始识别");
                        // 双击，语音识别
                        /*
                         * 震动的方式
                         */
                        // vibrator.vibrate(2000);//振动两秒
                        // 下边是可以使震动有规律的震动  -1：表示不重复 0：循环的震动
                        long[] pattern = { 200, 2000, 2000, 200, 200, 200 };
                        vibrator.vibrate(pattern, -1);
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
        recognize = findViewById(R.id.recognize);
        recognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognize();
            }
        });

        stop = findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIat.stopListening();
                Log.i(TAG, "停止听写");
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIat.cancel();
                Log.i(TAG, "取消听写");
            }
        });

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
            Toast.makeText(SearchActivity.this, "开始说话", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            Toast.makeText(SearchActivity.this, "结束说话", Toast.LENGTH_SHORT).show();
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
        final String text = JsonParser.parseIatResult(results.getResultString());

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
            resultBuffer.append(mIatResult.get(key));
        }

        mResultText.setText(resultBuffer.toString());

        /**
         * 上传搜索结果
         * */
        HashMap<String,String> map = new HashMap<>();
        map.put("newsName",resultBuffer.toString());
        Gson gson = new Gson();
        json = gson.toJson(map);
        GetNews();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 *要执行的操作
                 */
                if(result!=null)
                {

                    mNewsList.clear();
                    Gson gson1 = new Gson();//创建Gson对象
                    news = gson1.fromJson(result, News.class);
                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String info = jsonObject.getString("info");

                        JSONObject jsonObject = new JSONObject(result);
                        String newsList = jsonObject.getString("info");

                        Log.d("info", "onResponse: " + newsList);
                        com.google.gson.JsonParser jsonParser = new com.google.gson.JsonParser();
                        JsonArray jsonElements = jsonParser.parse(newsList).getAsJsonArray();//获取JsonArray对象
                        for (JsonElement bean : jsonElements) {
                            News news1 = gson1.fromJson(bean, News.class);//解析
                            mNewsList.add(news1);
                        }

                    }catch (JSONException e){

                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new NewsAdapter(SearchActivity.this,mNewsList);
                    recyclerView.setAdapter(adapter);
                    if(mNewsList.size()>0)
                    {
                        mSpeechUtil.speaking("搜索成功！");
                        textResult.setVisibility(View.GONE);



                    }
                    else
                    {
                        mSpeechUtil.speaking("查找不到相关新闻！");
                        textResult.setVisibility(View.VISIBLE);

                    }
                    adapter.notifyDataSetChanged();





                }
            }
        }, 2500);//2秒后执行Runnable中的run方法
//        mSpeechUtil.speaking("8次“新年第一课”，总书记讲了啥？一起来做学习笔记。2021年1月11日，习近平总书记赴中央党校，出席省部级主要领导干部学习贯彻党的十九届五中全会精神专题研讨班开班式，并为大家讲了“新年第一课”。");
//        imageView_search.setVisibility(View.GONE);
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
        mIat.setParameter(SpeechConstant.VAD_BOS, "2000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "900");
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


    private void GetNews() {

        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        OkHttpClient okHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@SearchActivity搜索失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string();
                Log.d("@SearchActivity", "搜索新闻结果: " + result);
            }
        });

    }
}