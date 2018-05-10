package com.saintsung.saintsungpmc.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XLzY on 2017/11/9.
 */

public class DataProcess {
    /**
     * 对数据进行MD5加密
     * 服务器ID+补足后的用户名+补足后的密码+补足后的IMEI
     *
     * @param res
     * @return
     */
    public static String createRequestPacket(String res) {
        String request = res;
        String md5Crc = MD5.toMD5(request);
        request += md5Crc;
        int len = request.length();
        String lenStr = len + "";
        //数据报文前6位为长度位,指明其后数据内容的长度.
        lenStr = ComplementZeor(lenStr, 6);
        request = lenStr + request;
        return request;
    }

    /**
     * 前补空格
     *
     * @param rst
     * @return
     */
    public static String ComplementSpace(String rst, int x) {
        String rtn = rst;
        for (int i = 0; i < x - rtn.length(); i++) {
            rst = " " + rst;
        }
        rtn = rst;
        return rtn;
    }

    /**
     * 前补零
     *
     * @param rst
     * @return
     */
    public static String ComplementZeor(String rst, int x) {
        String rtn = rst;
        for (int i = 0; i < x - rtn.length(); i++) {
            rst = "0" + rst;
        }
        rtn = rst;
        return rtn;
    }

    /**
     * 后补空格
     *
     * @param rst
     * @return
     */
    public static String ComplementSpace2(String rst, int x) {
        String rtn = rst;
        for (int i = 0; i < x - rtn.length(); i++) {
            rst = rst + " ";
        }
        rtn = rst;
        return rtn;
    }

    /**
     * 后补零
     *
     * @param rst
     * @return
     */
    public static String ComplementZeor2(String rst, int x) {
        String rtn = rst;
        for (int i = 0; i < x - rtn.length(); i++) {
            rst = rst + "0";
        }
        rtn = rst;
        return rtn;
    }

    //RTN_FLAG	返回标志	VARCHAR2 (1)	1表示成功，0表示失败
    //RTN_ERRCD	错误代号	VARCHAR2 (4)	验证错误的错误代号，字母/数字
    //RTN_UID	成功登陆的用户ID	VARCHAR2(16)	由服务器返还的用户ID，供业务操作验证使用
    //LOGIN_TIME	登录时间	VARCHAR2(14)	成功则返回登录服务器时间YYYYMMDDhhmmss,24小时格式
    //SX_TIMES	无信号自动失效时间分钟	VARCHAR2(4)	数字(默认60分钟)
    //ZQ_TIMES	周期检查时间分钟	VARCHAR2(4)	数字(默认15分钟)
    //数据长度+数据+MD5检验码
    public static boolean getLoginReturn(String res) {
        if (res.substring(10, 11).equals("0")) {
            return false;
        } else
            return true;
    }

    /**
     *
     * @param reslut 服务器返回的登录数据
     * @return 截取登录数据中的UserId
     */
    public static String getUserIdInService(String reslut){
        return reslut.substring(15,31);
    }
    public static String getErrorInService(String reslut){
        String errorStr=reslut.substring(11,15);
        switch (errorStr){
            case "E003":
                reslut="用户名与密码不匹配！";
                break;
            case "E014":
                reslut="用户不存在！";
                break;
            case "E015":
                reslut="用户已离职！";
                break;
            case "E016":
                reslut="用户已删除！";
                break;
            default:
                reslut="请联系管理员！";
                break;
        }
        return reslut;
    }
    public static String getTiem(){
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String time=sdf.format(date);
        return time;
    }
}
