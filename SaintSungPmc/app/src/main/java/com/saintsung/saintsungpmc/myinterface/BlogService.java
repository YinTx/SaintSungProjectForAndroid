package com.saintsung.saintsungpmc.myinterface;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;


/**
 * Created by XLzY on 2017/12/29.
 */

public interface BlogService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("httpInterface.php")
    Observable<ResponseBody> getCall(@Body RequestBody route);

    @Multipart
    @POST("httpInterface.php")
    Call<ResponseBody> getServiceFileCall(@Part("description") RequestBody route, @Part("") MultipartBody.Part file);
}
