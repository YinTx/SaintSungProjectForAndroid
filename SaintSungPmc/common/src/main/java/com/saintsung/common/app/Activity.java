package com.saintsung.common.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import java.util.List;
import butterknife.ButterKnife;


/**
 * @author XioYLz
 */

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();
        if (initArgs(getIntent().getExtras())) {
            setContentView(getContentLayoutId());
            initWidget();
            initData();
        }else {
            finish();
        }
    }

    /**
     * 在界面未初始化之前调用的初始化窗口
     */
    protected void initWindows(){}

    /**
     * 初始化相关参数
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true 如果错误返回False
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前界面的资源文件Id
     *
     * @return 资源文件Id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {
        ButterKnife.bind(this);
    }

    /**
     * 初始化数据
     */
    protected void initData() {}

    @Override
    public boolean onSupportNavigateUp() {
        //当点击界面导航返回时，Finish当前界面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前Activity下的所有Fragment
        @SuppressLint("RestrictedApi")
        List<Fragment> fragments= (List<Fragment>) getSupportFragmentManager().getFragments();
        //判断是否为空
        if(fragments!=null&&fragments.size()>0){
            for(Fragment fragment:fragments){
                //判断是否为我们能够处理的Fragment类型
                if(fragment instanceof com.saintsung.common.app.Fragment){
                    //判断是否拦截了返回按钮
                    if(((com.saintsung.common.app.Fragment) fragment).onBackPressed()){
                        //如果有直接Return
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
