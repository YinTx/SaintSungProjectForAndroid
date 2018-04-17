package com.saintsung.saintsungpmc.bluetoothdata;

import com.clj.fastble.utils.HexUtil;
import com.saintsung.saintsungpmc.tools.CRC;


/**
 * Created by EvanShu on 2018/4/11.
 */

public class SendBluetoothData {
    private static byte[] setS00Parameter = new byte[]{0x00, 0x00, 0x20};
    private static byte[] readS00Parameter = new byte[]{0x00, 0x00, 0x10};

    /**
     * 协议第一条读取小掌机参数
     *
     * @return
     */
    public static byte[] readS00() {
        byte[] bytes = readS00Parameter;
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    /**
     * 协议第二条设置参数 开始包
     *
     * @return
     */
    public static byte[] setParameterPackage() {
        byte[] bytes = increaseNullByte(setS00Parameter, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0XF0);
        return bytes;
    }

    public static byte[] setParameterSubpackage1(String timeStr, String serialNumberStr) {
        byte[] yearBytes = HexString2Bytes(timeStr.substring(0, 4));
        byte[] monthBytes = HexString2Bytes(timeStr.substring(4, 6));
        byte[] dayBytes = HexString2Bytes(timeStr.substring(6, 8));
        byte[] hourBytes = HexString2Bytes(timeStr.substring(8, 10));
        byte[] minuteBytes = HexString2Bytes(timeStr.substring(10, 12));
        byte[] secondBytes = HexString2Bytes(timeStr.substring(12, 14));
        byte[] serialNumberBytes = HexString2Bytes(serialNumberStr);
        byte[] bytes = new byte[]{0x01, yearBytes[1], yearBytes[0], monthBytes[0], dayBytes[0], hourBytes[0], minuteBytes[0], secondBytes[0], serialNumberBytes[3], serialNumberBytes[2], serialNumberBytes[1], serialNumberBytes[0], 0x00, 0x00};
        bytes = increaseNullByte(bytes, 3);
        bytes = increaseCRC(bytes);
        bytes = DataStarPackage(bytes, (byte) 0xA1);
        return bytes;
    }

    public static byte[] setParameterSubpackage2(String secretKeyName) {
        byte[] secretKeyNameBytes = secretKeyName.getBytes();
        byte[] bytes = new byte[]{0x02};
        secretKeyNameBytes = fillNullByte(secretKeyNameBytes, 16);
        bytes = addBytes(bytes, secretKeyNameBytes);
        bytes = increaseCRC(bytes);
        bytes = DataStarPackage(bytes, (byte) 0xA1);
        return bytes;
    }

//    public static byte[] setParameterSubpackage3(String softwareVersion, String hardwareVersion) {
//        byte[] largeSoftwareBytes = HexString2Bytes(softwareVersion.substring(0, 1));
//        byte[] mediumSoftwareBytes = HexString2Bytes(softwareVersion.substring(1, 2));
//        byte[] minorSoftwareBytes = HexString2Bytes(softwareVersion.substring(2, 3));
//
//        byte[] largeHardwareBytes = HexString2Bytes(hardwareVersion.substring(0, 1));
//        byte[] mediumHardwareBytes = HexString2Bytes(hardwareVersion.substring(1, 2));
//        byte[] minorHardwareBytes = HexString2Bytes(hardwareVersion.substring(2, 3));
//        byte[] bytes = new byte[]{0x03, minorSoftwareBytes[0], mediumSoftwareBytes[0], largeSoftwareBytes[0], minorHardwareBytes[0], mediumHardwareBytes[0], largeHardwareBytes[0]};
//        bytes = increaseNullByte(bytes, 10);
//        bytes = increaseCRC(bytes);
//        bytes = DataStarPackage(bytes, (byte) 0xA1);
//        return bytes;
//    }

    public static byte[] sendParameterEndPack() {
        byte[] bytes = new byte[]{0x00, 0x00, 0x20};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA2, (byte) 0xF2);
        return bytes;
    }

    private static byte[] setZeroData(byte[] bytes, int number) {
        byte[] newBytes = new byte[bytes.length + number];
        for (int i = 0; i < newBytes.length; i++) {
            if (i < number)
                newBytes[i] = bytes[i];
            else
                newBytes[i] = 0X00;
        }
        return bytes;
    }

