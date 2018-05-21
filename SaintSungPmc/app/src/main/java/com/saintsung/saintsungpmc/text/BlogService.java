package com.saintsung.saintsungpmc.text;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by XLzY on 2018/1/8.
 */

public interface BlogService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("httpInterface.php")
    Observable<ResponseBody> getLoginService(@Body RequestBody route);

    @GET("geocoding?a=上海市&aa=松江区&aaa=车墩镇")
    Call<AliAddrsBean> getIndexContent();
    @GET("{parameters}?a=苏州市")
    Call<AliAddrsBean> getIndexContentOne(@Path("parameters") String parameters);
    //看到那么多参数请求，肯定有简单的方法,单个参数添加
    @POST("geocoding?")
    Call<AliAddrsBean> getIndexContentTow(
            @Query("a") String key1,
            @Query("aa") String key2,
            @Query("aaa") String key3
    );
    //看到那么多参数请求，肯定有简单的方法,多个参数添加使用map
    @GET("geocoding?")
    Call<AliAddrsBean> getIndexContentThree(
            @QueryMap Map<String, Object> options
    );
    //可以通过@Body注解指定一个对象作为Http请求的请求体
    //类似于一二三级城市的参数都放在的请求体indexRequestBean里面
    @POST("geocoding?")
    Call<AliAddrsBean> getIndexContentFour(
            @Body IndexRequestBean indexRequestBean);
    //我们适配Rxjava的时候，只需要将返回结果的call变成Rxjava的被订阅者Observable即可
    @GET("geocoding?")
    Observable<AliAddrsBean> getIndexContentEleven(
           @Query("a") String city);
    @POST("geocoding?")
    Observable<AliAddrsBean> getIndexContentTwelve(
            @Body IndexRequestBean indexRequestBean);
}
