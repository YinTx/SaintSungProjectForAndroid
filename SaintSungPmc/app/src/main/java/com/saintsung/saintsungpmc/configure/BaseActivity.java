package com.saintsung.saintsungpmc.configure;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.saintsung.common.app.CheckPermissionsActivity;

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
//        ToastUtil.setColor(this, getResources().getColor(R.color.colorAccent));
        //防止Fragment切换高德地图闪屏问题
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }
}
