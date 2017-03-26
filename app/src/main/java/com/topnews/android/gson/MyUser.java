package com.topnews.android.gson;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by dell on 2017/3/22.
 */

public class MyUser extends BmobUser implements Serializable{

    private String icon;        //我的头像

    private String keep;       //我的收藏

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKeep() {
        return keep;
    }

    public void setKeep(String keep) {
        this.keep = keep;
    }
}
