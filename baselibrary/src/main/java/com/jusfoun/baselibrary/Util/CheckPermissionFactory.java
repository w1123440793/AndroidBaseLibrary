package com.jusfoun.baselibrary.Util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Author  wangchenchen
 * CreateDate 2017-3-9.
 * Email wcc@jusfoun.com
 * Description 检查权限
 */
public class CheckPermissionFactory {

    public void check(Context context,String permission){
        final int res = context.checkCallingOrSelfPermission(permission);
        final boolean hasPermission = res == PackageManager.PERMISSION_GRANTED;
        if (hasPermission) {
            //TODO：以申请权限
        } else {
            //TODO:未申请权限
        }
    }
}
