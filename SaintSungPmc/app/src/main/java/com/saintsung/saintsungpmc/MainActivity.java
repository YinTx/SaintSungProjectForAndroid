package com.saintsung.saintsungpmc;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.saintsung.saintsungpmc.configure.BaseActivity;
import com.saintsung.saintsungpmc.fragment.MainControlFragment;
import com.saintsung.saintsungpmc.fragment.MainHomeFragment;
import com.saintsung.saintsungpmc.fragment.MainMapFragment;
import com.saintsung.saintsungpmc.fragment.MainPersonalFragment;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private long exitTime = 0;//2次回退计时器

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化控件
    private void initView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navi_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        //打开程序初始化界面
        getHomeFragment();
    }

    private void getHomeFragment() {
        MainHomeFragment mainHomeFragment = new MainHomeFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, mainHomeFragment).commit();
        getFragmentManager().beginTransaction().show(mainHomeFragment);
    }

    private void getControlFragment() {
        MainControlFragment mainHomeFragment = new MainControlFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, mainHomeFragment).commit();
        getFragmentManager().beginTransaction().show(mainHomeFragment);
    }

    private void getMapFragment() {
        MainMapFragment mainHomeFragment = new MainMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, mainHomeFragment).commit();
        getFragmentManager().beginTransaction().show(mainHomeFragment);


    }

    private void getPersonalFragment() {
        MainPersonalFragment mainHomeFragment = new MainPersonalFragment();
        getFragmentManager().beginTransaction().replace(R.id.main_fragment, mainHomeFragment).commit();
        getFragmentManager().beginTransaction().show(mainHomeFragment);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //主页Home页面的Fragment
            case R.id.menu_main_home:
                getHomeFragment();
                break;
            //设备空的页面的Fragment
            case R.id.menu_main_control:
                getControlFragment();
                break;
            //地图页面的Fragment
            case R.id.menu_main_map:
                getMapFragment();
                break;
            //个人中心页面的Fragment
            case R.id.menu_main_personal:
                getPersonalFragment();
                break;
            default:
                break;
        }
        return true;
    }
}
