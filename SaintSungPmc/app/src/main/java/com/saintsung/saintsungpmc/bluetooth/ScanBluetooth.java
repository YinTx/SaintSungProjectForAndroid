package com.saintsung.saintsungpmc.bluetooth;

import com.clj.fastble.conn.BleScanCallback;
import com.clj.fastble.data.ScanResult;

import java.util.List;

/**
 * Created by XLzY on 2017/11/22.
 */

public class ScanBluetooth extends BleScanCallback {
    @Override
    public void onScanStarted() {
        // 扫描开始
    }

    @Override
    public void onScanning(ScanResult result) {
        // 扫描到一个符合条件的BLE设备
    }

    @Override
    public void onScanFinished(List<ScanResult> scanResultList) {
// 扫描结束
    }
}
