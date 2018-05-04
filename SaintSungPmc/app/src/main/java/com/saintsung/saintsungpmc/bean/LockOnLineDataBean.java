package com.saintsung.saintsungpmc.bean;

/**
 * Created by EvanShu on 2018/5/3.
 */

public class LockOnLineDataBean {
    private String optUserNumber;
    private String lockNo;
    private String assetNo="";
    private String keyNumber="";
    private String pointX="";
    private String pointY="";
    private String actType;
    private String optPwd;
    private String typeId;
    private String typeName;
    private String permissionType;

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getOptUserNumber() {
        return optUserNumber;
    }

    public void setOptUserNumber(String optUserNumber) {
        this.optUserNumber = optUserNumber;
    }

    public String getLockNo() {
        return lockNo;
    }

    public void setLockNo(String lockNo) {
        this.lockNo = lockNo;
    }

    public String getAssetNo() {
        return assetNo;
    }

    public void setAssetNo(String assetNo) {
        this.assetNo = assetNo;
    }

    public String getKeyNumber() {
        return keyNumber;
    }

    public void setKeyNumber(String keyNumber) {
        this.keyNumber = keyNumber;
    }

    public String getPointX() {
        return pointX;
    }

    public void setPointX(String pointX) {
        this.pointX = pointX;
    }

    public String getPointY() {
        return pointY;
    }

    public void setPointY(String pointY) {
        this.pointY = pointY;
    }

    public String getOptPwd() {
        return optPwd;
    }

    public void setOptPwd(String optPwd) {
        this.optPwd = optPwd;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
}
