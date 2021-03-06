package com.topnews.android.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.topnews.android.R;
import com.topnews.android.gson.KeepInfo;

public class KeepDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ProgressBar mProgress;
    private WebView mWebView;
    private KeepInfo keepInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar = (Toolbar) findViewById(R.id.toolbar_news_detail);
        setSupportActionBar(toolbar);

        mProgress = (ProgressBar) findViewById(R.id.pb_text_progress);
        mWebView = (WebView) findViewById(R.id.wv_show_text);

        keepInfo = (KeepInfo) getIntent().getSerializableExtra("keep_info");
        mWebView.loadUrl(keepInfo.getContentUri());

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

}