    /**
     * 今天在于硬件进行交互的过程中,要到了了需要两个数组进行合并,然后对数组进行反转和加密操作,以下是两个byte数组合并的方法.
     *
     * @param data1
     * @param data2
     * @return data1 与 data2拼接的结果
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 数据包 结束
     *
     * @param bytes
     * @return
     */
    private byte[] DataEndPackage(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length + 2];
        newBytes[0] = (byte) 0xB2;
        newBytes[newBytes.length] = (byte) 0xF2;
        for (int i = 1; i < newBytes.length - 1; i++) {
            newBytes[i] = bytes[i - 1];
        }
        return newBytes;
    }

    /**
     * 数据包 开始
     *
     * @return
     */
    private static byte[] DataStarPackage(byte[] bytes, byte starPack) {
        byte[] newBytes = new byte[bytes.length + 1];
        newBytes[0] = starPack;
        for (int i = 1; i < newBytes.length; i++) {
            newBytes[i] = bytes[i - 1];
        }
        return newBytes;
    }

    /**
     * CRC校验
     *
     * @param bytes
     * @return
     */
    public static byte[] increaseCRC(byte[] bytes) {
        byte[] newBytes = new byte[bytes.length + 2];
        for (int i = 0; i < bytes.length; i++) {
            newBytes[i] = bytes[i];
        }
        String crc = Integer.toHexString(CRC.calcCrc16(bytes));
        byte[] bytesCRC = HexString2Bytes(crc);
        newBytes[newBytes.length - 2] = bytesCRC[1];
        newBytes[newBytes.length - 1] = bytesCRC[0];
        return newBytes;
    }

    public static byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    public static byte[] HexString2Bytes(String src) {
        if (src.length() % 2 != 0) {
            src = "0" + src;
        }
        byte[] ret = new byte[src.length() / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < src.length() / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 增加数据
     *
     * @param bytes  需要补充的数据
     * @param number 需要补充的个数
     * @return
     */
    static byte[] increaseNullByte(byte[] bytes, int number) {
        byte[] newBytes = new byte[bytes.length + number];
        for (int i = 0; i < newBytes.length; i++) {
            if (i < bytes.length)
                newBytes[i] = bytes[i];
            else
                newBytes[i] = (byte) 0x0D;
        }
        return newBytes;
    }

    /**
     * 填充数据
     *
     * @param bytes
     * @param number
     * @return
     */
    static byte[] fillNullByte(byte[] bytes, int number) {
        return increaseNullByte(bytes, number - bytes.length);
    }

    /**
     * 该方法是对APP发送数据到下位机包进行封装
     *
     * @param bytes 需要封装的数据
     * @return
     */
    static byte[] increasePackage(byte[] bytes, byte packStar, byte packEnd) {
        byte[] newBytes = new byte[bytes.length + 2];
        newBytes[0] = packStar;
        newBytes[newBytes.length - 1] = packEnd;
        for (int i = 1; i < newBytes.length - 1; i++) {
            newBytes[i] = bytes[i - 1];
        }
        return newBytes;
    }

    /**
     * 发送开锁
     *
     * @param lockNumber
     * @param lockType
     * @return
     */
    public static byte[] sendLockNumber(String lockNumber, String lockType) {
        byte[] bytes = new byte[]{0x0C, 0x00, 0x30};
        byte[] lockOneGroup = HexString2Bytes(decimalSystemHexadecimal(lockNumber.substring(0, 3)));
        byte[] lockTwoGroup = HexString2Bytes(decimalSystemHexadecimal(lockNumber.substring(3, 6)));
        byte[] lockThreeGroup = HexString2Bytes(decimalSystemHexadecimal(lockNumber.substring(6, 9)));
        byte[] lockFourGroup = HexString2Bytes(decimalSystemHexadecimal(lockNumber.substring(9, 12)));
        byte[] lockFiveGroup = HexString2Bytes(decimalSystemHexadecimal(lockNumber.substring(12, 15)));
        bytes = increaseHandle(lockOneGroup, bytes);
        bytes = increaseHandle(lockTwoGroup, bytes);
        bytes = increaseHandle(lockThreeGroup, bytes);
        bytes = increaseHandle(lockFourGroup, bytes);
        bytes = increaseHandle(lockFiveGroup, bytes);
        bytes = setZeroData(bytes, 1);
        bytes = addBytes(bytes, lockType.getBytes());
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    private static byte[] increaseHandle(byte[] bytes, byte[] oldBytes) {
        byte[] newBytes = new byte[oldBytes.length + 2];
        for (int i = 0; i < oldBytes.length; i++) {
            newBytes[i] = oldBytes[i];
        }
        if (bytes.length < 2) {
            newBytes[newBytes.length - 2] = 0x00;
            newBytes[newBytes.length - 1] = bytes[0];
        } else {
            newBytes[newBytes.length - 2] = bytes[1];
            newBytes[newBytes.length - 1] = bytes[0];
        }
        return newBytes;
    }

    private static String decimalSystemHexadecimal(String string) {
        Integer integer = new Integer(string);
        if (integer > 256)
            return "0" + integer.toHexString(integer);
        else
            return integer.toHexString(integer);
    }

    /**
     * 发送关锁
     *
     * @return
     */
    public static byte[] sendCloseLock() {
        byte[] bytes = new byte[]{0x00, 0x00, 0x40};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    /**
     * 下载工单
     *
     * @param workOrderNumber
     * @return
     */
    public static byte[] downLoadWorkOrder(String workOrderNumber) {
        byte[] bytes = new byte[]{0x8, 0x0, 0x50};
        byte[] newBytes = HexUtil.hexStringToBytes(workOrderNumber);
        bytes = transPositionBytes(bytes, newBytes);
        bytes = increaseNullByte(bytes, 5);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    private static byte[] transPositionBytes(byte[] bytes, byte[] addBytes) {
        byte[] newBytes = new byte[bytes.length + addBytes.length];
        for (int i = 0; i < newBytes.length; i++) {
            if (i < bytes.length)
                newBytes[i] = bytes[i];
            else
                newBytes[i] = addBytes[newBytes.length - i];
        }
        return newBytes;
    }

    /**
     * 下载工单有效时间
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static byte[] downWorkOrderTime(String startTime, String endTime) {
        byte[] yearStartBytes = HexString2Bytes(startTime.substring(0, 4));
        byte[] monthStartBytes = HexString2Bytes(startTime.substring(4, 6));
        byte[] dayStartBytes = HexString2Bytes(startTime.substring(6, 8));
        byte[] hourStartBytes = HexString2Bytes(startTime.substring(8, 10));
        byte[] minuteStartBytes = HexString2Bytes(startTime.substring(10, 12));
        byte[] secondStartBytes = HexString2Bytes(startTime.substring(12, 14));

        byte[] yearEndBytes = HexString2Bytes(endTime.substring(0, 4));
        byte[] monthEndBytes = HexString2Bytes(endTime.substring(4, 6));
        byte[] dayEndBytes = HexString2Bytes(endTime.substring(6, 8));
        byte[] hourEndBytes = HexString2Bytes(endTime.substring(8, 10));
        byte[] minuteEndBytes = HexString2Bytes(endTime.substring(10, 12));
        byte[] secondEndBytes = HexString2Bytes(endTime.substring(12, 14));
        byte[] bytes = new byte[]{0x8, 0x00, 0x60, yearStartBytes[1], yearStartBytes[0], monthStartBytes[0], dayStartBytes[0], hourStartBytes[0], minuteStartBytes[0], secondStartBytes[0], yearEndBytes[1], yearEndBytes[0], monthEndBytes[0], dayEndBytes[0], hourEndBytes[0], minuteEndBytes[0], secondEndBytes[0]};
        bytes = increaseCRC(bytes);
        bytes = DataStarPackage(bytes, (byte) 0xA0);
        return bytes;
    }

    /**
     * 开始锁具信息下载
     *
     * @return
     */
    public static byte[] startLockInfo(String dataLength, String dataPackage) {
        byte[] LengthBytes = HexString2Bytes(dataLength);
        byte[] dataPackageBytes = HexString2Bytes(dataPackage);
        byte[] bytes = new byte[]{LengthBytes[0], dataPackageBytes[0], 0x70};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    /**
     * 下载锁具信息（单条）
     *
     * @param packCon        当前条数
     * @param lockNumber     锁号
     * @param openLockNumber 开锁码
     * @param lockType       锁具类型
     * @return
     */
    public static byte[] downLoadLockInfo(String packCon, String lockNumber, String openLockNumber, String lockType) {
        byte[] lockBytes = HexUtil.hexStringToBytes(lockNumber);
        byte[] lockOneGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(0, 3)));
        byte[] lockTwoGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(3, 6)));
        byte[] lockThreeGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(6, 9)));
        byte[] lockFourGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(9, 12)));
        byte[] lockFiveGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(12, 15)));
        byte[] bytes = HexUtil.hexStringToBytes(packCon);
        byte[] newBytes = new byte[]{lockBytes[3], lockBytes[2], lockBytes[1], lockBytes[0]};
        bytes = addBytes(bytes, newBytes);
        bytes = increaseHandle(lockOneGroup, bytes);
        bytes = increaseHandle(lockTwoGroup, bytes);
        bytes = increaseHandle(lockThreeGroup, bytes);
        bytes = increaseHandle(lockFourGroup, bytes);
        bytes = increaseHandle(lockFiveGroup, bytes);
        bytes = setZeroData(bytes, 1);
        bytes = addBytes(bytes, HexUtil.hexStringToBytes(lockType));
        bytes = increaseCRC(bytes);
        return bytes;
    }

    /**
     * 工单下载结束包（一个大包）
     *
     * @return
     */
    public static byte[] downLoadEndPackage() {
        byte[] bytes = new byte[]{0x00, 0x00, 0x70};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA2, (byte) 0xF2);
        return bytes;
    }

    /**
     * 清除小掌机中的工单以及锁具信息
     *
     * @return
     */
    public static byte[] cleanLockWorkOrder() {
        byte[] bytes = new byte[]{0x00, 0x00, 0x71};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    /**
     * 请求上传开锁记录包
     *
     * @return
     */
    public static byte[] upLoadOpenLockRecord() {
        byte[] bytes = new byte[]{0x00, 0x00, (byte) 0x80};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }

    /**
     * 连接蓝牙
     *
     * @return
     */
    public static byte[] connectBluetooth() {
        byte[] bytes = new byte[]{0x00, 0x00, (byte) 0xA0};
        bytes = increaseNullByte(bytes, 13);
        bytes = increaseCRC(bytes);
        bytes = increasePackage(bytes, (byte) 0xA0, (byte) 0xF0);
        return bytes;
    }
}
