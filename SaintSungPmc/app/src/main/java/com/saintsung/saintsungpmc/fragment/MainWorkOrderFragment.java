package com.saintsung.saintsungpmc.fragment;


import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;

import android.content.Intent;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.jwkj.libzxing.activity.CaptureActivity;
import com.saintsung.saintsungpmc.R;

import com.saintsung.saintsungpmc.activity.ApplyManageActivity;
import com.saintsung.saintsungpmc.activity.UnauthorizedActivity;
import com.saintsung.saintsungpmc.activity.WorkOrderApplyActivity;
import com.saintsung.saintsungpmc.adapter.DeviceAdapter;
import com.saintsung.saintsungpmc.bluetoothdata.MyBluetoothManagements;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 蓝牙控制使用GitHub上的框架
 * 框架地址 ：<a href="https://github.com/Jasonchenlijian/FastBle">FastBle项目</a>
 * Created by XLzY on 2017/7/28.
 */

public class MainWorkOrderFragment extends com.saintsung.common.app.Fragment implements View.OnClickListener {
    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;//ListView的Adapter
    private ProgressDialog progressDialog;
    MyBluetoothManagements myBluetoothManagement;
    //使用ButterKnife框架进行注解
    @BindView(R.id.btn_scan)
    Button scanBLE;
    @BindView(R.id.list_device)
    ListView bleListView;
    @BindView(R.id.txt_setting)
    TextView txtSetting;
    @BindView(R.id.layout_setting)
    LinearLayout layoutSetting;
    @BindView(R.id.img_loading)
    ImageView imgLoading;
    @BindView(R.id.txt_title)
    TextView txtTitle;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initData() {
        super.initData();
        txtTitle.setText(R.string.workorder_title);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.connection));
        BleManager.getInstance().init(getActivity().getApplication());
        scanBLE.setOnClickListener(this);
        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(bluetoothAdapterItemOnClick);
        bleListView.setAdapter(mDeviceAdapter);
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        txtSetting.setText(getString(R.string.expand_search_settings));
        txtSetting.setOnClickListener(this);
        layoutSetting.setVisibility(View.GONE);
        BleScanRuleConfig bleScanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(0)
                .build();
        BleManager.getInstance().initScanRule(bleScanRuleConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        showConnectedDevice();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BleManager.getInstance().disconnectAllDevice();
        BleManager.getInstance().destroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //搜索按钮的响应方法
            case R.id.btn_scan:
                //isSupportBle  判断是否该机型能否使用BLE
                if (BleManager.getInstance().isSupportBle()) {
                    //判断蓝牙是否打开
                    if (BleManager.getInstance().isBlueEnable()) {
                        //判断蓝牙是开始扫描还是停止扫描执行对应的方法
                        if (scanBLE.getText().equals(getString(R.string.start_scan))) {
                            scanBlutooth();
                        } else if (scanBLE.getText().equals(getString(R.string.stop_scan))) {
                            //取消蓝牙的搜索
                            BleManager.getInstance().cancelScan();
                        }
                    } else {
                        //打开蓝牙
                        BleManager.getInstance().enableBluetooth();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.please_replacePhone), Toast.LENGTH_LONG).show();
                }
                break;
            //展开小掌机信息及操作结果按钮
            case R.id.txt_setting:
                if (layoutSetting.getVisibility() == View.VISIBLE) {
                    layoutSetting.setVisibility(View.GONE);
                    txtSetting.setText(getString(R.string.expand_search_settings));
                } else {
                    layoutSetting.setVisibility(View.VISIBLE);
                    txtSetting.setText(getString(R.string.retrieve_search_settings));
                }
                break;
            default:
                break;
        }
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
                imgLoading.clearAnimation();
                imgLoading.setVisibility(View.INVISIBLE);
                scanBLE.setText(getString(R.string.start_scan));
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
     * 搜索蓝牙
     */
    private void scanBlutooth() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean success) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
                imgLoading.setVisibility(View.VISIBLE);
                imgLoading.startAnimation(operatingAnim);
                scanBLE.setText(getString(R.string.stop_scan));
            }

            @Override
            public void onScanning(BleDevice result) {
                mDeviceAdapter.addDevice(result);
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                imgLoading.clearAnimation();
                imgLoading.setVisibility(View.INVISIBLE);
                scanBLE.setText(getString(R.string.start_scan));
            }
        });
    }

    @OnClick(R.id.read_parameter)
    void readParameter() {
        myBluetoothManagement.readParameter();
    }

    @OnClick(R.id.set_parameter)
    void setParameter() {
        myBluetoothManagement.setParameter("20181014135615", "E3C565A5", "TensorFlow");
    }

    @OnClick(R.id.down_workorder)
    void downWorkOrder() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    myBluetoothManagement.downloadLockInfo(new String[]{"201804090001", "20180408121020", "20180512082010", "8675761241252531203506521", "2675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521", "3675761241252531203506521"});
                }
            }
        }).start();
    }

    @OnClick(R.id.clean)
    void clean() {
        myBluetoothManagement.cleanInfo();
    }

    @OnClick(R.id.upload)
    void upload() {
        myBluetoothManagement.upload();
    }

    @OnClick(R.id.ble_connect)
    void connect() {
        myBluetoothManagement.connectBluetooth();
    }

    @OnClick(R.id.erweima)
    void saomiao() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.fragment_complete)
    void complete() {
        startActivity(new Intent(getActivity(), UnauthorizedActivity.class));
    }

    @OnClick(R.id.fragment_uncomplete)
    void unComplete() {
        startActivity(new Intent(getActivity(), UnauthorizedActivity.class));
    }

    @OnClick(R.id.fragment_record)
    void record() {
        startActivity(new Intent(getActivity(), UnauthorizedActivity.class));
    }

    @OnClick(R.id.fragment_apply)
    void apply() {
        startActivity(new Intent(getActivity(), WorkOrderApplyActivity.class));
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
}
