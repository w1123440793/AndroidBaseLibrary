package com.wang.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jusfoun.baselibrary.base.BaseActivity;

/**
 * Created by wangcc on 2017/10/12.
 * describe
 */

public abstract class BaseAppActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDatas();
        initView();
        initAction();
    }
}
