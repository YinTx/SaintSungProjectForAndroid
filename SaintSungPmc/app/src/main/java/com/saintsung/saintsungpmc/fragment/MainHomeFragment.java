package com.saintsung.saintsungpmc.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.activity.PersonalActivity;
import com.saintsung.saintsungpmc.adapter.DeviceAdapter;
import com.saintsung.saintsungpmc.bluetoothdata.MyBluetoothManagements;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.saintsung.saintsungpmc.tools.DataProcess.getTiem;
import static com.saintsung.saintsungpmc.tools.ToastUtil.getProgressDialog;
import static com.saintsung.saintsungpmc.tools.ToastUtil.refreshLogView;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainHomeFragment extends Fragment implements MyBluetoothManagements.showState {
    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;//ListView的Adapter
    private BleDevice mBleDevice;
    private ProgressDialog mProgressDialog;
    MyBluetoothManagements myBluetoothManagement;
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.lst_authorized)
    ListView bleListView;
    @BindView(R.id.operation_record)
    TextView operationRecord;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initData() {
        super.initData();
        mProgressDialog = getProgressDialog(getActivity(), getString(R.string.connection));
        BleManager.getInstance().init(getActivity().getApplication());
        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(bluetoothAdapterItemOnClick);
        bleListView.setAdapter(mDeviceAdapter);
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        operationRecord.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (mBleDevice == null) {
            BleManager.getInstance().destroy();
            openBluetooth();
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
                BleManager.getInstance().destroy();
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

    public void openBluetooth() {
        //isSupportBle  判断是否该机型能否使用BLE
        if (BleManager.getInstance().isSupportBle()) {
            //判断蓝牙是否打开
            if (BleManager.getInstance().isBlueEnable()) {
                setScanRule();
                scan();
            } else {
                //打开蓝牙
                BleManager.getInstance().enableBluetooth();
            }
        }
    }

    private void setScanRule() {
        BleScanRuleConfig bleScanRuleConfig = new BleScanRuleConfig.Builder()
                .setScanTimeOut(0)
                .setServiceUuids(new UUID[]{Constant.uuidWriteService, Constant.uuidNotifyService})
                .build();
        BleManager.getInstance().initScanRule(bleScanRuleConfig);
    }

    public void scan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean b) {
                mDeviceAdapter.clearScanDevice();
                mDeviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();

            }

            @Override
            public void onScanFinished(List<BleDevice> list) {

            }
        });
    }

    private void connect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                mProgressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException e) {
                mProgressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
                openBluetooth();
            }

            @Override
            public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
                mProgressDialog.dismiss();
                mDeviceAdapter.clear();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                myBluetoothManagement = new MyBluetoothManagements(bleDevice);
                myBluetoothManagement.setShowState(MainHomeFragment.this);
                myBluetoothManagement.connectBluetooth();
                //连接成功后开始监听返回的数据
                myBluetoothManagement.notifyBle(bleDevice);
                mBleDevice = bleDevice;
                MyApplication.setMyBluetoothManagements(myBluetoothManagement);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                mProgressDialog.dismiss();
                mDeviceAdapter.removeDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                if (!isActiveDisConnected) {
                    Toast.makeText(getActivity(), getString(R.string.disconnected), Toast.LENGTH_LONG).show();
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
                openBluetooth();
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

    //BleManager.getInstance().disconnectAllDevice(); 该方法存在Bug
    @OnClick(R.id.dis_connect)
    void disConnect() {
        if (mBleDevice != null) {
            BleManager.getInstance().disconnect(mBleDevice);
            mBleDevice = null;
            BleManager.getInstance().destroy();
            MyApplication.setMyBluetoothManagements(null);
            openBluetooth();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("TAG", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("TAG", "onAttach");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("TAG","onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG","onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("TAG","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        showConnectedDevice();
        operationRecord.setText(MyApplication.getOperationRecord());
        Log.e("TAG","onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("TAG","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("TAG","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("TAG","onDetach");
    }

    @OnClick(R.id.img_portrait)
    void portait() {
        startActivity(new Intent(getActivity(), PersonalActivity.class));
    }

    @OnClick(R.id.txt_title)
    void clon() {
        myBluetoothManagement.cleanInfo();
    }



    @Override
    public void showState(String state) {
        refreshLogView(operationRecord, getTiem() + " " + state + "\r\n");
    }
}
