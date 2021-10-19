package com.panszzz.newsight.news;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.panszzz.newsight.R;
import com.panszzz.newsight.utils.SpeechUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    public Context mContext;
    private List<News> mNewsList;
    private int[] mViewList = new int[]{R.mipmap.bg_news_1, R.mipmap.bg_news_1, R.mipmap.bg_news_2, R.mipmap.bg_news_3, R.mipmap.bg_news_4, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3, R.mipmap.news_3};
    private News news;
    private SpeechUtil mSpeechUtil;


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

    public NewsAdapter(Context context,List<News> newsList) {
        mContext = context;
        mNewsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_main, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                mSpeechUtil = SpeechUtil.getInstance(v.getContext());
                mSpeechUtil.speaking(mNews.getTitle() + "常按阅读详情");
            }
        });

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, NewsActivity.class);
                News mNews = mNewsList.get(position);
                intent.putExtra("newsUid",mNews.getArticleUid());
                intent.putExtra("newsId",mNews.getId());
                intent.putExtra("memo",mNews.getMemo());
                intent.putExtra("title",mNews.getTitle());
                intent.putExtra("editor",mNews.getMainEditor());
                intent.putExtra("time",mNews.getCreateTime());
                mContext.startActivity(intent);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }



}
