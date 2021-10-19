package com.panszzz.newsight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.panszzz.newsight.news.News;
import com.panszzz.newsight.news.NewsActivity;
import com.panszzz.newsight.utils.SpeechUtil;

import java.io.IOException;
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

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.ViewHolder> {
    public Context mContext;
    private List<News> mNewsList;
    private int[] mViewList = new int[]{R.mipmap.bg_news_1, R.mipmap.bg_news_1, R.mipmap.bg_news_2, R.mipmap.bg_news_3, R.mipmap.bg_news_4, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3};
    private News news;
    private SpeechUtil mSpeechUtil;

    private String collectionId = "";

    private String userUid;
    private String url_del = "http://1.116.213.5:8080/reader/collection/deleteCollection";
    private String result;

    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsWriter, newsSource,newsTime;
        ImageView newsView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            newsView = itemView.findViewById(R.id.news_image);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsWriter = itemView.findViewById(R.id.news_writer);
 //           newsSource = itemView.findViewById(R.id.news_source);
            newsTime = itemView.findViewById(R.id.news_time);
            layout  = itemView.findViewById(R.id.layout);
        }
    }

    public CollectionAdapter(Context context, List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_collection, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        //获取用户id
        SharedPreferences sharedPreferences= mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        userUid = sharedPreferences.getString("userId",null);

        news = mNewsList.get(position);
        holder.newsTitle.setText(news.getTitle());
//        holder.newsSource.setText(news.getMainEditor());
        holder.newsWriter.setText(news.getMainEditor());
        holder.newsTime.setText(news.getCreateTime());


        Glide.with(mContext)
                .load(news.getIndexImage())
                .error(R.mipmap.news_zj)
                .into(holder.newsView);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                News mNews = mNewsList.get(position);
                if(collectionId.equals(mNews.getId()))
                {
                    Intent intent = new Intent(mContext, NewsActivity.class);
                    intent.putExtra("newsUid",mNews.getArticleUid());
                    intent.putExtra("newsId",mNews.getId());
                    intent.putExtra("memo",mNews.getMemo());
                    intent.putExtra("title",mNews.getTitle());
                    intent.putExtra("editor",mNews.getMainEditor());
                    intent.putExtra("time",mNews.getCreateTime());
                    mContext.startActivity(intent);
                }
                else {
                    collectionId = mNews.getId();
                    mSpeechUtil = SpeechUtil.getInstance(v.getContext());
                    mSpeechUtil.speaking(mNews.getTitle() + "再次点击阅读详情,长按移出收藏夹");
                }
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                News mNews = mNewsList.get(position);
                deleteCollection(mNews.getId());
                mNewsList.remove(position);
                notifyItemRemoved(position);
                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    private void deleteCollection(String newsId)
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
                .url(url_del)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("@NewsActivity收藏移除失败：", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                result = response.body().string();
                Log.d("@NewsActivity", "移除新闻："+json_coll+"\n结果: " + result);
            }
        });
    }



}
