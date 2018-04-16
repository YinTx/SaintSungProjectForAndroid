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
     * @param bytes
     * @return 返回一个类型或者错误类型
     */
    public static String checkPackageStartEnd(byte[] bytes) {
        int startPack = getUnsignedByte(bytes[0]);
        int endPack = getUnsignedByte(bytes[19]);
        if (startPack== getUnsignedByte((byte) 0xB0)&& endPack==getUnsignedByte((byte) 0xF0)) {
            if (checkCRC(bytes, 4))
                return "0";
            else
                return "CRCError";
        } else if (startPack==getUnsignedByte((byte) 0xB1)) {
            if (checkCRC(bytes, 3))
                return "1";
            else
                return "CRCError";
        } else if (startPack==getUnsignedByte((byte) 0xB2) && endPack==getUnsignedByte((byte) 0xF2)) {
            if (checkCRC(bytes, 4))
                return "2";
            else
                return "CRCError";
        } else if(startPack==getUnsignedByte((byte) 0xc0) && endPack==getUnsignedByte((byte) 0xF0)){
            return "Error";
        }
        return "PackageError";
    }

    /**
     * 检查CRC是否正确
     * @param bytes
     * @param sum 该变量是一个判断bytes数组为有包头包尾还是为只有包头
     *            如果只有包头传3如果有头有尾则传4
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

    /**
     * 将一个7byte的数据转换为String
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

    public static long hexTranslateToDecimal(String s) {
        BigInteger bigInteger = new BigInteger(s, 16);
        return bigInteger.longValue();
    }

    public static int getUnsignedByte(byte data) {      //将data字节型数据转换为0~255 (0xFF 即BYTE)。
        return data & 0x0FF;
    }
}
