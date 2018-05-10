package com.saintsung.saintsungpmc.bluetoothdata;

import android.bluetooth.BluetoothGatt;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.fragment.MainHomeFragment;
import com.saintsung.saintsungpmc.myinterface.IBluetoothInterface;
import com.saintsung.saintsungpmc.observice.ObserverManager;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/8.
 */

public class BluetoothImp implements IBluetoothInterface{
    @Override
    public void connect(BleDevice bleDevice){
        mConnect(bleDevice);
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
