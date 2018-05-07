package com.saintsung.saintsungpmc.bean;

import java.util.List;

/**
 * Created by 14215 on 2018/5/6.
 */

public class UpLoadLockBena {
    private String optCode;
    private List<UpLoadDataBean> data;
    private String sign;
    private String result;
    private String resultMessage;

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public List<UpLoadDataBean> getData() {
        return data;
    }

    public void setData(List<UpLoadDataBean> data) {
        this.data = data;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }
}
