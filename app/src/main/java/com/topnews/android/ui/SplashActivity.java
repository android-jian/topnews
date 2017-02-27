package com.topnews.android.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.topnews.android.R;
import com.topnews.android.ui.HomeActivity;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(2000);

                enterHome();
            }
        }.start();
    }

    /**
     * 进入主界面   销毁当前activity
     */
    private void enterHome(){
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);

        finish();
    }
}
