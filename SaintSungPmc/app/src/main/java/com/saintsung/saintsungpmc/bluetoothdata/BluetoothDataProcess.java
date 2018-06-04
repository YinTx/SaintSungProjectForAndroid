package com.saintsung.saintsungpmc.bluetoothdata;

import com.clj.fastble.utils.HexUtil;
import com.saintsung.saintsungpmc.tools.CRC;

import java.math.BigInteger;

import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.HexString2Bytes;

/**
 * Created by EvanShu on 2018/4/16.
 */

public class BluetoothDataProcess {

    /**
     * 检查包的类型
     *
     * @param bytes
     * @return 返回一个类型或者错误类型
     */
    public static String checkPackageStartEnd(byte[] bytes) {
        int startPack = getUnsignedByte(bytes[0]);
        int endPack = getUnsignedByte(bytes[19]);
        if (startPack == getUnsignedByte((byte) 0xB0) && endPack == getUnsignedByte((byte) 0xF0)) {
            if (checkCRC(bytes, 4))
                return "0";
            else
                return "CRCError";
        } else if (startPack == getUnsignedByte((byte) 0xB1)) {
            if (checkCRC(bytes, 3))
                return "1";
            else
                return "CRCError";
        } else if (startPack == getUnsignedByte((byte) 0xB2) && endPack == getUnsignedByte((byte) 0xF2)) {
            if (checkCRC(bytes, 4))
                return "2";
            else
                return "CRCError";
        } else if (startPack == getUnsignedByte((byte) 0xc0) && endPack == getUnsignedByte((byte) 0xF0)) {
            return "Error";
        }
        return "PackageError";
    }

    /**
     * 检查CRC是否正确
     *
     * @param bytes
     * @param sum   该变量是一个判断bytes数组为有包头包尾还是为只有包头
     *              如果只有包头传3如果有头有尾则传4
     * @return
     */
    public static boolean checkCRC(byte[] bytes, int sum) {
        byte[] newBytes = new byte[bytes.length - sum];
        for (int i = 0; i < newBytes.length; i++)
            newBytes[i] = bytes[i + 1];
        if (sum == 4) {
            int crc1 = bytes[bytes.length - 2];
            int crc2 = bytes[bytes.length - 3];
            byte[] mCRC = HexString2Bytes(Integer.toHexString(CRC.calcCrc16(newBytes)));
            if (mCRC.length == 1) {
                mCRC = new byte[]{0x00, mCRC[0]};
            }
            if (crc1 == mCRC[0] && crc2 == mCRC[1])
                return true;
            else
                return false;
        } else {
            byte crc1 = bytes[bytes.length - 1];
            byte crc2 = bytes[bytes.length - 2];
            byte[] mCRC = HexString2Bytes(Integer.toHexString(CRC.calcCrc16(newBytes)));
            if (crc1 == mCRC[0] && crc2 == mCRC[1])
                return true;
            else
                return false;
        }

    }

    //    private static byte[] transPositionBytes(byte[] bytes, byte[] addBytes) {
//        byte[] newBytes = new byte[bytes.length + addBytes.length];
//        for (int i = 0; i < newBytes.length; i++) {
//            if (i < bytes.length)
//                newBytes[i] = bytes[i];
//            else
//                newBytes[i] = addBytes[newBytes.length - i];
//        }
//        return newBytes;
//    }
    public static byte[] hexOpenLockNumber(String openLockNumber) {
        byte[] lockOneGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(0, 3)));
        byte[] lockTwoGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(3, 6)));
        byte[] lockThreeGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(6, 9)));
        byte[] lockFourGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(9, 12)));
        byte[] lockFiveGroup = HexString2Bytes(decimalSystemHexadecimal(openLockNumber.substring(12, 15)));
        return null;
    }

    /**
     * 将锁号转为String
     *
     * @param openLockNumber
     * @return
     */
    public static String hexOpenLockNumber(byte[] openLockNumber) {
        byte[] bytes = subBytes(openLockNumber, 5, 9);
        bytes = new byte[]{bytes[3], bytes[2], bytes[1], bytes[0]};
        String hexString = HexUtil.formatHexString(bytes);
        Long lockNumber = Long.parseLong(hexString, 16);
//       lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[2]),16));
//        lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[1]),16));
//        lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[0]),16));
        return String.valueOf(lockNumber);
    }
