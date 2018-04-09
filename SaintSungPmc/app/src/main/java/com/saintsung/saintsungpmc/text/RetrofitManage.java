package com.saintsung.saintsungpmc.text;

/**
 * Created by XLzY on 2018/1/8.
 */

public class RetrofitManage {
    public static RetrofitManage getInstance() {
        return RetrofitManager.retrofitManage;
    }

    private static class RetrofitManager {
        private static final RetrofitManage retrofitManage = new RetrofitManage();
    }
}
