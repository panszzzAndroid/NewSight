package com.panszzz.newsight;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.panszzz.newsight.news.News;
import com.panszzz.newsight.news.NewsAdapter;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CollectionFragment extends Fragment {

    private List<News> mNewsList;
    private CollectionAdapter adapter;
    private RecyclerView recyclerView;
    private String url_collection = "http://1.116.213.5:8080/reader/collection/getCollection?userUid=";

    private String userId;
    private String inters;


    private SpeechUtil mSpeechUtil;

    private TextView textId;
    private TextView textInter;

    private String result;
    private News news;

    private SmartRefreshLayout refreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化列表
        mNewsList = new ArrayList<>();
        // 初始化讯飞语音
        mSpeechUtil = SpeechUtil.getInstance(getContext());

        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,

        };
        String[] permissionsNeedToGrant = PermissionUtils.checkPermission((Activity) getContext(),
                permissions);
        if (permissionsNeedToGrant != null) {
            PermissionUtils.grantPermission(getActivity(), permissionsNeedToGrant,
                    PermissionUtils.REQUEST_GRANT_READ_AND_WRITE_EXTERNAL_STORAGE_PERMISSIONS);
        }
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        recyclerView = view.findViewById(R.id.recycler_collection);
        refreshLayout = view.findViewById(R.id.ly_refresh);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @org.jetbrains.annotations.NotNull RefreshLayout refreshLayout) {
                GetCollection();
                refreshLayout.finishRefresh();
                mSpeechUtil.speaking("刷新完成");
            }
        });


        //获取用户id和兴趣
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("userId",null);
        inters = sharedPreferences.getString("inters",null);
        //设置显示用户id和兴趣
        textId = view.findViewById(R.id.text_id);
        textId.setText(userId);
        textInter = view.findViewById(R.id.text_inter);
        textInter.setText(inters);

        //获取用户收藏信息
        GetCollection();




        return view;

    }


    private void GetCollection() {

        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url_collection+userId)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@CollectionFragment获取失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string();
                Log.d("@CollectionFragment获取成功：", "\n用户id"+userId+"\n获取收藏结果: " + result);
                //主线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!result.equals(""))
                            //开启子线程，更新UI
                            handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });



    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            {
                switch (msg.what){
                    case 0:
                        Log.d("@CollectionFragment子线程开始更新UI：", result);
                        if(result!=null)
                        {

                            mNewsList.clear();
                            Gson gson1 = new Gson();//创建Gson对象
                            news = gson1.fromJson(result,News.class);
                            try {


                                JSONObject jsonObject1 = new JSONObject(result);
                                String newsList = jsonObject1.getString("info");
                                Log.d("@CollectionFragment：", userId+"\n解析结果: " + newsList);
                                if(newsList.length()>15)
                                {
                                    com.google.gson.JsonParser jsonParser = new JsonParser();
                                    JsonArray jsonElements = jsonParser.parse(newsList).getAsJsonArray();//获取JsonArray对象

                                    for (JsonElement bean : jsonElements) {
                                        News news1 = gson1.fromJson(bean, News.class);//解析
                                        mNewsList.add(news1);
                                    }

                                }

                            }catch (JSONException e){

                            }
                            Log.d("@CollectionFragment：", userId+"\n解析长度: " + mNewsList.size());
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new CollectionAdapter(getContext(),mNewsList);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    default:break;
                }
            }
        }
    };


}
