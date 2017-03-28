package com.topnews.android.pager;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
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
import com.topnews.android.utils.UIUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.functions.Action1;

/**
 * Created by dell on 2017/3/23.
 */

public class SettingFragment extends BasePagerFragment{

    private Button settingLogin;
    private LinearLayout headLogin;
    private RelativeLayout mUserInfo;
    private CircleImageView mIcon;
    private TextView mName;
    private RelativeLayout mChangeSkin;
    private SharedPreferences sp;
    private boolean isNight;
    private ImageView mIsNight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=View.inflate(UIUtils.getContext(), R.layout.setting_pager,null);
        settingLogin = (Button) view.findViewById(R.id.btn_setting_login);
        headLogin = (LinearLayout) view.findViewById(R.id.head_login);
        mUserInfo = (RelativeLayout) view.findViewById(R.id.head_user_info);
        mIcon = (CircleImageView) view.findViewById(R.id.setting_head_icon);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mChangeSkin = (RelativeLayout) view.findViewById(R.id.setting_change_skin);
        mIsNight = (ImageView) view.findViewById(R.id.iv_is_night);

        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);

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

        isNight = sp.getBoolean("night", false);
        if (isNight) {
            mIsNight.setImageResource(R.drawable.apointe);

        } else {
            mIsNight.setImageResource(R.drawable.apointd);
        }

        mChangeSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNightMode();
                getActivity().recreate();

            }
        });

        return view;
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

}
