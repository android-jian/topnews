package com.topnews.android.pager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.topnews.android.R;
import com.topnews.android.gson.MyUser;
import com.topnews.android.ui.LoginActivity;
import com.topnews.android.ui.ReadActivity;
import com.topnews.android.ui.SignActivity;
import com.topnews.android.ui.UserKeepActivity;
import com.topnews.android.utils.UIUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import rx.functions.Action1;

/**
 * Created by dell on 2017/3/23.
 */

public class SettingFragment extends BasePagerFragment implements EasyPermissions.PermissionCallbacks{

    private Button settingLogin;
    private LinearLayout headLogin;
    private RelativeLayout mUserInfo;
    private CircleImageView mIcon;
    private TextView mName;
    private RelativeLayout mChangeSkin;
    private SharedPreferences sp;
    private boolean isNight;
    private ImageView mIsNight;
    private Button mQuikSign;
    private LinearLayout mKeep;
    private LinearLayout mUserLoginOut;
    private LinearLayout mLinearRead;

    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;

    /**
     * 请求CAMERA权限码
     */
    public static final int REQUEST_CAMERA_PERM = 101;
    private RelativeLayout mScan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=View.inflate(UIUtils.getContext(), R.layout.setting_pager,null);

        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

        initView(view);

        initListener();

        isNight = sp.getBoolean("night", false);
        if (isNight) {
            mIsNight.setImageResource(R.drawable.apointe);

        } else {
            mIsNight.setImageResource(R.drawable.apointd);
        }

