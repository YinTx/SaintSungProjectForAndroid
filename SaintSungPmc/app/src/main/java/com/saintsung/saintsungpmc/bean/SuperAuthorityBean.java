package com.saintsung.saintsungpmc.bean;

import com.saintsung.saintsungpmc.tools.MD5;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/23.
 */

public class SuperAuthorityBean {
    private String optCode="AllLockDetailInfos";
    private String nums;
    private String cntNum;
    private String infoNum;
    private String sign;
    private String result;
    private String resultMessage;
    private List<SuperAuthorityArrBean> data;

    public SuperAuthorityBean(String nums, String cntNum, String infoNum) {
        this.nums = nums;
        this.cntNum = cntNum;
        this.infoNum = infoNum;
        this.sign = sign;
        this.sign= MD5.toMD5(optCode+nums+cntNum+infoNum);
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getCntNum() {
        return cntNum;
    }

    public void setCntNum(String cntNum) {
        this.cntNum = cntNum;
    }

    public String getInfoNum() {
        return infoNum;
    }

    public void setInfoNum(String infoNum) {
        this.infoNum = infoNum;
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

    public List<SuperAuthorityArrBean> getData() {
        return data;
    }

    public void setData(List<SuperAuthorityArrBean> data) {
        this.data = data;
    }
}
