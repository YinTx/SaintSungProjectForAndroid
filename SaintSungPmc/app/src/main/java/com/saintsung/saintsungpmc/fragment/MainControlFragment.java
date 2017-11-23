package com.saintsung.saintsungpmc.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleScanCallback;
import com.clj.fastble.data.ScanResult;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.saintsung.saintsungpmc.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 蓝牙控制使用GitHub上的框架
 * 框架地址 ：<a href="https://github.com/Jasonchenlijian/FastBle">FastBle项目</a>
 * Created by XLzY on 2017/7/28.
 */

public class MainControlFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.open_ble)
    Button openBle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        ButterKnife.bind(this, view);
        openBle.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_ble:
                BleManager bleManager = new BleManager(getActivity());
                if (bleManager.isSupportBle()) {
                    bleManager.enableBluetooth();
                    if (bleManager.isBlueEnable()) {
                        scanBlutooth(bleManager);
                    } else {

                    }

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "请更换成蓝牙4.0版本及以上的蓝牙版本再试！", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 搜索蓝牙
     *
     * @param bleManager 传入FastBle中的类BleManager
     */
    private void scanBlutooth(BleManager bleManager) {
        BleScanRuleConfig bleScanRuleConfig = new BleScanRuleConfig();
        bleManager.initScanRule(bleScanRuleConfig);
        bleManager.scan(new BleScanCallback() {
            @Override
            public void onScanStarted() {

            }

            @Override
            public void onScanning(ScanResult result) {

            }

            @Override
            public void onScanFinished(List<ScanResult> scanResultList) {

            }
        });
    }


}
