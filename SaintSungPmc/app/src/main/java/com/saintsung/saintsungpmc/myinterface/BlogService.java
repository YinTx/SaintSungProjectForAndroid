package com.saintsung.saintsungpmc.myinterface;


import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by XLzY on 2017/12/29.
 */

public interface BlogService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("test.php")
    Call<ResponseBody> getCall(@Body RequestBody route);
}
