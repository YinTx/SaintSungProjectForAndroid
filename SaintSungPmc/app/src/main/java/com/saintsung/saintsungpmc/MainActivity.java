package com.saintsung.saintsungpmc;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.saintsung.common.app.Activity;
import com.saintsung.common.widget.PortraitView;
import com.saintsung.saintsungpmc.bluetoothdata.ReceiveBluetoothData;
import com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData;
import com.saintsung.saintsungpmc.fragment.MainControlFragment;
import com.saintsung.saintsungpmc.fragment.MainHomeFragment;
import com.saintsung.saintsungpmc.fragment.MainPersonalFragment;
import com.saintsung.saintsungpmc.tools.NevHelper;

import butterknife.BindView;
import butterknife.OnClick;

import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.setS00Parameter;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NevHelper.OnTabChangedListener<Integer> {
    private long exitTime = 0;//2次回退计时器
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.txt_title)
    TextView mTitle;
    @BindView(R.id.img_portrait)
    PortraitView mPortrait;
    @BindView(R.id.lay_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private NevHelper<Integer> mNevHelper;

    /**
     * 主界面监听返回按钮，在2秒内连续点击2次返回按钮则退出程序
     *
     * @param keyCode
     * @param event
     * @return
     */
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
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNevHelper = new NevHelper(this, getSupportFragmentManager(), R.id.lay_container, this);
        mNevHelper.add(R.id.menu_main_home, new NevHelper.Tab<>(MainHomeFragment.class, R.string.menu_home))
                .add(R.id.menu_main_control, new NevHelper.Tab<>(MainControlFragment.class, R.string.menu_control))
                .add(R.id.menu_main_personal, new NevHelper.Tab<>(MainPersonalFragment.class, R.string.menu_personal));
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this).load(R.drawable.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(glideDrawable.getCurrent());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //从底部接管Menu,模拟一次手动点击
        Menu menu=mNavigation.getMenu();
        //触发一次点击
        menu.performIdentifierAction(R.id.menu_main_home,0);
//        byte[] bytes=SendBluetoothData.increaseCRC(setS00Parameter);


    }

    @OnClick(R.id.img_search)
    void onSearchMenuClick() {
    }

    @OnClick(R.id.btn_action)
    void onActionClick() {
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNevHelper.performClickMenu(item.getItemId());
    }

    /**
     * 处理后的回掉方法
     *
     * @param newTab
     * @param oldTab
     */
    @Override
    public void onTabChanged(NevHelper.Tab<Integer> newTab, NevHelper.Tab<Integer> oldTab) {
        //
        mTitle.setText(newTab.extra);
    }
}
