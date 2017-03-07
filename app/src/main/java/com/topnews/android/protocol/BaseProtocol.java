package com.topnews.android.protocol;

import com.topnews.android.utils.IOUtils;
import com.topnews.android.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dell on 2017/3/7.
 *
 * 访问网络的基类
 */

public abstract class BaseProtocol <T>{

    /**
     * 获取网络数据
     */
    public T getData(int index){
        //1.首先要获取文件缓存数据
        String data=getCache(index);
        if(data==null){     //1.没有文件缓存数据   2.文件缓存数据过期
            data= getDataFromServer(index);
        }

        if(data!=null){
            return processData(data);
        }
        return null;
    }

    /**
     * 从服务器获取数据
     */
    public String getDataFromServer(int index){

        //http://v.juhe.cn/weixin/query?pno=3&key=24356ec566122b022445333bc04aa489
        String url="http://v.juhe.cn/weixin/query?pno="+index+"&key=24356ec566122b022445333bc04aa489";

        try {
            OkHttpClient client=new OkHttpClient();
            Request request=new Request.Builder().url(url).build();
            Response response=client.newCall(request).execute();

            if (response!=null){
                String mData=response.body().string();
                if (!mData.isEmpty()){
                    //写入缓存
                    setCache(index,mData);
                    return mData;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 向本地文件缓存写入数据
     *
     * 以url为文件名，以json为文件内容，保存在本地
     */
    public void setCache(int index,String result){

        //获取系统缓存目录
        File cacheDir=UIUtils.getContext().getCacheDir();
        File cacheFile=new File(cacheDir,"index=" + index);

        FileWriter writer=null;
        try {
            writer=new FileWriter(cacheFile);

            long deadTime=System.currentTimeMillis()+30*60*1000;    //设置数据缓存时间
            writer.write(deadTime+"\n");    //将数据缓存时间写在文件第一行
            writer.write(result);

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            IOUtils.close(writer);
        }
    }

    /**
     * 获取本地文件缓存数据
     */
    public String getCache(int index){

        //获取系统缓存目录
        File cacheDir=UIUtils.getContext().getCacheDir();
        File cacheFile=new File(cacheDir,"index=" + index);

        if(cacheFile.exists()){
            BufferedReader reader=null;
            try {
                reader=new BufferedReader(new FileReader(cacheFile));
                String deadTime=reader.readLine();
                if(System.currentTimeMillis()<Long.parseLong(deadTime)){     //当前时间小于缓存截止时间,说明缓存还在有效期范围内

                    StringBuffer mBuffer=new StringBuffer();
                    String line=null;
                    while ((line=reader.readLine())!=null) {
                        mBuffer.append(line);
                    }

                    return mBuffer.toString();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                IOUtils.close(reader);
            }
        }
        return null;
    }

    /**
     * 解析数据   必须由子类做具体实现
     * @return
     */
    public abstract T processData(String data);
}
