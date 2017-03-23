package com.topnews.android.pager;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.topnews.android.R;
import com.topnews.android.ui.LoginActivity;
import com.topnews.android.utils.UIUtils;

/**
 * Created by dell on 2017/2/26.
 *
 * 标签页-设置
 */

public class SettingPager extends BasePager {

    private Button settingLogin;

    @Override
    public View initView() {

        View view=View.inflate(UIUtils.getContext(), R.layout.setting_pager,null);
        settingLogin = (Button) view.findViewById(R.id.btn_setting_login);

        settingLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(UIUtils.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UIUtils.getContext().startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void initData() {

    }
}
