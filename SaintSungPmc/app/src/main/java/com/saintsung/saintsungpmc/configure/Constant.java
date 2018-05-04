package com.saintsung.saintsungpmc.configure;

import java.util.UUID;

/**
 * Created by XLzY on 2017/11/9.
 */

public class Constant {
    public static String loginServiceLable = "L001";
    public static String AddressIp = "210.22.164.146";
    //新版小掌机UUID
//    public static final UUID uuidWriteService = UUID.fromString("0000ffe5-0000-1000-8000-00805f9b34fb");
//    public static final UUID uuidWrite =   UUID.fromString("0000ffe9-0000-1000-8000-00805f9b34fb");
//    public static final UUID uuidNotifyService = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
//    public static final UUID uuidNotify =  UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");

    //旧版小掌机UUID
    public static final UUID uuidWriteService = UUID.fromString("00001910-0000-1000-8000-00805f9b34fb");
    public static final UUID uuidNotifyService = UUID.fromString("00001910-0000-1000-8000-00805f9b34fb");
    public static final UUID uuidNotify = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");
    public static final UUID uuidWrite= UUID.fromString("0000FFF2-0000-1000-8000-00805f9b34fb");
    //JDY-08 UUID
//    public static String uuidService="0000ffe0-0000-1000-8000-00805f9b34fb";
//    public static String uuidWrite="0000ffe1-0000-1000-8000-00805f9b34fb";
//    public static String uuidNotify="0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String LoginService = "http://192.169.3.39/modifysaintsung/api/";
    public static final String serviceTail="/api/";
    public static final String addressHttp="http://210.22.164.146:";
    public static final String addressHttps="http://210.22.164.146:105/index.php/";
}
