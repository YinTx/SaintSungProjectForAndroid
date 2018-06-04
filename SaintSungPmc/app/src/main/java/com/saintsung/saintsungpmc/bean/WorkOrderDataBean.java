package com.saintsung.saintsungpmc.bean;

import java.util.List;

/**
 * Created by XLzY on 2018/1/18.
 */

public class

WorkOrderDataBean {
    private String workOrderId;
    private String workOrderNo;//工单编号
    private String workType; //类型
    private String workTypeName;
    private String workNote; //内容
    private String workNoteName;
    private String workState;//状态
    private String workStateName;
    private String startTime;
    private String endTime;

    public String getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(String workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public String getWorkNoteName() {
        return workNoteName;
    }

    public void setWorkNoteName(String workNoteName) {
        this.workNoteName = workNoteName;
    }

    public String getWorkStateName() {
        return workStateName;
    }

    public void setWorkStateName(String workStateName) {
        this.workStateName = workStateName;
    }

    public String getWorkOrderNo() {
        return workOrderNo;
    }

    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getWorkNote() {
        return workNote;
    }

    public void setWorkNote(String workNote) {
        this.workNote = workNote;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getWorkState() {
        return workState;
    }

    public void setWorkState(String workState) {
        this.workState = workState;
    }


    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
