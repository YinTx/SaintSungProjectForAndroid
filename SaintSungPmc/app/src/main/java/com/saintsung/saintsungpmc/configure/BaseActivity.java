package com.saintsung.saintsungpmc.configure;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.map.CheckPermissionsActivity;
import com.saintsung.saintsungpmc.tools.DataProcess;
import com.saintsung.saintsungpmc.tools.ToastUtil;

/**
 * Created by XLzY on 2017/7/28.
 */
//CheckPermissionsActivity   AppCompatActivity
public class BaseActivity extends CheckPermissionsActivity {
    public BaseApplication baseApplication;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseApplication= (BaseApplication) getApplication();

    }

    @Override
    protected void onStart() {
        Log.e("BaseActivityTAG","onStart");

        super.onStart();
        // 沉浸式状态栏
        ToastUtil.setColor(this, getResources().getColor(R.color.colorAccent));
        //防止Fragment切换高德地图闪屏问题
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
}
