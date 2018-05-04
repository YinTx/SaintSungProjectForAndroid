package com.saintsung.saintsungpmc.bean;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/3.
 */

public class LockOnLineBean {
    private String optCode;
    private String sign;
    private String result;
    private String resultMessage;
    private LockOnLineDataBean data;

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
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

    public LockOnLineDataBean getData() {
        return data;
    }

    public void setData(LockOnLineDataBean data) {
        this.data = data;
    }
}
