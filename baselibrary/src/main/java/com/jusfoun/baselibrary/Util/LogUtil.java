package com.jusfoun.baselibrary.Util;

import android.util.Log;

import com.jusfoun.baselibrary.BuildConfig;


/**
 * Author  wangchenchen
 * CreateDate 2016/7/6.
 * Email wcc@jusfoun.com
 * Description log工具，debug打印日志 release不打印
 */
public class LogUtil {

    public static void d(String tag, String msg){
        if (BuildConfig.DEBUG)
            Log.d(tag,msg);
    }

    public static void w(String tag, String msg){
        if (BuildConfig.DEBUG)
            Log.w(tag,msg);
    }

    public static void e(String tag, String msg){
        if (BuildConfig.DEBUG)
            Log.e(tag,msg);
    }

    public static void i(String tag, String msg){
        if (BuildConfig.DEBUG)
            Log.i(tag,msg);
    }

    public static void e(String tag,Exception e){
        if (e==null)
            return;
        if (BuildConfig.DEBUG)
            Log.d(tag,e.toString());
    }
}
