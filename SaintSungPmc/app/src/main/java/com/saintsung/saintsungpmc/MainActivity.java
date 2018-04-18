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
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.saintsung.common.app.Activity;
import com.saintsung.common.widget.BottomNavigationViewHelper;
import com.saintsung.common.widget.PortraitView;
import com.saintsung.saintsungpmc.bluetoothdata.ReceiveBluetoothData;
import com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData;
import com.saintsung.saintsungpmc.fragment.MainControlFragment;
import com.saintsung.saintsungpmc.fragment.MainHomeFragment;
import com.saintsung.saintsungpmc.fragment.MainPersonalFragment;
import com.saintsung.saintsungpmc.tools.NevHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NevHelper.OnTabChangedListener<Integer> {
    private long exitTime = 0;//2次回退计时器
    @BindView(R.id.appbar)
    View mLayAppbar;
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
        BottomNavigationViewHelper.disableShiftMode(mNavigation);
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
        BleManager.getInstance().init(getApplication());
        BleScanRuleConfig bleScanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(0)
                .build();
        BleManager.getInstance().initScanRule(bleScanRuleConfig);
        //isSupportBle  判断是否该机型能否使用BLE
        if (BleManager.getInstance().isSupportBle()) {
            //判断蓝牙是否打开
            if (BleManager.getInstance().isBlueEnable())
                scanBlutooth();
            else {
                //打开蓝牙
                BleManager.getInstance().enableBluetooth();
                scanBlutooth();
            }

        } else {
            Toast.makeText(this, getString(R.string.please_replacePhone), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void initData() {
        super.initData();
        //从底部接管Menu,模拟一次手动点击
        Menu menu = mNavigation.getMenu();
        //触发一次点击
        menu.performIdentifierAction(R.id.menu_main_home, 0);
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
//        mTitle.setText(newTab.extra);
    }

    /**
     * 搜索蓝牙
     */

    private void scanBlutooth() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
//                mDeviceAdapter.clearScanDevice();
//                mDeviceAdapter.notifyDataSetChanged();
//                imgLoading.setVisibility(View.VISIBLE);
//                imgLoading.startAnimation(operatingAnim);
//                scanBLE.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onScanning(BleDevice result) {
//                mDeviceAdapter.addDevice(result);
//                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
//                imgLoading.clearAnimation();
//                imgLoading.setVisibility(View.INVISIBLE);
//                scanBLE.setText(getString(R.string.start_scan));
            }
        });
    }

}
