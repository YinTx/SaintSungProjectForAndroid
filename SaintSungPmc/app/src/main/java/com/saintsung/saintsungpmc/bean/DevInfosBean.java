package com.saintsung.saintsungpmc.bean;

/**
 * Created by EvanShu on 2018/5/30.
 */

public class DevInfosBean {
    private String lockNo;
    private String assetNo;
    private String optPwd;
    private String typeId;
    private String typeName;

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
}
