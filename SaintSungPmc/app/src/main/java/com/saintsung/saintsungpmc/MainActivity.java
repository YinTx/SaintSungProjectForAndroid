package com.saintsung.saintsungpmc;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    private MainHomeFragment mainHomeFragment = new MainHomeFragment();
    private MainControlFragment mainControlFragment = new MainControlFragment();
    private MainMapFragment mainMapFragment = new MainMapFragment();
    private MainPersonalFragment mainPersonalFragment = new MainPersonalFragment();

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
        setSelect(0);
    }

    /*
     * 切换显示内容的fragment
     * */
    private void setSelect(int i) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//创建一个事务
        hideFragment(transaction);//我们先把所有的Fragment隐藏了，然后下面再开始处理具体要显示的Fragment
        switch (i) {
            case 0:
                if (mainHomeFragment == null) {
                    mainHomeFragment = new MainHomeFragment();
                /*
                 * 将Fragment添加到活动中，public abstract FragmentTransaction add (int containerViewId, Fragment fragment)
                *containerViewId即为Optional identifier of the container this fragment is to be placed in. If 0, it will not be placed in a container.
                 * */
                    transaction.add(R.id.main_fragment, mainHomeFragment);//将微信聊天界面的Fragment添加到Activity中
                } else {
                    transaction.show(mainHomeFragment);
                }
                break;
            case 1:
                if (mainControlFragment == null) {
                    mainControlFragment = new MainControlFragment();
                    transaction.add(R.id.main_fragment, mainControlFragment);
                } else {
                    transaction.show(mainControlFragment);
                }

                break;
            case 2:
                if (mainMapFragment == null) {
                    mainMapFragment = new MainMapFragment();
                    transaction.add(R.id.main_fragment, mainMapFragment);
                } else {
                    transaction.show(mainMapFragment);
                }

                break;
            case 3:
                if (mainPersonalFragment == null) {
                    mainPersonalFragment = new MainPersonalFragment();
                    transaction.add(R.id.main_fragment, mainPersonalFragment);
                } else {
                    transaction.show(mainPersonalFragment);
                }
                break;

            default:
                break;
        }
        transaction.commit();//提交事务
    }

    /*
     * 隐藏所有的Fragment
     * */
    private void hideFragment(FragmentTransaction transaction) {
        if (mainHomeFragment != null) {
            transaction.hide(mainHomeFragment);
        }
        if (mainControlFragment != null) {
            transaction.hide(mainControlFragment);
        }
        if (mainMapFragment != null) {
            transaction.hide(mainMapFragment);
        }
        if (mainPersonalFragment != null) {
            transaction.hide(mainPersonalFragment);
        }

    }


    //-----------------------------------------------------------------------------------------------------------------------------------------
    //初始化控件
    private void initView() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navi_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            //主页Home页面的Fragment
            case R.id.menu_main_home:
                setSelect(0);
                break;
            //设备空的页面的Fragment
            case R.id.menu_main_control:
                setSelect(1);
                break;
            //地图页面的Fragment
            case R.id.menu_main_map:
                setSelect(2);
                break;
            //个人中心页面的Fragment
            case R.id.menu_main_personal:
                setSelect(3);
                break;
            default:
                break;
        }

        return true;
    }
}
