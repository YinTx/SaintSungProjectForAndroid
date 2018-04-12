package com.saintsung.saintsungpmc.bluetoothdata;

import android.util.Log;

import com.saintsung.saintsungpmc.tools.CRC;

import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.*;

/**
 * Created by EvanShu on 2018/4/11.
 */

public class ReceiveBluetoothData {
    public static void getReceiveBluetoothData(byte[] bytes) {
        String result = checkPackageStartEnd(bytes);
        if (result.equals("0"))
            startPackage(bytes[3]);
        else if (result.equals("1"))
            startDara(bytes);
        else if (result.equals("2"))
            endPackage(bytes);
        else if (result.equals("CRCError"))
            Log.e("TAG", "CRC校验不正确！");
        else if (result.equals("PackageError")) {
            //数据包不完整
            Log.e("TAG", "数据包不完整!");
        }

    }


    /**
     * 检查包的完整性
     *
     * @param bytes
     * @return
     */
    private static String checkPackageStartEnd(byte[] bytes) {
        byte startPack = bytes[0];
        byte endPack = bytes[19];
        if (startPack == 0xB0 && endPack == 0xF0) {
            if (checkCRC(bytes, 4))
                return "0";
            else
                return "CRCError";
        } else if (startPack == 0xB1) {
            if (checkCRC(bytes, 4))
                return "1";
            else
                return "CRCError";
        } else if (startPack == 0xB2 && endPack == 0xF2) {
            if (checkCRC(bytes, 4))
                return "2";
            else
                return "CRCError";
        }
        return "PackageError";
    }

    private static boolean checkCRC(byte[] bytes, int sum) {
        byte[] newBytes = new byte[bytes.length - sum];
        for (int i = 0; i < newBytes.length; i++)
            newBytes[i] = bytes[i + 1];
        if (sum == 4) {
            byte crc1 = bytes[bytes.length - 1];
            byte crc2 = bytes[bytes.length - 2];
            byte[] mCRC = HexString2Bytes(Integer.toHexString(CRC.calcCrc16(newBytes)));
            if (crc1 == mCRC[0] && crc2 == mCRC[1])
                return true;
            else
                return false;
        } else {
            byte crc1 = bytes[bytes.length];
            byte crc2 = bytes[bytes.length - 1];
            byte[] mCRC = HexString2Bytes(Integer.toHexString(CRC.calcCrc16(newBytes)));
            if (crc1 == mCRC[0] && crc2 == mCRC[1])
                return true;
            else
                return false;
        }

    }

    private static void startPackage(byte command) {
        switch (command) {
            case 0x10:
                //开始读取S00参数
                break;
            case 0x20:
                //设置S00参数结果的返回
                break;
            case 0x30:
                //执行开设备操作
                break;
            case 0x40:
                //执行关设备操作
                break;
            case 0x50:
                //在线发送设备操作码
                break;
            case 0x60:
                //执更新S00时间
                break;
            case 0x70:
                //工单下载反馈（一个包）
                break;
            case 0x71:
                //清除记录反馈
                break;
            case (byte) 0x80:
                //上传S00操作记录开始包
                break;
            case (byte) 0x90:
                //告诉APP，掌机进入待机模式
                break;
            case (byte) 0x91:
                //掌机进入自动关机
                break;
            case (byte) 0x92:
                //掌机低压模式
                break;
            case (byte) 0x93:
                //掌机开锁状态标志
                break;
            case (byte) 0x94:
                //掌机关锁状态标志
                break;
            default:
                break;
        }
    }

    private static void endPackage(byte[] bytes) {
    }

    private static void startDara(byte[] bytes) {
    }
}
