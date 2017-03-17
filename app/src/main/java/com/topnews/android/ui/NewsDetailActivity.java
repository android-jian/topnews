package com.topnews.android.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.topnews.android.R;
import com.topnews.android.gson.TopInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar mProgress;
    private WebView mWebView;
    private TopInfo topInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ShareSDK.initSDK(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar_news_detail);
        setSupportActionBar(toolbar);

        mProgress = (ProgressBar) findViewById(R.id.pb_text_progress);
        mWebView = (WebView) findViewById(R.id.wv_show_text);

        topInfo = (TopInfo) getIntent().getSerializableExtra("top_info");
        mWebView.loadUrl(topInfo.ContentUrl);

        WebSettings mWebSet=mWebView.getSettings();
        mWebSet.setBuiltInZoomControls(true);
        mWebSet.setUseWideViewPort(true);
        mWebSet.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                mProgress.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                mProgress.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                mProgress.setProgress(newProgress);
                super.onProgressChanged(view, newProgress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.backup:
                finish();
                break;

            case R.id.share:

                share();

                break;

            case R.id.keep:
                Toast.makeText(this,"keep",Toast.LENGTH_SHORT).show();
                break;

            case R.id.photo:

                takeScreenShot(this);

                Intent intent=new Intent(this,ScreenCutActivity.class);
                startActivity(intent);

                break;

            case R.id.text_size:

                textSizeDialog();

                break;

            default:
                break;
        }
        return true;
    }

    private int whichSelect;
    private int curSelect=2;

    /**
     * 字体大小设置
     */
    private void textSizeDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字体大小设置");
        String[] items={"超大号字体","大号字体","正常字体","小号字体","超小号字体"};

        builder.setSingleChoiceItems(items,curSelect,new AlertDialog.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                whichSelect=which;
            }
        });

        builder.setPositiveButton("确定",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {

                WebSettings webSet=mWebView.getSettings();

                switch (whichSelect){

                    case 0:
                        webSet.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        webSet.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        webSet.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        webSet.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        webSet.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                    default:
                        break;
                }

                curSelect=whichSelect;
            }
        });

        builder.setNegativeButton("取消",null);
        builder.show();
    }

    /**
     * 截屏并保存图片
     * @param activity
     */
    public  void takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        //获取actiobBar的高度
        float actionBarHeight=activity.obtainStyledAttributes(new int[]{android.R.attr.actionBarSize})
                .getDimension(0,0);

        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight+(int)actionBarHeight, width, height
                - statusBarHeight-(int)actionBarHeight);
        view.destroyDrawingCache();

        //将bitmap存入本地文件

        //获取系统缓存文件
        File cacheFile=new File(getCacheDir(),"ScreenCut");

        FileOutputStream out=null;
        try {
            out=new FileOutputStream(cacheFile);
            b.compress(Bitmap.CompressFormat.PNG,90,out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分享
     */
    private void share(){

        OnekeyShare share = new OnekeyShare();
        share.disableSSOWhenAuthorize();

        share.setText("源自"+topInfo.source);
        // text是分享文本，所有平台都需要这个字段
        share.setTitle(topInfo.title);
        // url仅在微信（包括好友和朋友圈）中使用
        share.setUrl(topInfo.ContentUrl);
        share.setTitleUrl(topInfo.ContentUrl);
        share.setImageUrl(topInfo.imgeUrl);

        // 启动分享GUI
        share.show(this);
    }
}
