package com.wang.simple;

import java.util.HashMap;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by wangcc on 2017/10/12.
 * describe
 */

public interface ApiService {
    @GET("http://api.douban.com/v2/movie/top250")
    Observable<MoveModel> getMovie(@QueryMap HashMap<String, String> params);
}
