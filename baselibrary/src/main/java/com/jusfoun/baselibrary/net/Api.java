package com.jusfoun.baselibrary.net;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/5.
 * Email wcc@jusfoun.com
 * Description 请求网络
 */
public class Api {

    private int TIMEOUT = 10000;
    private OkHttpClient okHttpClient;

    public Retrofit retrofit;

    private static class SingletonHolder {
        private static final Api INSTANCE = new Api();
    }

    public static Api getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 注册网络连接
     * @param mContext
     * @param baseUrl
     */
    public void register(Context mContext, String baseUrl){
        File cacheFile = new File(mContext.getApplicationContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);//100Mb
        //设置证书
        CertificatePinner.Builder builder=new CertificatePinner.Builder();
//        builder.add("sbbic.com","sha1");
        okHttpClient = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HeaderWeather(mContext))
                .addInterceptor(new CacheInterceptor(mContext))
                //https证书锁定
//                .certificatePinner(builder.build())
                .cache(cache)
                .build();

        Request request=new Request.Builder()
                .get()
                .url("")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * 获取路由表
     * @param apiService
     * @param <T>
     * @return
     */
    public <T> T getService(Class<T> apiService){
        if (retrofit==null){
            throw new IllegalArgumentException("retrofit not register");
        }
        return retrofit.create(apiService);
    }

    private Api() {

    }
}
