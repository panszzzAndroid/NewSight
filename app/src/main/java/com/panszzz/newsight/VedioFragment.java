package com.panszzz.newsight;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.panszzz.newsight.news.News;
import com.panszzz.newsight.news.NewsAdapter;
import com.panszzz.newsight.utils.PermissionUtils;
import com.panszzz.newsight.utils.SpeechUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class VedioFragment extends Fragment {

    private List<News> mNewsList;
    private NewsAdapter adapter;
    private RecyclerView recyclerView;
    private Banner mBanner;
    private String json;
    private String result;
    private News news;
    private SpeechUtil mSpeechUtil;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vedio, container, false);




        view.findViewById(R.id.text_vedio_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_1)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });
        view.findViewById(R.id.image_vedio_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_1)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });




        view.findViewById(R.id.image_vedio_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_2)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });
        view.findViewById(R.id.text_vedio_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_2)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });





        view.findViewById(R.id.image_vedio_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_3)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });
        view.findViewById(R.id.text_vedio_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_3)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });





        view.findViewById(R.id.image_vedio_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_4)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });
        view.findViewById(R.id.text_vedio_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_4)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });




        view.findViewById(R.id.image_vedio_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_5)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });
        view.findViewById(R.id.text_vedio_5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = getString(R.string.video_5)+"常按观看视频";
                mSpeechUtil.speaking(str);
            }
        });



        /***
         * 转跳到新闻视频界面
         * **/
        view.findViewById(R.id.image_vedio_1).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",1);
                startActivity(intent);
                return false;
            }
        });
        view.findViewById(R.id.text_vedio_1).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",1);
                startActivity(intent);
                return false;
            }
        });





        view.findViewById(R.id.image_vedio_2).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",2);
                startActivity(intent);
                return false;
            }
        });
        view.findViewById(R.id.text_vedio_2).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",2);
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.image_vedio_3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",3);
                startActivity(intent);
                return false;
            }
        });
        view.findViewById(R.id.text_vedio_3).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",3);
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.image_vedio_4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",4);
                startActivity(intent);
                return false;
            }
        });
        view.findViewById(R.id.text_vedio_4).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",4);
                startActivity(intent);
                return false;
            }
        });




        view.findViewById(R.id.image_vedio_5).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",5);
                startActivity(intent);
                return false;
            }
        });
        view.findViewById(R.id.text_vedio_5).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(getContext(),VedioActivity.class);
                intent.putExtra("position",5);
                startActivity(intent);
                return false;
            }
        });



        mBanner = view.findViewById(R.id.banner);
        //图片资源
        int[] imageResourceID = new int[]{ R.drawable.ic_vedio,
                R.drawable.ic_vedio};

        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        // String[] mtitle = new String[]{"图片1", "图片2", "图片3", "图片4"};
        List<String> titleList = new ArrayList<>();

        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            //titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
            mBanner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, Object path, ImageView imageView) {
                    Glide.with(getContext()).load(path).into(imageView);
                }
            });
            mBanner.setImages(imgeList);//设置图片资源
            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);//设置banner显示样式（带标题的样式）
            mBanner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
            //设置指示器位置（即图片下面的那个小圆点）
            mBanner.setIndicatorGravity(BannerConfig.CENTER);
            mBanner.setDelayTime(2500);//设置轮播时间3秒切换下一图
            //mBanner.setOnBannerListener(getContext());//设置监听
            mBanner.start();//开始进行banner渲染
        }
        return view;

    }
}
