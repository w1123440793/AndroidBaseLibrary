package com.wang.simple;

import android.widget.TextView;

import com.google.gson.Gson;
import com.jusfoun.baselibrary.base.BaseActivity;
import com.jusfoun.baselibrary.net.Api;

import java.util.HashMap;

import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    protected TextView net;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void initView() {
        net = (TextView) findViewById(R.id.net);

    }

    @Override
    public void initAction() {
        addNetwork(Api.getInstance().getService(ApiService.class)
                        .getMovie(new HashMap<String, String>())
                , new Action1<MoveModel>() {
                    @Override
                    public void call(MoveModel moveModel) {
                        net.setText(new Gson().toJson(moveModel));
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        net.setText(new Gson().toJson(throwable));
                    }
                });
    }
}
