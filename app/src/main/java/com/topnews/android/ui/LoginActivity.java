package com.topnews.android.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.topnews.android.R;
import com.topnews.android.gson.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class LoginActivity extends AppCompatActivity {

    private EditText mUserName,mPassword;
    private Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserName= (EditText) findViewById(R.id.login_email_phone);
        mPassword= (EditText) findViewById(R.id.login_password);
        mLogin= (Button) findViewById(R.id.btn_login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(mUserName.getText()) || TextUtils.isEmpty(mPassword.getText())){

                    Toast.makeText(LoginActivity.this,"请输入登录账号或密码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String loginAccount=mUserName.getText().toString().trim();
                String loginPassword=mPassword.getText().toString().trim();

                BmobUser.loginByAccount(loginAccount, loginPassword, new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        if(user!=null){
                            Toast.makeText(LoginActivity.this,"登陆成功，"+user.getUsername(),Toast.LENGTH_SHORT).show();

                           /* Intent intent=new Intent(SignUpActivity.this,MySettingActivity.class);
                            startActivity(intent);*/

                        }else{
                            Toast.makeText(LoginActivity.this,"登陆失败，请重试，"+"错误码："+e.getErrorCode()+",错误原因："+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
