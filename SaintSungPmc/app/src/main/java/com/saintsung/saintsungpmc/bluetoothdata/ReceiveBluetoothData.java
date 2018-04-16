package com.saintsung.saintsungpmc.bluetoothdata;

import android.util.Log;

import com.clj.fastble.utils.HexUtil;

import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.checkPackageStartEnd;
import static com.saintsung.saintsungpmc.bluetoothdata.BluetoothDataProcess.hexTime;
import static com.saintsung.saintsungpmc.bluetoothdata.SendBluetoothData.sendLockNumber;


/**
 * Created by EvanShu on 2018/4/11.
 */

public class ReceiveBluetoothData {
    public static boolean flag = false;
    public static byte mCommand;

    public static void getReceiveBluetoothData(byte[] bytes) {
        Log.e("TAG", "接收到数据：" + HexUtil.formatHexString(bytes));
        String result = checkPackageStartEnd(bytes);
        if (result.equals("0"))
            startPackage(bytes);
        else if (result.equals("1"))
            startData(bytes);
        else if (result.equals("2"))
            endPackage(bytes);
        else if (result.equals("CRCError"))
            Log.e("TAG", "CRC校验不正确！");
        else if (result.equals("PackageError")) {
            //数据包不完整
            Log.e("TAG", "数据包不完整!");
        }else if(result.equals("Error")){
            Log.e("TAG","传输出错！");
        }
    }


    private static void startPackage(byte[] bytes) {
        byte command=bytes[3];
        switch (command) {
            case 0x10:
                //开始读取S00参数
                Log.e("TAG", "开始读取小掌机参数！");
                flag = true;
                mCommand = command;
                break;
            case 0x20:
                //设置S00参数结果的返回
                Log.e("TAG", "设置掌机参数结束！");
                isSetParameter(bytes);
                break;
            case 0x30:
                //执行开设备操作
                Log.e("TAG", "开始执行开设备操作！");
                sendLockNumber("210056125255147","1");
                break;
            case 0x40:
                //执行关设备操作
                Log.e("TAG", "开始执行关设备操作！");

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
     * @param bytes
     */
    private static void endPackage(byte[] bytes) {
        byte command=bytes[3];
        switch (command) {
            case 0x10:
                //结束读取S00参数
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
     * @param bytes
     */
    private static void isSetParameter(byte[] bytes) {
        if(bytes[4]==0x00){
            Log.e("TAG","Success");
        }else {
            Log.e("TAG","Fail");
        }
    }

    /**
     * 这个包是开始接收以0xb1为包头没有包尾的数据包
     * @param bytes
     */
    private static void startData(byte[] bytes) {
        if (flag) {
            if (mCommand == 0x10) {
                readS00Parameter(bytes);
            }
//            else if (mCommand==0x20){
//
//            }
        }
    }

    /**
     * 读取S00参数
     * @param bytes
     */
    public static void readS00Parameter(byte[] bytes){
        byte packBytes = bytes[1];
        switch (packBytes) {
            case 0x01:
                byte[] timeBytes = new byte[]{bytes[3], bytes[2], bytes[4], bytes[5], bytes[6]};
                byte[] serialNumberBytes = new byte[]{bytes[10], bytes[9], bytes[8], bytes[7]};
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

    private static byte[] subBytes(byte[] bytes, int start, int end) {
        byte[] newBytes = new byte[end - start];
        for (int i = start; i < end; i++) {
            newBytes[i - start] = bytes[i];
        }
        return newBytes;
    }


}
