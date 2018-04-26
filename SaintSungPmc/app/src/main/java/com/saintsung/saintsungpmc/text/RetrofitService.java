package com.saintsung.saintsungpmc.text;


import android.util.Log;


import com.saintsung.saintsungpmc.MyApplication;
import com.saintsung.saintsungpmc.configure.BaseApplication;

import java.io.IOException;


import okhttp3.MediaType;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

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

public class RetrofitService {
    private static final MediaType CONTENT_TYPE = MediaType.parse("application/json; charset=utf-8");
    public void sendRequest(String loginService, String url, Action1<ResponseBody> action1) {
        //每一个Call实例可以同步(call.excute())或者异步(call.enquene(CallBack<?> callBack))的被执行，
        //每一个实例仅仅能够被使用一次，但是可以通过clone()函数创建一个新的可用的实例。
        //默认情况下，Retrofit只能够反序列化Http体为OkHttp的ResponseBody类型
        //并且只能够接受ResponseBody类型的参数作为@body
        Log.e("TAG","==============="+loginService);
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(url)
                .client(MyApplication.getokHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
                .addConverterFactory(GsonConverterFactory.create()) // 使用Gson作为数据转换器
                .build();
        BlogService service = retrofit.create(BlogService.class);
        RequestBody body=RequestBody.create(CONTENT_TYPE,loginService);
        Observable<ResponseBody> requestInde = service.getLoginService(body);
        requestInde.subscribeOn(Schedulers.newThread())//这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);
//        Observable.zip(
//                service.getIndexContentEleven("苏州市"),//第一个Observable对象
//                service.getIndexContentEleven("上海市"),//第二个Observable对象
//                new Func2<AliAddrsBean, AliAddrsBean, String>() {//function1中传入的是《1，第一个Observable对象；2，第二个Observable对象；3，返回类型》
//                    @Override
//                    public String call(AliAddrsBean o, AliAddrsBean o2) {
//                        return o.getLat() + ":" + o2.getLat();
//                    }
//                })
//                .subscribeOn(Schedulers.newThread())//这里需要注意的是，网络请求在非ui线程。如果返回结果是依赖于Rxjava的，则需要变换线程
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.i("test", "retrofit error " + e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(String s) {//传入的是function1中返回的对象
//                        Log.i("test", "retrofit结果1" + s);
//                    }
//                });
    }
}
