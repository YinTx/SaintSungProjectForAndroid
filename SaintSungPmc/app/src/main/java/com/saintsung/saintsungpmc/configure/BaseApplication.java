package com.saintsung.saintsungpmc.configure;

import android.app.Application;
import android.net.Network;
import android.util.Log;

import com.saintsung.saintsungpmc.tools.NetworkUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by XLzY on 2017/11/9.
 */

public class BaseApplication extends Application {
    public static OkHttpClient okHttpClient;
    private String port;
    private String IEME;
    private String userId;

    public void setIEME(String IEME) {
        this.IEME = IEME;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIEME() {
        return IEME;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        okHttpClient = new OkHttpClient();
        okHttpClient = okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new MyLogInterceptor())
                .build();
    }

    //拦截器,可以修改header,可以通过拦截器打印日志
    public class MyLogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request.header("User-Agent");
//            HttpUrl url = request.url();
//            String httpUrl = url.url().toString();
//            Log.e("TAG", "=====" + httpUrl);
//            Response response = chain.proceed(request);
            long t1 = System.nanoTime();
            Log.e(TAG,String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Log.e(TAG, String.format("Received response for %s in %.1fms%n%sconnection=%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(), chain.connection()));
            return response;
//            int code = response.code();
//            Log.e("TAG", "code() == " + code);
//            if(!NetworkUtils.isNetworkAvailable(getApplicationContext())){
//                request=request.newBuilder()
//                        .cacheControl(CacheControl.FORCE_CACHE)
//                        .build();
//                Log.e("CacheInterceptor","no network");
//            }
//            Response originalResponse=chain.proceed(request);
//            if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
//                return originalResponse.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control","public, max-age="+0)
//                        .build();
//
//            }else {
//                int maxTime=4*24*60*60;
//                return originalResponse.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control","public, only-if-cached,maxstale="+maxTime)
//                        .build();
//            }

        }
    }

    public static OkHttpClient getokHttpClient() {
        return okHttpClient;
    }
}
