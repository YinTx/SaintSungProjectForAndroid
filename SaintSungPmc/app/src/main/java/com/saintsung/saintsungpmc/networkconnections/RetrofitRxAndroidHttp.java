package com.saintsung.saintsungpmc.networkconnections;


import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.configure.Constant;
import com.saintsung.saintsungpmc.myinterface.BlogService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by XLzY on 2018/1/8.
 */

public class RetrofitRxAndroidHttp {
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");

    public void serviceConnect(String url, String result, Action1<ResponseBody> action1) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.addressHttps)
                .client(MyApplication.getokHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        BlogService service = retrofit.create(BlogService.class);
        RequestBody body = RequestBody.create(CONTENT_TYPE, result);
        Observable<ResponseBody> call = service.getCall(body);
        call.subscribeOn(Schedulers.newThread())//这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1, onErrorAction);
    }

    public void serviceFileConnect(String url, String filePath, String result, Callback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        BlogService service = retrofit.create(BlogService.class);
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("aFile", file.getName(), requestFile);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), result);
        Call<ResponseBody> call = service.getServiceFileCall(description, body);
        call.enqueue(callback);
    }

    //处理onError()中的内容
    Action1<Throwable> onErrorAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {


        }
    };
}
