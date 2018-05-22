package com.saintsung.saintsungpmc.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.saintsung.common.app.Fragment;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.activity.PersonalActivity;
import com.saintsung.saintsungpmc.adapter.DeviceAdapter;
import com.saintsung.saintsungpmc.bluetoothdata.MyBluetoothManagements;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import static com.saintsung.saintsungpmc.tools.DataProcess.getTiem;
import static com.saintsung.saintsungpmc.tools.ToastUtil.refreshLogView;

/**
 * Created by XLzY on 2017/7/28.
 */

public class MainHomeFragment extends Fragment implements MyBluetoothManagements.showState {
    private Animation operatingAnim;
    private DeviceAdapter mDeviceAdapter;//ListView的Adapter
    private ProgressDialog progressDialog;
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.connection));
        BleManager.getInstance().init(getActivity().getApplication());
        mDeviceAdapter = new DeviceAdapter(getActivity());
        mDeviceAdapter.setOnDeviceClickListener(bluetoothAdapterItemOnClick);
        bleListView.setAdapter(mDeviceAdapter);
        operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());
        operationRecord.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!thread.isAlive())
            thread.start();
    }

    public Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
                try {
                    openBluetooth();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    });

    @Override
    public void onStart() {
        super.onStart();
        operationRecord.setText(MyApplication.getOperationRecord());
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

    public void openBluetooth() {
        //isSupportBle  判断是否该机型能否使用BLE
        if (BleManager.getInstance().isSupportBle()) {
            //判断蓝牙是否打开
            if (BleManager.getInstance().isBlueEnable())
                scan();
            else {
                //打开蓝牙
                BleManager.getInstance().enableBluetooth();
            }
        }
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
                progressDialog.show();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), getString(R.string.connect_fail), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {
                progressDialog.dismiss();
                mDeviceAdapter.addDevice(bleDevice);
                mDeviceAdapter.notifyDataSetChanged();
                myBluetoothManagement = new MyBluetoothManagements(bleDevice);
                myBluetoothManagement.setShowState(MainHomeFragment.this);
                myBluetoothManagement.connectBluetooth();
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

    @OnClick(R.id.dis_connect)
    void disConnect() {
        BleManager.getInstance().disconnectAllDevice();
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

    @Override
    public void showState(String state) {
        refreshLogView(operationRecord, getTiem() + " " + state + "\r\n");
    }
}
