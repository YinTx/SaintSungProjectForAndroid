package com.saintsung.saintsungpmc.configure;

import android.support.v7.app.AppCompatActivity;

import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.map.CheckPermissionsActivity;
import com.saintsung.saintsungpmc.tools.ToastUtil;

/**
 * Created by XLzY on 2017/7/28.
 */
//CheckPermissionsActivity   AppCompatActivity
public class BaseActivity extends CheckPermissionsActivity {
    @Override
    protected void onStart() {
        super.onStart();
        // 沉浸式状态栏
        ToastUtil.setColor(this, getResources().getColor(R.color.colorAccent));
    }
}
