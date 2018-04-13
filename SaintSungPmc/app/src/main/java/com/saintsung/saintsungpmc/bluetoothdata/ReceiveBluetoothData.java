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
            startData(bytes);
        else if (result.equals("2"))
            endPackage(bytes[3]);
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

    /**
     * 结束包
     *
     * @param command
     */
    private static void endPackage(byte command) {
        switch (command) {
            case 0x10:
                //结束读取S00参数
                break;
            case 0x20:
                //设置S00参数结果的返回
                break;
            case (byte) 0x80:
                //上传S00操作记录结束包
                break;
            default:
                break;
        }
    }

    /**
     * 这个包是协议第一条接收读取掌机参数的方法
     *
     * @param bytes
     */
    private static void startData(byte[] bytes) {
        byte packBytes = bytes[1];
        switch (packBytes) {
            case 0x01:
                byte[] timeBytes = new byte[]{bytes[3], bytes[2], bytes[4], bytes[5], bytes[6]};
                byte[] serialNumberBytes = new byte[]{bytes[10], bytes[9], bytes[8], bytes[7]};
                break;
            case 0x02:
                byte[] secretKey = subBytes(bytes, 2, 18);
                break;
            case 0x03:
                byte[] softwareVersion = new byte[]{bytes[4], bytes[3], bytes[2]};
                byte[] hardwareVersion = new byte[]{bytes[7], bytes[6], bytes[5]};
                break;
            default:
                break;
        }
    }

    private static void upLoadOpenLockRecordStart(byte[] bytes) {
        byte[] dataPackage = new byte[]{bytes[1], bytes[2]};
        byte[] dataLength = new byte[]{bytes[7], bytes[6], bytes[5], bytes[4]};
        byte result = bytes[8];
    }

    private static void upLoadOpenLockOneRecord(byte[] bytes) {
        byte dataPack = bytes[1];
        if (dataPack == 0x01) {
            byte[] serialNumber = new byte[]{bytes[5], bytes[4], bytes[3], bytes[2]};
            byte[] workOrderNumber = new byte[]{bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8], bytes[7], bytes[6]};
        } else if (0x02 < dataPack && dataPack <= 0x65) {
            byte[] lockNumber = new byte[]{bytes[5], bytes[4], bytes[3], bytes[2]};
            byte[] lockTime = new byte[]{bytes[7], bytes[6], bytes[8], bytes[9], bytes[10], bytes[11], bytes[12]};
            byte lockState = bytes[13];
            byte lcokResult = bytes[14];
        }
    }

    private static byte[] subBytes(byte[] bytes, int start, int end) {
        byte[] newBytes = new byte[end - start];
        for (int i = start; i < end; i++) {
            newBytes[i - start] = bytes[i];
        }
        return newBytes;
    }
}
