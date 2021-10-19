package com.panszzz.newsight.news;

import com.loopj.android.http.AsyncHttpClient;

public class NewsApi {
    public static final String base_url = "http://v.juhe.cn/toutiao/index?type=";
    public static final String api_key = "&key=6a3384d1d8115d254cc0bc14e158a3b5";

    public static final String cate_top = "top";
    public static final String cate_national = "guonei";
    public static final String cate_international = "guoji";
    public static final String cate_sports = "tiyu";
    public static final String cate_military = "junshi";
    public static final String cate_technique = "keji";

    private static AsyncHttpClient sClient = null;

    public static synchronized AsyncHttpClient getClient() {
        if (sClient == null) {
            try {
                sClient = new AsyncHttpClient();
                sClient.setTimeout(20000);
            } catch (Exception e) {

            }
        }
        return sClient;
    }
}
