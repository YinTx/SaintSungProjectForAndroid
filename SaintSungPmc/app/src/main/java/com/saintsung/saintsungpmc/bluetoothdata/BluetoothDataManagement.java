package com.saintsung.saintsungpmc.bluetoothdata;

import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.cleanLockWorkOrder;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.connectBluetooth;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downLoadEndPackage;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downLoadLockInfo;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downLoadWorkOrder;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downWorkOrderTime;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.readS00;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.sendParameterEndPack;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.setParameterSubpackage1;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.setParameterSubpackage2;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.startLockInfo;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.upLoadOpenLockRecord;

/**
 * Created by EvanShu on 2018/4/16.
 */

public class BluetoothDataManagement {
    public interface S00BluetoothInterface {

    }
    /**
     * 读取参数
     *
     * @return
     */
    public static byte[] readParameter() {
        return readS00();
    }

    /**
     * 设置参数
     *
     * @param parameterStringArr 规定第一个为
     *                           时间 格式（yyyyMMddHHmmss）
     *                           第二个为序列号（为字母与数组组合 8个）
     *                           第三个为名称 最长可设置16个数字、字母、汉字
     */
    public static byte[][] setParameter(String[] parameterStringArr) {
        byte[][] bytes = new byte[3][20];
        if (parameterStringArr.length == 3) {
            bytes[0] = setParameterSubpackage1(parameterStringArr[1], parameterStringArr[2]);
            bytes[1] = setParameterSubpackage2(parameterStringArr[3]);
            bytes[2] = sendParameterEndPack();
            return bytes;
        } else {
            return null;
        }
    }

    /**
     * 发送工单编号
     *
     * @param workOrder
     * @return
     */
    public static byte[] sendWorkOrderNumber(String workOrder) {
        if (workOrder != null && workOrder.length() == 12) {
            return downLoadWorkOrder(workOrder);
        }
        return null;
    }

    /**
     * 发送工单有效时间
     *
     * @param starTime 长度等于14
     * @param endTime  长度等于14
     * @return
     */
    public static byte[] sendWorkOrderTime(String starTime, String endTime) {
        if (starTime != null && starTime.length() == 14 && endTime != null && endTime.length() == 14) {
            return downWorkOrderTime(starTime, endTime);
        }
        return null;
    }

    /**
     * 锁具信息下载开始包
     *
     * @param dataLength 数据长度（最大值为256，为16的倍数）
     * @return
     */
    public static byte[] sendStartLockInfo(int dataLength) {
        if (dataLength % 16 != 0 || dataLength > 256) {
            return null;
        }
        return startLockInfo(Integer.toHexString(dataLength), Integer.toHexString(dataLength / 16));
    }

    /**
     * 发送锁具信息数据 将String数据打包成为byte[]
     *
     * @param lockInfo 二阶数组 第一阶存储N条锁具信息 第二阶存储锁具信息详情 第一位为锁号 第二位为开锁码 第三位为类型
     * @return
     */
    public static byte[][] sendLockInfoData(String[][] lockInfo) {
        byte[][] newBytes = new byte[lockInfo.length][];
        for (int i = 0; i < lockInfo.length; i++) {
            newBytes[i] = downLoadLockInfo(i+1+ "", lockInfo[i][0], lockInfo[i][1], lockInfo[i][2]);
        }
        return newBytes;
    }

    /**
     * 发送工单下载结束（一个大包）
     * @return
     */
    public static byte[] sendEndLockInfo(){
        return downLoadEndPackage();
    }

    /**
     * 清除开锁记录以及工单
     * @return
     */
    public static byte[] sendCleanData(){
        return cleanLockWorkOrder();
    }

    /**
     * 上传开锁记录信息
     * @return
     */
    public static byte[] uploadOpenLockRecord(){
        return upLoadOpenLockRecord();
    }

    /**
     * 连接蓝牙设备
     * @return
     */
    public static byte[] connectBluetoothInterface(){
        return connectBluetooth();
    }
}
