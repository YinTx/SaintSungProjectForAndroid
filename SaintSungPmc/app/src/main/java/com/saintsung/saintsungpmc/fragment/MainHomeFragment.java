package com.saintsung.saintsungpmc.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.activity.PersonalActivity;
import com.saintsung.saintsungpmc.adapter.DeviceAdapter;
import com.saintsung.saintsungpmc.bluetoothdata.MyBluetoothManagements;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainHomeFragment extends Fragment {
    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;//ListView的Adapter
    private ProgressDialog progressDialog;
    MyBluetoothManagements myBluetoothManagement;
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.lst_authorized)
    ListView bleListView;
    @BindView(R.id.fragment_mac_address)
    TextView macAddress;
    @BindView(R.id.fragment_signal)
    TextView signal;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();
        Glide.with(this).load(R.drawable.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
            @Override
            public void onResourceReady(GlideDrawable glideDrawable, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(glideDrawable.getCurrent());
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.connection));
        BleManager.getInstance().init(getActivity().getApplication());
        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(bluetoothAdapterItemOnClick);
        bleListView.setAdapter(mDeviceAdapter);
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        BleManager.getInstance().init(getActivity().getApplication());
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
            Toast.makeText(getActivity(), getString(R.string.please_replacePhone), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 搜索蓝牙
     */

    private void scanBlutooth() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanning(BleDevice result) {
                mDeviceAdapter.addDevice(result);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
            }
        });
    }

    /**
     * 这个是搜索蓝牙显示在ListView中单项点击按钮监听事件
     */
    DeviceAdapter.OnDeviceClickListener bluetoothAdapterItemOnClick = new DeviceAdapter.OnDeviceClickListener() {
        @Override
        public void onConnect(BleDevice bleDevice) {
            if (!BleManager.getInstance().isConnected(bleDevice)) {
                BleManager.getInstance().cancelScan();
                connect(bleDevice);
            }
        }

        @Override
        public void onDisConnect(BleDevice bleDevice) {
            if (BleManager.getInstance().isConnected(bleDevice)) {
                BleManager.getInstance().disconnect(bleDevice);
            }
        }

        @Override
        public void onDetail(BleDevice bleDevice) {
            if (BleManager.getInstance().isConnected(bleDevice)) {
            }
        }
    };

    private void connect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleException exception) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                myBluetoothManagement = new MyBluetoothManagements(bleDevice);
                //连接成功后开始监听返回的数据
                myBluetoothManagement.notifyBle(bleDevice);
                macAddress.setText(bleDevice.getMac());
                signal.setText(bleDevice.getRssi());
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();

                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

                if (!isActiveDisConnected) {
                    Toast.makeText(getActivity(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
            }
        });
    }

    /**
     * 扫描到蓝牙刷新ListView
     */
    private void showConnectedDevice() {
        List<BleDevice> deviceList = BleManager.getInstance().getAllConnectedDevice();
        mDeviceAdapter.clearConnectedDevice();
        for (BleDevice bleDevice : deviceList) {
            mDeviceAdapter.addDevice(bleDevice);
        }
        mDeviceAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.img_portrait)
    void portait() {
        startActivity(new Intent(getActivity(), PersonalActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        showConnectedDevice();
    }
}
