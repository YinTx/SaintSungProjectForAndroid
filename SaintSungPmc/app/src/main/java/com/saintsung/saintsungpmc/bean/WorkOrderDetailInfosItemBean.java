package com.saintsung.saintsungpmc.bean;

import java.util.List;

/**
 * Created by EvanShu on 2018/5/30.
 */

public class WorkOrderDetailInfosItemBean {
    private UserInfosBean userInfos;
    private List<DevInfosBean> devInfos;

    public UserInfosBean getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfosBean userInfos) {
        this.userInfos = userInfos;
    }

    public List<DevInfosBean> getDevInfos() {
        return devInfos;
    }

    public void setDevInfos(List<DevInfosBean> devInfos) {
        this.devInfos = devInfos;
    }
}
