package com.topnews.android.gson;

import org.litepal.crud.DataSupport;

/**
 * 用户阅读信息
 * Created by dell on 2017/4/13.
 */

public class ReadInfo extends DataSupport {

    private String date;

    private String title;

    private String contentUri;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }
}
