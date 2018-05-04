package com.saintsung.saintsungpmc.networkconnections;

import android.util.Log;

import com.google.gson.Gson;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.bean.LockOnLineBean;
import com.saintsung.saintsungpmc.bean.LockOnLineDataBean;
import com.saintsung.saintsungpmc.bean.WorkOrderBean;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.tools.MD5;

import okhttp3.ResponseBody;
import rx.functions.Action1;

/**
 * Created by EvanShu on 2018/4/27.
 */

public class ConntentService {
    RetrofitRxAndroidHttp retrofitRxAndroidHttp = new RetrofitRxAndroidHttp();
    private resultServiceData resultServiceData;

    public ConntentService.resultServiceData getResultServiceData() {
        return resultServiceData;
    }

    public void setResultServiceData(ConntentService.resultServiceData resultServiceData) {
        this.resultServiceData = resultServiceData;
    }

    public interface resultServiceData{
        void resultServiceData(String openLockNumber,String type);
    }
    public void loginService() {
//        retrofitRxAndroidHttp.serviceConnect();
    }

    public void getWorkOrder() {
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps, getWorkOrderStr(), workOrderAct);
    }
    public void getLockOnLine(String number,String macAddress){
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps,getlockOnLine(number,macAddress),lockOnLineAct);
    }
    private String getWorkOrderStr() {
        Gson gson = new Gson();
        String sign = "";
        WorkOrderBean workOrderBean = new WorkOrderBean();
        workOrderBean.setOptCode("GetWorkOrderInfos");
        workOrderBean.setOptUserNumber(MyApplication.getUserId());
        workOrderBean.setEndTime("0000-00-00");
        workOrderBean.setStartTime("9999-00-00");
        workOrderBean.setKeyNumber("");
        String md5Str = workOrderBean.getOptCode() + workOrderBean.getOptUserNumber() + workOrderBean.getKeyNumber() + workOrderBean.getStartTime() + workOrderBean.getEndTime();
        sign = MD5.toMD5(md5Str);
        workOrderBean.setSign(sign);
        return gson.toJson(workOrderBean);
    }

    private Action1<ResponseBody> workOrderAct = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {

                workOrderActProcessing(responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Action1<ResponseBody> lockOnLineAct = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {

                lockOnLineActProcessing(responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void lockOnLineActProcessing(String string) {
        Log.e("TAG",""+string);
        Gson gson = new Gson();
        if (!string.equals("")) {
            LockOnLineBean lockOnLineBean = gson.fromJson(string, LockOnLineBean.class);
            if (lockOnLineBean.getResult().equals("0000")) {
                resultServiceData.resultServiceData(lockOnLineBean.getData().getOptPwd(),lockOnLineBean.getData().getTypeId());
            }
        }
    }

    private void workOrderActProcessing(String string) {
        Gson gson = new Gson();
        Log.e("TAG", string);
        if (!string.equals("")) {
            WorkOrderBean workOrderBean = gson.fromJson(string, WorkOrderBean.class);
            if (workOrderBean.getResult().equals("0000")) {
                MyApplication.setWorkOrderBean(workOrderBean);
            }
        }
    }
    private String getlockOnLine(String number,String macAddress) {
        Gson gson = new Gson();
        LockOnLineBean lockOnLineBean = new LockOnLineBean();
        LockOnLineDataBean lockOnLineDataBean=new LockOnLineDataBean();
        lockOnLineDataBean.setLockNo(number);
        lockOnLineDataBean.setKeyNumber(macAddress);
        lockOnLineBean.setOptCode("WorkOrderOnline");
        lockOnLineDataBean.setActType("1");
        lockOnLineDataBean.setOptUserNumber(MyApplication.getUserId());
        lockOnLineBean.setData(lockOnLineDataBean);
        String sss = lockOnLineBean.getOptCode() + gson.toJson(lockOnLineBean.getData());
        lockOnLineBean.setSign(MD5.toMD5(sss));
        return gson.toJson(lockOnLineBean);
    }
}
