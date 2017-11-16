package com.saintsung.saintsungpmc.loading;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 该Class为SharedPreferences操作类
 * Created by XLzY on 2017/11/16.
 */

public class SharedPreferencesUtil {
    private static final String spFileName = "welcomePage";
    private static final String spFileN="SharedPreferencesUser";
    public static final String FIRST_OPEN = "first_open";

    /**
     * 公共方法，储存轻量级数据
     * @param context 上下文参数
     * @param strKey 根据该参数查询sp
     * @param strDefault 如没有数据则使用默认的值
     * @return
     */
    public static String getSharedPreferences(Context context, String strKey,
                                              String strDefault) {//strDefault	boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileN, Context.MODE_PRIVATE);
        return setPreferences.getString(strKey, strDefault);
    }

    public static void putSharedPreferences(Context context, String strKey,
                                            String strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putString(strKey, strData);
        editor.commit();
    }

    public static Boolean getBoolean(Context context, String strKey,
                                     Boolean strDefault) {//strDefault	boolean: Value to return if this preference does not exist.
        SharedPreferences setPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        Boolean result = setPreferences.getBoolean(strKey, strDefault);
        return result;
    }

    public static void putBoolean(Context context, String strKey,
                                  Boolean strData) {
        SharedPreferences activityPreferences = context.getSharedPreferences(
                spFileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = activityPreferences.edit();
        editor.putBoolean(strKey, strData);
        editor.commit();
    }
}
