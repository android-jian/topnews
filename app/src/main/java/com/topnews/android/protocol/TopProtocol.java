package com.topnews.android.protocol;

import com.topnews.android.gson.TopInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 2017/3/7.
 *
 * 首页网络数据解析
 */

public class TopProtocol extends BaseProtocol<ArrayList<TopInfo>>{

    @Override
    public ArrayList<TopInfo> processData(String data) {

        ArrayList<TopInfo> mInfos=new ArrayList<TopInfo>();

        try {
            JSONObject object=new JSONObject(data);
            JSONObject result=object.getJSONObject("result");
            JSONArray list=result.getJSONArray("list");

            for (int i=0;i<list.length();i++){

                TopInfo topInfo=new TopInfo();
                JSONObject newInfo=list.getJSONObject(i);

                topInfo.id=newInfo.getString("id");
                topInfo.title=newInfo.getString("title");
                topInfo.source=newInfo.getString("source");
                topInfo.imgeUrl=newInfo.getString("firstImg");
                topInfo.ContentUrl=newInfo.getString("url");
                topInfo.curPage=result.getInt("pno");

                mInfos.add(topInfo);
            }

            return mInfos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
