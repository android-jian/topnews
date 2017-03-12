package com.topnews.android.ui;

import android.graphics.Bitmap;
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

public class NewsDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar mProgress;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_news_detail);
        setSupportActionBar(toolbar);

        mProgress = (ProgressBar) findViewById(R.id.pb_text_progress);
        mWebView = (WebView) findViewById(R.id.wv_show_text);

        String mUrl=getIntent().getStringExtra("url");
        mWebView.loadUrl(mUrl);

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
                Toast.makeText(this,"back up",Toast.LENGTH_SHORT).show();
                break;

            case R.id.share:
                Toast.makeText(this,"share",Toast.LENGTH_SHORT).show();
                break;

            case R.id.keep:
                Toast.makeText(this,"keep",Toast.LENGTH_SHORT).show();
                break;

            case R.id.photo:
                Toast.makeText(this,"photo",Toast.LENGTH_SHORT).show();
                break;

            case R.id.text_size:
                Toast.makeText(this,"textsize",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }
}
