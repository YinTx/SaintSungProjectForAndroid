package com.saintsung.saintsungpmc.bluetoothdata;

import android.bluetooth.BluetoothGatt;


import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.saintsung.saintsungpmc.myinterface.IBluetoothInterface;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/8.
 */

public class BluetoothImp implements IBluetoothInterface {
    @Override
    public void connect(BleDevice bleDevice) {
        mConnect(bleDevice);
    }

    @Override
    public void scan() {
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanStarted(boolean b) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {

            }

            @Override
            public void onScanFinished(List<BleDevice> list) {

            }
        });
    }

    public Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true)
                try {
                    openBluetooth();
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }
    });

    public void openBluetooth() {
        //isSupportBle  判断是否该机型能否使用BLE
        if (BleManager.getInstance().isSupportBle()) {
            //判断蓝牙是否打开
            if (BleManager.getInstance().isBlueEnable())
                scan();
            else {
                //打开蓝牙
                BleManager.getInstance().enableBluetooth();
                scan();
            }

        }
    }

    private void mConnect(BleDevice bleDevice) {
        BleManager.getInstance().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException e) {

            }

            @Override
            public void onConnectSuccess(final BleDevice bleDevice, BluetoothGatt gatt, int status) {

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice bleDevice, BluetoothGatt gatt, int status) {
                if (!isActiveDisConnected) {
                    ObserverManager.getInstance().notifyObserver(bleDevice);
                }
            }
        });
    }
}
