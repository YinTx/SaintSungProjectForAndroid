package com.saintsung.saintsungpmc.networkconnections;

import android.util.Log;

import com.google.gson.Gson;
import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.bean.DevInfosBean;
import com.saintsung.saintsungpmc.bean.LockOnLineBean;
import com.saintsung.saintsungpmc.bean.LockOnLineDataBean;
import com.saintsung.saintsungpmc.bean.SuperAuthorityBean;
import com.saintsung.saintsungpmc.bean.UpLoadDataBean;
import com.saintsung.saintsungpmc.bean.UpLoadLockBena;
import com.saintsung.saintsungpmc.bean.WorkOrderBean;
import com.saintsung.saintsungpmc.bean.WorkOrderDetailInfosBean;
import com.saintsung.saintsungpmc.bean.WorkOrderDetailInfosItemBean;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.tools.MD5;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import rx.functions.Action1;

import static com.saintsung.saintsungpmc.tools.DataProcess.addStringByte;

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

    public SuperAuthorityBean getSuper() {
        SuperAuthorityBean superAuthorityBean = new SuperAuthorityBean("0", "1", "1000");
        return superAuthorityBean;
    }

    public WorkOrderDetailInfosBean getWorkOrderInfo(String workOrderNo) {
        WorkOrderDetailInfosBean workOrderDetailInfosBean = new WorkOrderDetailInfosBean(workOrderNo, "0", "1", "1000");
        return workOrderDetailInfosBean;
    }

    public interface resultServiceData {
        void resultServiceData(String openLockNumber, String type, String message);
    }

    public void loginService() {
//        retrofitRxAndroidHttp.serviceConnect();
    }

    public void getSuperAuthority() {
        retrofitRxAndroidHttp.serviceConnectNew(Constant.addressHttps, getSuper(), SuperAuthorityAct, onErrorAction);
    }

    public void getWorkOrder() {
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps, getWorkOrderStr(), workOrderAct, onErrorAction);
    }

    public void getWorkOrderInfos(String workOrderNo) {
        retrofitRxAndroidHttp.serviceConnectNew(Constant.addressHttps, getWorkOrderInfo(workOrderNo), WorkOrderInfos, onErrorAction);
    }

    public void getLockOnLine(String number, String macAddress) {
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps, getlockOnLine(number, macAddress), lockOnLineAct, onErrorAction);
    }

    public void uploadService(String string, String keyNumber) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String times = sdf.format(date);
        retrofitRxAndroidHttp.serviceConnect(Constant.addressHttps, getUploadRecord(keyNumber, string.substring(0, string.length() - 1), "5", times, string.substring(string.length() - 1, string.length())), uploadRecordAct, onErrorAction);
    }

    private String getWorkOrderStr() {
        Gson gson = new Gson();
        String sign = "";
        WorkOrderBean workOrderBean = new WorkOrderBean();
        workOrderBean.setOptCode("GetWorkOrderInfos");
        workOrderBean.setOptUserNumber(MyApplication.getUserId());
        workOrderBean.setEndTime("9999-00-00");
        workOrderBean.setStartTime("0000-00-00");
        workOrderBean.setKeyNumber("");
        String md5Str = workOrderBean.getOptCode() + workOrderBean.getOptUserNumber() + workOrderBean.getKeyNumber() + workOrderBean.getStartTime() + workOrderBean.getEndTime();
        sign = MD5.toMD5(md5Str);
        workOrderBean.setSign(sign);
        return gson.toJson(workOrderBean);
    }

    private Action1<ResponseBody> SuperAuthorityAct = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {
                Log.e("TAG", "" + responseBody.string());
//                workOrderActProcessing(responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private Action1<ResponseBody> WorkOrderInfos = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {
                sendBluetoothWorkOrder(responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
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
        Log.e("TAG", "" + string);
        Gson gson = new Gson();
        if (!string.equals("")) {
            LockOnLineBean lockOnLineBean = gson.fromJson(string, LockOnLineBean.class);
            if (lockOnLineBean.getResult().equals("0000")) {
                resultServiceData.resultServiceData(lockOnLineBean.getData().getOptPwd(), lockOnLineBean.getData().getTypeId(), lockOnLineBean.getResultMessage());
            } else {
                resultServiceData.resultServiceData(lockOnLineBean.getData().getOptPwd(), lockOnLineBean.getData().getTypeId(), lockOnLineBean.getResultMessage());
            }
        }
    }

    private void sendBluetoothWorkOrder(String string) {
        Gson gson = new Gson();
        Log.e("TAG", string);
        if (!string.equals("")) {
            WorkOrderDetailInfosBean workOrderDetailInfosBean = gson.fromJson(string, WorkOrderDetailInfosBean.class);
            if (workOrderDetailInfosBean.getResult().equals("0000")) {
                String[] strings = new String[]{workOrderDetailInfosBean.getWorkOrderNo(), MyApplication.getWorkOrderBean().getData().get(0).getStartTime(), MyApplication.getWorkOrderBean().getData().get(0).getEndTime()};
                for (WorkOrderDetailInfosItemBean workOrderDetailInfosItemBean : workOrderDetailInfosBean.getData()) {
                    for (DevInfosBean devInfosBean : workOrderDetailInfosItemBean.getDevInfos()) {
                        String[] str = new String[]{devInfosBean.getLockNo() + devInfosBean.getOptPwd() + devInfosBean.getTypeId()};
                        strings = addStringByte(strings, str);
                    }
                }
                MyApplication.setWorkOrder(strings);
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
                getWorkOrderInfos(workOrderBean.getData().get(1).getWorkOrderNo());
            }
        }
    }

    private String getlockOnLine(String number, String macAddress) {
        Gson gson = new Gson();
        LockOnLineBean lockOnLineBean = new LockOnLineBean();
        LockOnLineDataBean lockOnLineDataBean = new LockOnLineDataBean();
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

    private String getUploadRecord(String keyNumber, String lockNumber, String type, String time, String resultId) {
        Gson gson = new Gson();
        UpLoadLockBena upLoadLockBena = new UpLoadLockBena();
        UpLoadDataBean upLoadDataBean = new UpLoadDataBean();
        List<UpLoadDataBean> upLoadDataBeanList = new ArrayList<>();
        upLoadLockBena.setOptCode("LockLogUpload");

        upLoadDataBean.setOptUserNumber(MyApplication.getUserId());
        upLoadDataBean.setKeyNumber(keyNumber);
        upLoadDataBean.setLockNumber(lockNumber);
        upLoadDataBean.setOptType(type);
        upLoadDataBean.setDateTime(time);
        upLoadDataBean.setResultId(resultId);
        upLoadDataBeanList.add(upLoadDataBean);
        upLoadLockBena.setData(upLoadDataBeanList);
        String sign = MD5.toMD5(upLoadLockBena.getOptCode() + gson.toJson(upLoadLockBena.getData()));
        upLoadLockBena.setSign(sign);
        return gson.toJson(upLoadLockBena);
    }

    private Action1<ResponseBody> uploadRecordAct = new Action1<ResponseBody>() {
        @Override
        public void call(ResponseBody responseBody) {
            try {
                Log.e("TAG", responseBody.string());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    //处理onError()中的内容
    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {

        }
    };
}
