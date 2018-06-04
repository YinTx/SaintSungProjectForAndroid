package com.saintsung.saintsungpmc.bean;

import com.saintsung.saintsungpmc.tools.MD5;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/30.
 */

public class WorkOrderDetailInfosBean {
    private String optCode="GetWorkOrderDetailInfos";
    private String workOrderNo;
    private String nums;
    private String cntNum;
    private String infoNum;
    private String sign;
    private List<WorkOrderDetailInfosItemBean> data;
    private String result;
    private String resultMessage;
    public WorkOrderDetailInfosBean(String workOrderNo, String nums, String cntNum, String infoNum) {
        this.workOrderNo = workOrderNo;
        this.nums = nums;
        this.cntNum = cntNum;
        this.infoNum = infoNum;
        this.sign= MD5.toMD5(optCode+workOrderNo+nums+cntNum+infoNum);
    }

    public List<WorkOrderDetailInfosItemBean> getData() {
        return data;
    }

    public void setData(List<WorkOrderDetailInfosItemBean> data) {
        this.data = data;
    }

    public String getOptCode() {
        return optCode;
    }

    public void setOptCode(String optCode) {
        this.optCode = optCode;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
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
}
