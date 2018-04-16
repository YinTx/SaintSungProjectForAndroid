package com.saintsung.saintsungpmc.bluetoothdata;

import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downLoadWorkOrder;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.downWorkOrderTime;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.readS00;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.sendParameterEndPack;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.setParameterSubpackage1;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.setParameterSubpackage2;

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
     *                           最长可设置16个数字、字母、汉字
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
        if (workOrder != null && workOrder.length() == 16) {
            return downLoadWorkOrder(workOrder);
        }
        return null;
    }

    /**
     * 发送工单有效时间
     * @param starTime 长度等于14
     * @param endTime 长度等于14
     * @return
     */
    public static byte[] sendWorkOrderTime(String starTime, String endTime) {
        if (starTime != null && starTime.length() == 14 && endTime != null && endTime.length() == 14) {
            return downWorkOrderTime(starTime, endTime);
        }
        return null;
    }
}
