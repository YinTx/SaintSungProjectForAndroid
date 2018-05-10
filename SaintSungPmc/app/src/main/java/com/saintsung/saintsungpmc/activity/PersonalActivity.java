package com.saintsung.saintsungpmc.activity;


import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.saintsung.common.app.Activity;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.loading.SharedPreferencesUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonalActivity extends Activity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.personal_title);
        imgBack.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.personal_accountsecurity)
    void accountSecurity(){
        startActivity(new Intent(PersonalActivity.this,AccountSecurityActivity.class));
    }
    @OnClick(R.id.personal_out)
    void personalOut(){
        SharedPreferencesUtil.putSharedPreferences(PersonalActivity.this, "Login", "");
        finish();
        startActivity(new Intent(PersonalActivity.this,LoginActivity.class));
    }
}
