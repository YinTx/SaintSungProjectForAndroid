package com.saintsung.saintsungpmc.networkconnections;

import android.util.Log;

import com.google.gson.Gson;
import com.saintsung.saintsungpmc.MyApplication;
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

    public void loginService() {
//        retrofitRxAndroidHttp.serviceConnect();
    }

    public void getWorkOrder() {
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps, getWorkOrderStr(), action1);
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
        String md5Str=workOrderBean.getOptCode() + workOrderBean.getOptUserNumber()+workOrderBean.getKeyNumber()  + workOrderBean.getStartTime() + workOrderBean.getEndTime();
        sign = MD5.toMD5(md5Str);
        workOrderBean.setSign(sign);
        return gson.toJson(workOrderBean);
    }

    private Action1<ResponseBody> action1 = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {

                dataProcessing(responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void dataProcessing(String string) {
        Gson gson=new Gson();
        Log.e("TAG", string);
        if (!string.equals("")){
            WorkOrderBean workOrderBean=gson.fromJson(string,WorkOrderBean.class);
            if(workOrderBean.getResult().equals("0000")){
                MyApplication.setWorkOrderBean(workOrderBean);
            }
        }
    }
}
