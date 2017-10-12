package com.wang.simple;

import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.net.Api;

/**
 * Created by wangcc on 2017/10/12.
 * describe
 */

public class AppAplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Api.getInstance().register(this,"http://www.douban.com");
    }
}
