package com.panszzz.newsight.news;

public class News {
    // news_3 api sample: http://v.juhe.cn/toutiao/index?type=top&key=APPKEY

    private String id;
    private String title;
    private String content;
    private String memo;
    private String indexImage;
    private String createTime;
    private String articleUid;
    private String newsList;
    private String mainEditor;

    public News(String title, String memo, String indexImage,String mainEditor) {
        this.title = title;
        this.content = content;
        this.memo = memo;
        this.indexImage = indexImage;
        this.mainEditor = mainEditor;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMainEditor() {
        return mainEditor;
    }

    public void setMainEditor(String mainEditor) {
        this.mainEditor = mainEditor;
    }

    public String getNewsList() {
        return newsList;
    }

    public void setNewsList(String newsList) {
        this.newsList = newsList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIndexImage() {
        return indexImage;
    }

    public void setIndexImage(String indexImage) {
        this.indexImage = indexImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getArticleUid() {
        return articleUid;
    }

    public void setArticleUid(String articleUid) {
        this.articleUid = articleUid;
    }
}
