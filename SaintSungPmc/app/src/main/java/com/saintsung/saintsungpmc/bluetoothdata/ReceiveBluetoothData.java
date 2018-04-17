package com.saintsung.saintsungpmc.bluetoothdata;

import android.util.Log;

import com.clj.fastble.utils.HexUtil;

import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.checkPackageStartEnd;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.hexMacAddres;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.hexTime;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.subBytes;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.sendLockNumber;


/**
 * Created by EvanShu on 2018/4/11.
 */

public class ReceiveBluetoothData {
    static resultData resultDatacommand;
    private static byte mCommand;
    private static boolean flag = false;
    private static String[] parameterS00=new String[3];
    public interface resultData {
        void resultCommand(byte result);
    }

    public static void getReceiveBluetoothData(byte[] bytes) {
        Log.e("TAG", "接收到数据：" + HexUtil.formatHexString(bytes, true));
        String result = checkPackageStartEnd(bytes);
        if (result.equals("0"))
            commamdType(bytes);
        else if (result.equals("1"))
            startData(bytes);
        else if (result.equals("2"))
            endPackage(bytes);
        else if (result.equals("CRCError"))
            Log.e("TAG", "CRC校验不正确！");
        else if (result.equals("PackageError")) {
            //数据包不完整
            Log.e("TAG", "数据包不完整!");
        } else if (result.equals("Error")) {
            Log.e("TAG", "传输出错！");
        }
    }


    private static void commamdType(byte[] bytes) {
        switch (bytes[3]) {
            case 0x10:
                //开始读取S00参数
                Log.e("TAG", "开始读取小掌机参数！");
                mCommand = bytes[4];
                flag = true;
                break;
            case 0x20:
                //设置S00参数结果的返回
                Log.e("TAG", "设置掌机参数结束！");
                isSuccess(bytes[4]);
                break;
            case 0x30:
                //执行开设备操作
                Log.e("TAG", "开始执行开设备操作！");
                sendLockNumber("210056125255147", "1");
                break;
            case 0x40:
                //执行关设备操作
                Log.e("TAG", "开始执行关设备操作！");

                break;
            case 0x50:
                //下载工单编号结束
                Log.e("TAG", "下载工单编号结束！");
                if (isSuccess(bytes[4])) {
                    backResultCommand(bytes);
                } else {

                }
                break;
            case 0x60:
                //下载工单有效时间结束
                Log.e("TAG", "下载工单有效时间结束！");
                if (isSuccess(bytes[4])) {
                    backResultCommand(bytes);
                } else {

                }
                break;
            case 0x70:
                //工单下载反馈（一个大包）
                Log.e("", "下载一个包结束！");
                isSuccess(bytes[4]);
                break;
            case 0x71:
                //清除记录反馈
                Log.e("TAG", "清除开锁记录以及工单信息结束！");
                isSuccess(bytes[4]);
                break;
            case (byte) 0x80:
                //上传S00操作记录响应包
                Log.e("TAG", "上传S00操作记录响应包！");
                isSuccess(bytes[4]);
                break;
            case (byte) 0x90:
                //告诉APP，掌机进入待机模式
                Log.e("TAG", "掌机进入待机模式！");
                break;
            case (byte) 0x91:
                //掌机进入自动关机
                Log.e("TAG", "掌机进入自动关机！");
                break;
            case (byte) 0x92:
                //掌机低压模式
                Log.e("TAG", "掌机低压模式！");
                break;
            case (byte) 0x93:
                //掌机开锁状态标志
                Log.e("TAG", "掌机开锁状态标志！");
                break;
            case (byte) 0x94:
                //掌机关锁状态标志
                Log.e("TAG", "掌机关锁状态标志！");
                break;
            case (byte) 0xA0:
                String macAddres = hexMacAddres(subBytes(bytes, 5, 9));
                Log.e("TAG", "连接蓝牙成功！蓝牙MacAddres：" + macAddres.toUpperCase());
                break;
            default:
                Log.e("TAG", "未知命令！");
                break;
        }
    }

    /**
     * 结束包
     *
     * @param bytes
     */
    private static void endPackage(byte[] bytes) {
        byte command = bytes[3];
        switch (command) {
            case 0x10:
                //结束读取S00参数
                mCommand=0x00;
                flag=false;
                Log.e("TAG", "读取掌机参数结束！");
                break;
            case (byte) 0x80:
                //上传S00操作记录结束包
                break;
            default:
                break;
        }
    }

    /**
     * 判断小掌机参数是否设置成功
     *
     * @param bytes
     */
    private static boolean isSuccess(byte bytes) {
        if (bytes == 0x00) {
            Log.e("TAG", "Success");
            return true;
        } else {
            Log.e("TAG", "Fail");
            return false;
        }
    }

    public static void backResultCommand(byte[] bytes) {
        byte mCommand = bytes[3];
        if (mCommand == 0x50)
            resultDatacommand.resultCommand(mCommand);
        else if (mCommand == 0x60)
            resultDatacommand.resultCommand(mCommand);
    }

    /**
     * 这个包是开始接收以0xb1为包头没有包尾的数据包
     *
     * @param bytes
     */
    private static void startData(byte[] bytes) {
        if (flag) {
            if (mCommand == 0x10)
                readS00Parameter(bytes);
        }
    }

    /**
     * 读取S00参数
     *
     * @param bytes
     */
    public static void readS00Parameter(byte[] bytes) {
        byte packBytes = bytes[1];
        switch (packBytes) {
            case 0x01:
                byte[] timeBytes = new byte[]{bytes[3], bytes[2], bytes[4], bytes[5], bytes[6]};
                byte[] serialNumberBytes = new byte[]{bytes[10], bytes[9], bytes[8], bytes[7]};
                parameterS00[0]= hexTime(timeBytes);
//                parameterS00[1]=;
                Log.e("TAG", "Time:" + hexTime(timeBytes) + "Serial:" + HexUtil.formatHexString(serialNumberBytes));
                break;
            case 0x02:
                byte[] secretKey = subBytes(bytes, 2, 18);
                Log.e("TAG", "SecretKey:" + HexUtil.formatHexString(secretKey));
                break;
            case 0x03:
                byte[] softwareVersion = new byte[]{bytes[4], bytes[3], bytes[2]};
                byte[] hardwareVersion = new byte[]{bytes[7], bytes[6], bytes[5]};
                Log.e("TAG", "SoftwareVersion:" + HexUtil.formatHexString(softwareVersion) + "HardwareVersion:" + HexUtil.formatHexString(hardwareVersion));
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

}
