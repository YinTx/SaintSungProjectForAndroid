package com.saintsung.saintsungpmc.bluetoothdata;

import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.saintsung.saintsungpmc.configure.Constant;

import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataManagement.connectBluetoothInterface;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataManagement.sendLockInfoData;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataManagement.sendStartLockInfo;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataManagement.sendWorkOrderNumber;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataManagement.sendWorkOrderTime;
import static com.saintsung.saintsungpmc.bluetoothdata.ReceiveBluetoothData.getReceiveBluetoothData;

/**
 * 这个类是用于控制蓝牙接收发数据
 * Created by EvanShu on 2018/4/17.
 */

public class MyBluetoothManagement implements ReceiveBluetoothData.resultData {
    private BleDevice bleDevice;
    private String[] workOrderInfo;
    private int signStrip = 0;

    MyBluetoothManagement(BleDevice bleDevice) {
        this.bleDevice = bleDevice;
    }

    /**
     * 每次连接蓝牙自动发送该连接方法验证是否能通讯
     */
    public void connectBluetooth() {
        write(bleDevice, connectBluetoothInterface());
    }

    /**
     * 读取参数
     */
    public void readParameter() {
        write(bleDevice, BluetoothDataManagement.readParameter());

    }

    /**
     * 设置参数
     *
     * @param time         时间
     * @param serialNumber 序列号
     * @param keyName      名称
     */
    public void setParameter(String time, String serialNumber, String keyName) {
        String[] parameter = new String[]{time, serialNumber, keyName};
        byte[][] bytesArr = BluetoothDataManagement.setParameter(parameter);
        for (int i = 0; i < bytesArr.length; i++) {
            // TODO 如果发送不成功则可能是蓝牙发送速率太快导致先发送结束包，处理方法是判断是否为最后一个包，最后一个则延时发送200ms
            write(bleDevice, bytesArr[i]);
        }
    }

    public void downloadLockInfo(String[] workOrderInfo) {
        this.workOrderInfo = workOrderInfo;
        downloadWorkOrderNumber();
    }

    /**
     * 下载工单编号
     */
    public void downloadWorkOrderNumber() {
        write(bleDevice, sendWorkOrderNumber(workOrderInfo[0]));
    }

    /**
     * 下载工单有效时间
     *
     * @param starTime
     * @param endTime
     */
    public void downloadTime(String starTime, String endTime) {
        write(bleDevice, sendWorkOrderTime(starTime, endTime));
    }

    public void downloadLockInfo() {
        if (workOrderInfo.length >= 16 * (signStrip + 1) + 3) {
            sendStartLockInfo(256);
            String[][] workOrderArr = new String[16][];
            for (int i = 0; i < 16; i++) {
                String[] lockInfoArr = new String[3];
                int pack = 16 * signStrip + 3 + i;
                lockInfoArr[0] = workOrderInfo[pack].substring(0, 9);
                lockInfoArr[1] = workOrderInfo[pack].substring(9, 24);
                lockInfoArr[2] = workOrderInfo[pack].substring(24, 25);
                workOrderArr[i] = lockInfoArr;
            }
            sendLockInfoData(workOrderArr);
        } else {
            int dataLength = (workOrderInfo.length - 3) % 16;
            sendStartLockInfo(dataLength * 16);
            String[][] workOrderArr = new String[dataLength][];
            for (int i = 0; i < dataLength; i++) {
                String[] lockInfoArr = new String[3];
                int pack = 16 * signStrip + 3 + i;
                lockInfoArr[0] = workOrderInfo[pack].substring(0, 9);
                lockInfoArr[1] = workOrderInfo[pack].substring(9, 24);
                lockInfoArr[2] = workOrderInfo[pack].substring(24, 25);
                workOrderArr[i] = lockInfoArr;
            }
            sendLockInfoData(workOrderArr);
        }
    }

    private void write(BleDevice bleDevice, byte[] bytes) {
        BleManager.getInstance().write(
                bleDevice,
                Constant.uuidService.toString(),
                Constant.uuidWrite.toString(),
                bytes,
                new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess() {
                        // 发送数据到设备成功（UI线程）
                        Log.e("TAG", "发送数据到设备成功");
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {
                        // 发送数据到设备失败（UI线程）
                        Log.e("TAG", "发送数据到设备失败" + exception.toString());
                    }
                });
    }

    public void notifyBle(BleDevice bleDevice) {
        BleManager.getInstance().notify(bleDevice, Constant.uuidService.toString(), Constant.uuidNotify.toString(), new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                // 打开通知操作成功（UI线程）
                Log.e("TAG", "成功");
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                // 打开通知操作失败（UI线程）
                Log.e("TAG", "失败");
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                getReceiveBluetoothData(data);
//                Log.e("TAG", "下位机发送数据：" + HexUtil.formatHexString(data, true));
            }
        });
    }

    @Override
    public void resultCommand(byte result) {
        if (result == 0x50) {
            downloadTime(workOrderInfo[1], workOrderInfo[2]);
        } else if (result == 0x60) {
            downloadLockInfo();
        } else if (result == 0x70) {

        }
    }
}
