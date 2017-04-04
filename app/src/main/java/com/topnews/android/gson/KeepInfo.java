package com.topnews.android.gson;

import java.io.Serializable;

/**
 * Created by dell on 2017/4/4.
 *
 * 用户收藏信息
 */

public class KeepInfo implements Serializable{

    private String title;

    private String contentUri;

    private boolean state;

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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