        //获取本地用户 优化用户体验
        MyUser curUser=BmobUser.getCurrentUser(MyUser.class);
        if (curUser==null){

            headLogin.setVisibility(View.VISIBLE);
            mUserInfo.setVisibility(View.GONE);

        }else {

            headLogin.setVisibility(View.GONE);
            mUserInfo.setVisibility(View.VISIBLE);

            //获取本地用户信息
            mName.setText(curUser.getUsername());
            Glide.with(this).load(curUser.getIcon()).error(R.drawable.adf)
                    .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mIcon);
        }

        return view;
    }

    private void initView(View view){
        settingLogin = (Button) view.findViewById(R.id.btn_setting_login);
        headLogin = (LinearLayout) view.findViewById(R.id.head_login);
        mUserInfo = (RelativeLayout) view.findViewById(R.id.head_user_info);
        mIcon = (CircleImageView) view.findViewById(R.id.setting_head_icon);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mChangeSkin = (RelativeLayout) view.findViewById(R.id.setting_change_skin);
        mIsNight = (ImageView) view.findViewById(R.id.iv_is_night);
        mQuikSign = (Button) view.findViewById(R.id.btn_quik_sign);
        mKeep = (LinearLayout) view.findViewById(R.id.setting_my_keep);
        mUserLoginOut = (LinearLayout) view.findViewById(R.id.setting_login_out);
        mLinearRead = (LinearLayout) view.findViewById(R.id.setting_read);
        mScan = (RelativeLayout) view.findViewById(R.id.setting_scan);
    }

    private void initListener(){

        settingLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UIUtils.getContext(), LoginActivity.class);
                startActivityForResult(intent,1);
            }
        });

        mIcon.setOnClickListener(new View.OnClickListener() {

            private Dialog dialog;

            @Override
            public void onClick(View v) {

                View view=View.inflate(UIUtils.getContext(),R.layout.dialog_photo,null);
                LinearLayout mFirst= (LinearLayout) view.findViewById(R.id.dialog_first);
                LinearLayout mSecond= (LinearLayout) view.findViewById(R.id.dialog_second);
                LinearLayout mThird= (LinearLayout) view.findViewById(R.id.dialog_third);

                mFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        takePhoto();
                        dialog.dismiss();
                    }
                });

                mSecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        chooseFromIcons();
                        dialog.dismiss();
                    }
                });

                AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
                builder.setTitle("设置你的靓照");
                builder.setView(view);
                dialog = builder.show();
            }
        });

        mChangeSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNightMode();
                getActivity().recreate();

            }
        });

        mKeep.setOnClickListener(new View.OnClickListener() {
            private Intent intent;

            @Override
            public void onClick(View v) {

                MyUser mUser=BmobUser.getCurrentUser(MyUser.class);

                if (mUser==null){
                    intent= new Intent(UIUtils.getContext(),LoginActivity.class);
                }else {
                    intent=new Intent(UIUtils.getContext(), UserKeepActivity.class);
                }
                startActivity(intent);
            }
        });

        mUserLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLoginOut();
            }
        });

        mQuikSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UIUtils.getContext(), SignActivity.class);
                startActivity(intent);
            }
        });

        mLinearRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UIUtils.getContext(), ReadActivity.class);
                startActivity(intent);
            }
        });

        mScan.setOnClickListener(new ButtonOnClickListener(mScan.getId()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 1:
                if (resultCode==getActivity().RESULT_OK){

                    MyUser mUser= (MyUser) data.getSerializableExtra("userData");
                    headLogin.setVisibility(View.GONE);
                    mUserInfo.setVisibility(View.VISIBLE);

                    mName.setText(mUser.getUsername());
                    Glide.with(this).load(mUser.getIcon()).error(R.drawable.adf)
                            .diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(mIcon);
                }
                break;

            case TAKE_PHOTO:
                if (resultCode==getActivity().RESULT_OK){

                    Glide.with(this).load(imageUri).diskCacheStrategy(DiskCacheStrategy.NONE).
                            skipMemoryCache(true).into(mIcon);

                    /*String iconPath="//sdcard//Android//data//com.topnews.android//cache//output_image.jpg";
                    upLoadUserIcon(iconPath);*/
                }
                break;

            case CHOOSE_PHOTO:
                if (resultCode==getActivity().RESULT_OK){
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT>=19){

                        //4.4及以上系统使用这个方法来处理图片
                        handleImageOnKitKat(data);
                    }else {

                        //4.4以下系统使用这个方法来处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;

            /**
             * 处理二维码扫描结果
             */
            case REQUEST_CODE:

                //处理扫描结果（在界面上显示）
                if (null != data) {
                    Bundle bundle = data.getExtras();
                    if (bundle == null) {
                        return;
                    }
                    if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        //Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();

                        if (Patterns.WEB_URL.matcher(result).matches()) {
                            //符合标准
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(result);
                            intent.setData(content_url);
                            startActivity(intent);

                        } else{
                            //不符合标准
                            Toast.makeText(UIUtils.getContext(), "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                    } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(UIUtils.getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
            }
            break;

            case REQUEST_CAMERA_PERM:
                Toast.makeText(UIUtils.getContext(), "从设置页面返回...", Toast.LENGTH_SHORT)
                        .show();
            break;

            default:
                break;
        }
    }

    private static final int TAKE_PHOTO=2;
    private Uri imageUri;

    private void takePhoto(){

        File outPutImage = new File(getActivity().getExternalCacheDir(),"output_image.jpg");

        try {
            if (outPutImage.exists()){
                outPutImage.delete();
            }
            outPutImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT>=24){
            imageUri= FileProvider.getUriForFile(UIUtils.getContext(),
                    "com.jian.android.screencut.takephoto", outPutImage);
        }else {
            imageUri=Uri.fromFile(outPutImage);
        }

        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }

    public static final int CHOOSE_PHOTO=3;

    private void chooseFromIcons(){

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }else {
            openAlbum();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1:
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }else {
                    Toast.makeText(UIUtils.getContext(),"你关闭了权限功能",Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 打开相册
     */
    private void openAlbum(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){

        String imagePath=null;
        Uri uri=data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(),uri)){
            //如果是 document 类型的 Uri，则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];    //解析出数字格式的id
                String selection=MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri 则使用普通方式处理
            imagePath=getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath=uri.getPath();
        }
        displayImage(imagePath);      //根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data){

        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri,String selection){
        String path=null;
        //通过Uri 和selection来获取真实的图片路径
        Cursor cursor=getActivity().getContentResolver().query(uri,null,selection,null,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath!=null){

            Glide.with(this).load(imagePath).diskCacheStrategy(DiskCacheStrategy.NONE).
                    skipMemoryCache(true).into(mIcon);

            upLoadUserIcon(imagePath);

        }else {
            Toast.makeText(UIUtils.getContext(),"获取图片失败",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 上传用户头像信息
     */
    private void upLoadUserIcon(String imagePath){

        File iconFile= new File(imagePath);

        if (iconFile.exists()) {

            final BmobFile bmobFile = new BmobFile(iconFile);
            bmobFile.uploadblock(new UploadFileListener() {

                @Override
                public void done(BmobException e) {
                    if (e == null) {

                        MyUser bmobUser=BmobUser.getCurrentUser(MyUser.class);

                        if (bmobUser!=null){
                            MyUser newUser=new MyUser();
                            newUser.setIcon(bmobFile.getFileUrl());

                            newUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {

                                    if (e==null){
                                        Toast.makeText(UIUtils.getContext(), "头像信息上传成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(UIUtils.getContext(), "头像信息上传失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 夜间模式切换
     */
    private void isNightMode(){

        isNight = sp.getBoolean("night", false);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            sp.edit().putBoolean("night", false).commit();

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            sp.edit().putBoolean("night", true).commit();
        }

    }

    @AfterPermissionGranted(REQUEST_CAMERA_PERM)
    public void cameraTask(int viewId) {
        if (EasyPermissions.hasPermissions(UIUtils.getContext(), Manifest.permission.CAMERA)) {
            // Have permission, do the thing!
            onClick(viewId);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "需要请求camera权限",
                    REQUEST_CAMERA_PERM, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Toast.makeText(UIUtils.getContext(), "执行onPermissionsGranted()...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(UIUtils.getContext(), "执行onPermissionsDenied()...", Toast.LENGTH_SHORT).show();
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, "当前App需要申请camera权限,需要打开设置页面么?")
                    .setTitle("权限申请")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消", null /* click listener */)
                    .setRequestCode(REQUEST_CAMERA_PERM)
                    .build()
                    .show();
        }
    }

    /**
     * 按钮点击监听
     */
    class ButtonOnClickListener implements View.OnClickListener{

        private int buttonId;

        public ButtonOnClickListener(int buttonId) {
            this.buttonId = buttonId;
        }

        @Override
        public void onClick(View v) {

            cameraTask(buttonId);
        }
    }

    /**
     * 按钮点击事件处理逻辑
     * @param buttonId
     */
    private void onClick(int buttonId) {
        switch (buttonId) {
            case R.id.setting_scan:
                Intent intent = new Intent(UIUtils.getContext(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;

            default:
                break;
        }
    }

    /**
     * 用户登出
     */
    private void userLoginOut(){

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("您确定要登出吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BmobUser.logOut();
                getActivity().recreate();
            }
        });
        builder.setNegativeButton("取消",null);
        builder.show();
    }

}
