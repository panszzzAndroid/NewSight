package com.panszzz.newsight.news;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.panszzz.newsight.R;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    private SpeechUtil mSpeechUtil;
    private WebView webView;
    private ImageView collectionBtn;

    private String webUrl = "";
    private String url = "http://1.116.213.5:8080/reader/query_news_details";
    private String url_collection = "http://1.116.213.5:8080/reader/collection/addCollection";
    private String json;
    private String result;

    private String memo;
    private String title;
    private String editor;
    private String time;
    private String newsUid;
    private String newsId;

    private SimpleDateFormat sdfStart;
    private SimpleDateFormat sdfEnd;
    private String timeStart;
    private String timeEnd;
    private Date dateStart;
    private Date dateEnd;
    private String userUid;

    /**
     * 手机振动器
     */
    private Vibrator vibrator;

    private boolean isColl = false;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        webView = findViewById(R.id.web);
        collectionBtn =findViewById(R.id.btn_coll);



        /***获取刚进来的时间***/
        dateStart = new Date(System.currentTimeMillis());

        //获取用户id
        SharedPreferences sharedPreferences= getSharedPreferences("user", Context.MODE_PRIVATE);
        userUid = sharedPreferences.getString("userId",null);
        //获取新闻的信息
        Intent intent = getIntent();
        newsUid  = intent.getStringExtra("newsUid");
        newsId  = intent.getStringExtra("newsId");
        memo  = intent.getStringExtra("memo");
        title  =intent.getStringExtra("title");
        editor =intent.getStringExtra("editor");
        time  = intent.getStringExtra("time");


        // 震动效果的系统服务
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 初始化讯飞语音
        mSpeechUtil = SpeechUtil.getInstance(getApplicationContext());

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
        String[] permissionsNeedToGrant = PermissionUtils.checkPermission(this,
                permissions);
        if (permissionsNeedToGrant != null) {
            PermissionUtils.grantPermission(this, permissionsNeedToGrant,
                    PermissionUtils.REQUEST_GRANT_READ_AND_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }




        findViewById(R.id.btn_back).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                /**获取退出去的时间**/
                dateEnd = new Date(System.currentTimeMillis());
                /**计算时间差**/
                long diff =(dateEnd.getTime() - dateStart.getTime())/1000;
//                Toast.makeText(NewsActivity.this,"停留时间:"+diff+"秒",Toast.LENGTH_SHORT).show();
                finish();
                mSpeechUtil.speaking("返回");

                if(isColl)
                {
                    collectNews();
                }
                return true;
            }
        });

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSpeechUtil.speaking("常按返回");
            }
        });

        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //该方法在Build.VERSION_CODES.LOLLIPOP以前有效，从Build.VERSION_CODES.LOLLIPOP起，建议使用shouldOverrideUrlLoading(WebView, WebResourceRequest)} instead
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242

                if (url.toString().contains("sina.cn")){
                    view.loadUrl(webUrl);
                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {
                //返回false，意味着请求过程里，不管有多少次的跳转请求（即新的请求地址），均交给webView自己处理，这也是此方法的默认处理
                //返回true，说明你自己想根据url，做新的跳转，比如在判断url符合条件的情况下，我想让webView加载http://ask.csdn.net/questions/178242
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("sina.cn")){
                        view.loadUrl(webUrl);
                        return true;
                    }
                }
                return false;
            }

        });


        //收藏按钮
        collectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isColl)
                {
                    isColl = false;
                    collectionBtn.setBackgroundResource(R.mipmap.ic_sc);
                    /*
                     * 震动的方式
                     */
                    // vibrator.vibrate(2000);//振动两秒
                    // 下边是可以使震动有规律的震动  -1：表示不重复 0：循环的震动
                    long[] pattern = { 200, 2000,200};
                    vibrator.vibrate(pattern, -1);
                }
                else
                {
                    isColl = true;
                    collectionBtn.setBackgroundResource(R.mipmap.ic_sc_n);
                    long[] pattern = { 200, 200,200,200,200 };
                    vibrator.vibrate(pattern, -1);
                }
            }
        });

        HashMap<String,String> map = new HashMap<>();
        map.put("newsUid",newsUid);
        map.put("userUid",userUid);
        Gson gson = new Gson();
        json = gson.toJson(map);
        GetNews();


        webView.loadUrl(webUrl);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(webUrl);

                mSpeechUtil.speaking(title+"发布时间"+time+"文章作者"+editor+memo);

            }
        }, 1500);//1秒后执行Runnable中的run方法

    }


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
                Log.d("@NewsActivity获取失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string();
                Log.d("@NewsActivity", "获取新闻："+json+"\n结果: " + result);

                /***
                 * 解析JSON
                 * **/
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String info = jsonObject.getString("info");
                    jsonObject = new JSONObject(info);
                    webUrl = jsonObject.getString("url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void collectNews()
    {
        HashMap<String,String> map = new HashMap<>();
        map.put("newsId",newsId);
        map.put("userUid",userUid);
        Gson gson = new Gson();
        final String json_coll = gson.toJson(map);
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json_coll);
        OkHttpClient okHttpClient = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url_collection)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@NewsActivity收藏失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Log.d("@NewsActivity", "收藏新闻："+json_coll+"\n结果: " + result);
            }
        });

    }
}