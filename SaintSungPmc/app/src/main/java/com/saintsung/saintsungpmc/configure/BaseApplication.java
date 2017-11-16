package com.saintsung.saintsungpmc.configure;

import android.app.Application;

/**
 * Created by XLzY on 2017/11/9.
 */

public class BaseApplication extends Application{
    private String port;
    private String IEME;
    private String userId;
    public void setIEME(String IEME) {
        this.IEME = IEME;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIEME() {
        return IEME;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
