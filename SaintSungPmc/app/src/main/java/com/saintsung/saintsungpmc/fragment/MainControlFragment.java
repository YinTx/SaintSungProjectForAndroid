package com.saintsung.saintsungpmc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.conn.BleScanCallback;
import com.clj.fastble.data.ScanResult;
import com.saintsung.saintsungpmc.R;
import com.saintsung.saintsungpmc.bluetooth.ScanBluetooth;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
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
                    bleManager.scan(new ScanBluetooth());

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "请更换成蓝牙4.0版本及以上的蓝牙版本再试！", Toast.LENGTH_LONG).show();
                }
                Log.e("TAG", bleManager.isSupportBle() + "");
                break;
            default:
                break;
        }
    }
}
