package com.panszzz.newsight.news;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.panszzz.newsight.R;
import com.panszzz.newsight.SearchActivity;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xubo.linescrollviewlib.LineScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsFragment extends Fragment {
    private List<News> mNewsList;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private Banner mBanner;
    private String url_id = "http://1.116.213.5:8080/reader/query_news_list";
    private String url_type = "http://1.116.213.5:8080/reader/query_news_by_type";
    private String result;
    private News news;
    private SpeechUtil mSpeechUtil;
    private TranslateAnimation translateAniShow, translateAniHide;

    private RadioButton mItemBtn0;
    private RadioButton mItemBtn1;
    private RadioButton mItemBtn2;
    private RadioButton mItemBtn3;
    private RadioButton mItemBtn4;
    private RadioButton mItemBtn5;
    private RadioButton mItemBtn6;
    private RadioButton mItemBtn7;
    private RadioButton mItemBtn8;
    private RadioButton mItemBtn9;
    private RadioButton mItemBtn10;
    private RadioButton mItemBtn11;
    private RadioButton mItemBtn12;
    private RadioButton mItemBtn13;
    private RadioButton mItemBtn14;
    private RadioButton mItemBtnHome;

    private SmartRefreshLayout refreshLayout;

    private boolean bannerIsG = true;

    private int index = 1;

    private boolean isByType = false;
    private int typePosition = 0;


    private LineScrollView lineScrollView;
    private List<String> lineList = new ArrayList<String>();

    private String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsList = new ArrayList<>();


        //????????????id
        SharedPreferences sharedPreferences= getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userId = sharedPreferences.getString("userId",null);

        // ?????????????????????
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        //??????????????????
        translateAnimation();

        mBanner = view.findViewById(R.id.banner);

        refreshLayout = view.findViewById(R.id.ly_refresh);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                index++;
                if(isByType)
                {
                    NewsByType(typePosition);
                }
                else
                {
                    NewsByUserId(userId);
                }
                refreshLayout.finishRefresh();
                mSpeechUtil.speaking("????????????");
            }
        });

        mItemBtn0 = view.findViewById(R.id.item_btn_0);
        mItemBtn1 = view.findViewById(R.id.item_btn_1);
        mItemBtn2 = view.findViewById(R.id.item_btn_2);
        mItemBtn3 = view.findViewById(R.id.item_btn_3);
        mItemBtn4 = view.findViewById(R.id.item_btn_4);
        mItemBtn5 = view.findViewById(R.id.item_btn_5);
        mItemBtn6 = view.findViewById(R.id.item_btn_6);
        mItemBtn7 = view.findViewById(R.id.item_btn_7);
        mItemBtn8 = view.findViewById(R.id.item_btn_8);
        mItemBtn9 = view.findViewById(R.id.item_btn_9);
        mItemBtn10 = view.findViewById(R.id.item_btn_10);
        mItemBtn11 = view.findViewById(R.id.item_btn_11);
        mItemBtn12 = view.findViewById(R.id.item_btn_12);
        mItemBtn13 = view.findViewById(R.id.item_btn_13);
        mItemBtn14 = view.findViewById(R.id.item_btn_14);
        mItemBtnHome = view.findViewById(R.id.item_btn_home);

        //?????????????????????
        final RadioGroup mRadioGroup = view.findViewById(R.id.navigation);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                radioGroup.check(i);

                switch (i) {
                    case R.id.item_btn_0:
                        //mViewPager.setCurrentItem(0, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_0));
                        mItemBtn0.setTextSize(24);bannerGone();isByType=true;typePosition=0;
                        NewsByType(0);
                        break;
                    case R.id.item_btn_1:
                        //mViewPager.setCurrentItem(0, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_1));
                        mItemBtn1.setTextSize(24);isByType=true;typePosition=1;
                        bannerGone();
                        NewsByType(1);
                        break;
                    case R.id.item_btn_2:
                        //mViewPager.setCurrentItem(1, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_2));
                        mItemBtn2.setTextSize(24);bannerGone();NewsByType(2);isByType=true;typePosition=2;
                        break;
                    case R.id.item_btn_3:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_3));
                        mItemBtn3.setTextSize(24);bannerGone();NewsByType(3);isByType=true;typePosition=3;
                        break;
                    case R.id.item_btn_4:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_4));
                        mItemBtn4.setTextSize(24);bannerGone();NewsByType(4);isByType=true;typePosition=4;
                        break;
                    case R.id.item_btn_5:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_5));
                        mItemBtn5.setTextSize(24);bannerGone();NewsByType(5);isByType=true;typePosition=5;
                        break;
                    case R.id.item_btn_6:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_6));
                        mItemBtn6.setTextSize(24);bannerGone();NewsByType(6);isByType=true;typePosition=6;
                        break;
                    case R.id.item_btn_7:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_7));
                        mItemBtn7.setTextSize(24);bannerGone();NewsByType(7);isByType=true;typePosition=7;
                        break;
                    case R.id.item_btn_8:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_8));
                        mItemBtn8.setTextSize(24);bannerGone();NewsByType(8);isByType=true;typePosition=8;
                        break;
                    case R.id.item_btn_9:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_9));
                        mItemBtn9.setTextSize(24);bannerGone();NewsByType(9);isByType=true;typePosition=9;
                        break;
                    case R.id.item_btn_10:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_10));
                        mItemBtn10.setTextSize(24);bannerGone();NewsByType(10);isByType=true;typePosition=10;
                        break;
                    case R.id.item_btn_11:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_11));
                        mItemBtn11.setTextSize(24);bannerGone();NewsByType(11);isByType=true;typePosition=11;
                        break;
                    case R.id.item_btn_12:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_12));
                        mItemBtn12.setTextSize(24);bannerGone();NewsByType(12);isByType=true;typePosition=12;
                        break;
                    case R.id.item_btn_13:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_13));
                        mItemBtn13.setTextSize(24);bannerGone();NewsByType(13);isByType=true;typePosition=13;
                        break;
                    case R.id.item_btn_14:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking(getString(R.string.interest_14));
                        mItemBtn14.setTextSize(24);bannerGone();NewsByType(14);isByType=true;typePosition=14;
                        break;
                    case R.id.item_btn_home:
                        //mViewPager.setCurrentItem(2, true);
                        ItemsSamll();mSpeechUtil.speaking("??????");
                        mItemBtnHome.setTextSize(24);bannerVisible();NewsByUserId(userId);isByType=false;
                        break;
                }
            }
        });
        mItemBtnHome.setChecked(true);//????????????????????????

        view.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpeechUtil.speaking(getString(R.string.long_click_to_search));
            }
        });
        view.findViewById(R.id.btn_search).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                return true;
            }
        });



        lineScrollView = view.findViewById(R.id.linelist_lsv);

        lineScrollView.setLines(lineList);



        recyclerView = view.findViewById(R.id.recycler_view);

        //NewsByUserId(userId);


        return view;
    }

    private void NewsByUserId(final String userId) {
        mNewsList.clear();

        HashMap<String,String> map = new HashMap<>();
        map.put("index",index+"");
        map.put("userUid",userId);
        Gson gson = new Gson();
        String json = gson.toJson(map);
        GetNews(json,url_id);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 *??????????????????
//                 */
//                if(result!=null)
//                {
//
//
//                }
//            }
//        }, 4000);//2????????????Runnable??????run??????
    }

    //??????????????????
    private void NewsByType(int type)
    {
        mNewsList.clear();
        HashMap<String,String> map = new HashMap<>();
        map.put("index",index+"");
        map.put("type",type+"");
        Gson gson = new Gson();
        String json = gson.toJson(map);
        GetNews(json,url_type);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                /**
//                 *??????????????????
//                 */
//                if(result!=null)
//                {
//
//
//                    Gson gson1 = new Gson();//??????Gson??????
//                    news = gson1.fromJson(result,News.class);
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String info = jsonObject.getString("info");
//
//                        JSONObject jsonObject1 = new JSONObject(info);
//                        String newsList = jsonObject1.getString("newsList");
//
//                        //Log.d("@NewsFragment??????????????????", "????????????: " + newsList);
//                        JsonParser jsonParser = new JsonParser();
//                        JsonArray jsonElements = jsonParser.parse(newsList).getAsJsonArray();//??????JsonArray??????
//                        for (JsonElement bean : jsonElements) {
//                            News news1 = gson1.fromJson(bean, News.class);//??????
//                            mNewsList.add(news1);
//                        }
//
//                    }catch (JSONException e){
//
//                    }
//
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    recyclerView.setLayoutManager(layoutManager);
//                    adapter = new NewsAdapter(getContext(),mNewsList);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                }
//            }
//        }, 3000);//2????????????Runnable??????run??????

    }

    //????????????
    private void translateAnimation() {


        //????????????????????????  ?????????????????????????????????????????????????????????
        translateAniShow = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF??????????????????
                0,//fromXValue???????????????X?????????
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue???????????????X?????????
                Animation.RELATIVE_TO_SELF,
                1,//fromXValue???????????????Y?????????
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue???????????????Y?????????
        translateAniShow.setRepeatMode(Animation.REVERSE);
        translateAniShow.setDuration(700);

        //????????????????????????  ?????????????????????????????????????????????????????????
        translateAniHide = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF,//RELATIVE_TO_SELF??????????????????
                0,//fromXValue???????????????X?????????
                Animation.RELATIVE_TO_SELF,
                1,//fromXValue???????????????X?????????
                Animation.RELATIVE_TO_SELF,
                0,//fromXValue???????????????Y?????????
                Animation.RELATIVE_TO_SELF,
                0);//fromXValue???????????????Y?????????
        translateAniHide.setRepeatMode(Animation.REVERSE);
        translateAniHide.setDuration(700);
    }

    //???????????????
    private void bannerVisible(){
        mBanner.startAnimation(translateAniShow);
        mBanner.setVisibility(View.VISIBLE);
        bannerIsG = true;
    }
    //???????????????
    private void bannerGone(){
        if(bannerIsG)
        {
            //mBanner.startAnimation(translateAniHide);
            mBanner.setVisibility(View.GONE);
            bannerIsG = false;
        }
    }


    /***??????????????????***/
    private void ItemsSamll() {
        mItemBtn0.setTextSize(16);
        mItemBtn1.setTextSize(16);
        mItemBtn2.setTextSize(16);
        mItemBtn3.setTextSize(16);
        mItemBtn4.setTextSize(16);
        mItemBtn5.setTextSize(16);
        mItemBtn6.setTextSize(16);
        mItemBtn7.setTextSize(16);
        mItemBtn8.setTextSize(16);
        mItemBtn9.setTextSize(16);
        mItemBtn10.setTextSize(16);
        mItemBtn11.setTextSize(16);
        mItemBtn12.setTextSize(16);
        mItemBtn13.setTextSize(16);
        mItemBtn14.setTextSize(16);
        mItemBtnHome.setTextSize(16);
    }



    private void GetNews(final String json, String url) {

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
                Log.d("@NewsFragment???????????????", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                result = response.body().string();
                Log.d("@NewsFragment???????????????", "????????????"+json+"\n??????????????????: " + result);
                //?????????
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(!result.equals(""))
                            //????????????????????????UI
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
                        Log.d("@NewsFragment?????????????????????UI???", result);
                        Gson gson1 = new Gson();//??????Gson??????
                        news = gson1.fromJson(result,News.class);
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String info = jsonObject.getString("info");

                            JSONObject jsonObject1 = new JSONObject(info);
                            String newsList = jsonObject1.getString("newsList");

                            //Log.d("@NewsFragment????????????ID???", userId+"\n????????????: " + newsList);
                            JsonParser jsonParser = new JsonParser();
                            JsonArray jsonElements = jsonParser.parse(newsList).getAsJsonArray();//??????JsonArray??????
                            for (JsonElement bean : jsonElements) {
                                News news1 = gson1.fromJson(bean, News.class);//??????
                                mNewsList.add(news1);
                            }

                        }catch (JSONException e){

                        }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new NewsAdapter(getContext(),mNewsList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        //????????????
                        List<String> imgeList = new ArrayList<>();
                        //????????????
                        List<String> titleList = new ArrayList<>();

                        for (int i = 0; i < mNewsList.size(); i++) {
                            imgeList.add(mNewsList.get(i).getIndexImage());//???????????????????????????list??????

                            titleList.add(mNewsList.get(i).getTitle());//????????????????????????????????????
                            //??????????????????????????????Glide????????????
                            mBanner.setImageLoader(new ImageLoader() {
                                @Override
                                public void displayImage(Context context, Object path, ImageView imageView) {
                                    Glide.with(getContext()).load(path).into(imageView);
                                }
                            });
                            mBanner.setImages(imgeList);//??????????????????
                            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);//??????banner????????????????????????????????????
                            mBanner.setBannerTitles(titleList); //????????????????????????banner???????????????title??????
                            //????????????????????????????????????????????????????????????
                            mBanner.setIndicatorGravity(BannerConfig.CENTER);
                            mBanner.setDelayTime(5000);//??????????????????5??????????????????
                            mBanner.setOnBannerListener(new OnBannerListener() {
                                @Override
                                public void OnBannerClick(int position) {
                                    mSpeechUtil.speaking(mNewsList.get(position).getTitle());

                                }
                            });
                            mBanner.start();//????????????banner??????
                        }
                        break;
                    default:break;
                }
            }
        }
    };

}