//    public static byte[] hexLockNumber(String openLockNumber) {
//        byte[] bytes =Long.parseLong(openLockNumber, 16);
//        bytes = new byte[]{bytes[3], bytes[2], bytes[1], bytes[0]};
//        String hexString = HexUtil.(bytes);
//
////       lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[2]),16));
////        lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[1]),16));
////        lockNumber=lockNumber+String.valueOf(Integer.parseInt(String.valueOf(bytes[0]),16));
//        return String.valueOf(lockNumber);
//    }
    /**
     * 十进制转十六进制
     *
     * @param string
     * @return
     */
    private static String decimalSystemHexadecimal(String string) {
        Integer integer = new Integer(string);
        if (integer > 256)
            return "0" + integer.toHexString(integer);
        else
            return integer.toHexString(integer);
    }

    /**
     * 将String字符串转为byte数组
     *
     * @param timeStr
     * @return
     */
    public static byte[] hexTime(String timeStr) {
        byte[] yearBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(0, 4)));
        byte[] monthBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(4, 6)));
        byte[] dayBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(6, 8)));
        byte[] hourBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(8, 10)));
        byte[] minuteBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(10, 12)));
        byte[] secondBytes = HexString2Bytes(decimalSystemHexadecimal(timeStr.substring(12, 14)));
        return new byte[]{yearBytes[1], yearBytes[0], monthBytes[0], dayBytes[0], hourBytes[0], minuteBytes[0], secondBytes[0]};
    }

    /**
     * 将掌机序列号按高低位重新组合 转换为String
     *
     * @param bytes 固定长度为4byte
     * @return Mac地址与序列号（其实两者为一致）
     */
    public static String hexMacAddres(byte[] bytes) {
        byte[] macAddreas = new byte[]{bytes[3], bytes[2], bytes[1], bytes[0]};
        return HexUtil.formatHexString(macAddreas);

    }

    /**
     * 将一个7byte的时间数据转换为String
     *
     * @param bytes
     * @return
     */
    public static String hexTime(byte[] bytes) {
        String timeStrHex = HexUtil.formatHexString(bytes);
        long year = hexTranslateToDecimal(timeStrHex.substring(0, 4));
        long month = hexTranslateToDecimal(timeStrHex.substring(4, 6));
        long day = hexTranslateToDecimal(timeStrHex.substring(6, 8));
        long hour = hexTranslateToDecimal(timeStrHex.substring(8, 10));
        long minute = hexTranslateToDecimal(timeStrHex.substring(10, 12));
        long secon = hexTranslateToDecimal(timeStrHex.substring(12, 14));
        return "" + year + month + day + hour + minute + secon;
    }

    /**
     * 将十进制字符串转换为十六进制
     *
     * @param s
     * @return
     */
    public static long hexTranslateToDecimal(String s) {
        BigInteger bigInteger = new BigInteger(s, 16);
        return bigInteger.longValue();
    }

    public static int getUnsignedByte(byte data) {      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data & 0x0FF;
    }

    /**
     * 截取bytes的数据
     *
     * @param bytes 原数据
     * @param start 开始位置
     * @param end   结束位置
     * @return 截取后的bytes
     * 用法与subString类似
     */
    public static byte[] subBytes(byte[] bytes, int start, int end) {
        byte[] newBytes = new byte[end - start];
        for (int i = start; i < end; i++) {
            newBytes[i - start] = bytes[i];
        }
        return newBytes;
    }
}
