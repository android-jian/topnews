package com.topnews.android.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topnews.android.R;
import com.topnews.android.gson.MyUser;
import com.topnews.android.view.SecurityCodeView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;

public class SignActivity extends AppCompatActivity implements SecurityCodeView.InputCompleteListener  {

    private RelativeLayout mSignPhone;
    private RelativeLayout mSignEmail;
    private FrameLayout mUserSign;

    private static final int SIGN_SELECT=0;           //欢迎注册邮箱账号
    private static final int SIGN_BY_PHONE=1;         //手机号注册
    private static final int SIGN_BY_EMAIL=2;         //邮箱注册
    private int curState=SIGN_SELECT;                 //记录当前状态，决定动态添加哪个布局
    private View mSign;
    private View phoneSign;
    private EditText editPhoneNumber;
    private Button btnGetCode;

    private SecurityCodeView editText;
    private TextView text;
    private String phoneNumber;
    private View emailSign;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editConfirm;
    private Button mEmail;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sign);

        mUserSign = (FrameLayout) findViewById(R.id.user_sign);

        mSign = View.inflate(this, R.layout.activity_sign,null);
        phoneSign = View.inflate(this, R.layout.sign_by_phone,null);
        emailSign = View.inflate(this, R.layout.sign_by_email,null);

        BmobSMS.initialize(this,"c3afc6b5c969611f04beb79125e3ec2f");
        Bmob.initialize(this, "c3afc6b5c969611f04beb79125e3ec2f");

        initView();

    }

    /**
     * 动态添加布局view
     */
    private void initView(){

        mUserSign.removeAllViews();

        if (curState==SIGN_SELECT){

            mUserSign.addView(mSign);

        }else if (curState==SIGN_BY_PHONE){

            mUserSign.addView(phoneSign);

        }else if (curState==SIGN_BY_EMAIL){

            mUserSign.addView(emailSign);
        }

        initOnClickListener();

    }

    /**
     * 注册事件
     */
    private void initOnClickListener(){

        if (curState==SIGN_SELECT){
            mSignPhone = (RelativeLayout) mSign.findViewById(R.id.sign_by_phone);
            mSignEmail = (RelativeLayout) mSign.findViewById(R.id.sign_by_email);

            mSignPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    curState=SIGN_BY_PHONE;
                    initView();
                }
            });

            mSignEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    curState=SIGN_BY_EMAIL;
                    initView();
                }
            });
        }else if (curState==SIGN_BY_PHONE){

            editPhoneNumber = (EditText) phoneSign.findViewById(R.id.et_phone_number);
            btnGetCode = (Button) phoneSign.findViewById(R.id.btn_get_code);

            btnGetCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(editPhoneNumber.getText())){
                        Toast.makeText(SignActivity.this,"请输入您的手机号码亲",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    phoneNumber = editPhoneNumber.getText().toString().trim();

                    //匹配手机号
                    String patternPhone = "(13\\d|14[57]|15[^4,\\D]|17[678]|18\\d)\\d{8}|170[059]\\d{7}";
                    Pattern ptn = Pattern.compile(patternPhone);
                    Matcher mt = ptn.matcher(phoneNumber);
                    if (!mt.matches()){
                        Toast.makeText(SignActivity.this,"亲 请输入正确的手机号",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    BmobSMS.requestSMSCode(SignActivity.this, phoneNumber, "仅测试", new RequestSMSCodeListener() {

                        @Override
                        public void done(Integer smsId, BmobException ex) {
                            if(ex == null){

                                Toast.makeText(SignActivity.this,"验证码发送成功",Toast.LENGTH_SHORT).show();
                                showCodeDialog();

                            }else {
                                Toast.makeText(SignActivity.this,"验证码发送失败，请重试",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            });
        }else if (curState==SIGN_BY_EMAIL){

            editEmail = (EditText) emailSign.findViewById(R.id.edit_email);
            editPassword = (EditText) emailSign.findViewById(R.id.edit_password);
            editConfirm = (EditText) emailSign.findViewById(R.id.edit_confirm);
            mEmail = (Button) emailSign.findViewById(R.id.btn_email);

            mEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(editEmail.getText()) || TextUtils.isEmpty(editPassword.getText())
                            || TextUtils.isEmpty(editConfirm.getText())){

                        Toast.makeText(SignActivity.this,"亲 请不要留空",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userEmail=editEmail.getText().toString().trim();
                    String userPassword=editPassword.getText().toString().trim();
                    String passConfirm=editConfirm.getText().toString().trim();

                    if (!userPassword.equals(passConfirm)){

                        Toast.makeText(SignActivity.this,"亲 密码不一致 请重新输入",Toast.LENGTH_SHORT).show();
                        editPassword.setText("");
                        editConfirm.setText("");

                        return;
                    }

                    //匹配邮箱
                    String pattern = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m = r.matcher(editEmail.getText());
                    if (!m.matches()){
                        Toast.makeText(SignActivity.this,"亲 请输入正确的邮箱账号",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //用户注册
                    MyUser mUser=new MyUser();
                    mUser.setUsername(userEmail);
                    mUser.setPassword(userPassword);
                    mUser.setEmail(userEmail);

                    mUser.signUp(new SaveListener<MyUser>() {

                        @Override
                        public void done(MyUser myUser, cn.bmob.v3.exception.BmobException e) {

                            if(e==null){

                                Toast.makeText(SignActivity.this,"注册成功 验证消息以发送至您的邮箱，请激活",Toast.LENGTH_SHORT).show();
                                finish();

                            }else{
                                Toast.makeText(SignActivity.this,"该用户名已被注册，请重试",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }
            });
        }
    }

    /**
     * 验证码输入框
     */
    private void showCodeDialog(){

        AlertDialog.Builder builder=new AlertDialog.Builder(SignActivity.this);
        builder.setTitle("请输入验证码");

        View view=View.inflate(SignActivity.this,R.layout.sms_code_confirm,null);

        editText = (SecurityCodeView) view.findViewById(R.id.scv_edittext);
        text = (TextView) view.findViewById(R.id.tv_text);

        setListener();

        builder.setView(view);
        dialog = builder.show();
    }

    private void setListener() {
        editText.setInputCompleteListener(this);
    }

    @Override
    public void inputComplete() {

        BmobSMS.verifySmsCode(SignActivity.this, phoneNumber, editText.getEditContent(), new VerifySMSCodeListener() {

            @Override
            public void done(BmobException ex) {

                if(ex == null){

                    //用户注册
                    MyUser mUser=new MyUser();
                    mUser.setUsername(phoneNumber);
                    mUser.setPassword(editText.getEditContent());

                    mUser.signUp(new SaveListener<MyUser>() {

                        @Override
                        public void done(MyUser myUser, cn.bmob.v3.exception.BmobException e) {

                            if(e==null){
                                Toast.makeText(SignActivity.this,"注册成功 该验证码将作为您的初始密码",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                finish();
                            }else{
                                Toast.makeText(SignActivity.this,"该手机号已被注册，请重试",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }else {
                    text.setText("验证码输入错误");
                    text.setTextColor(Color.RED);
                }
            }
        });

    }

    @Override
    public void deleteContent(boolean isDelete) {
        if (isDelete){
            text.setText("输入验证码表示同意《用户协议》");
            text.setTextColor(Color.BLACK);
        }
    }

}