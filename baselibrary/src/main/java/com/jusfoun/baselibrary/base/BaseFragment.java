package com.jusfoun.baselibrary.base;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import rx.functions.Action1;

/**
 * Created by wang on 2016/11/8.
 * fragment 基类
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected RxManage rxManage;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxManage=new RxManage();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        initDatas();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayoutResId(),container,false);
        initView(view);
        initAction();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        rxManage.clear();//fragment销毁清除rxbus事件及网络请求，防止内存泄漏
    }

    public void showToast(String text){
        Toast.makeText(mContext,text, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int stringId){
        Toast.makeText(mContext,getString(stringId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 添加网络请求
     * @param observable
     * @param action1
     */
    protected <T extends BaseModel> void addNetwork(rx.Observable<T> observable, Action1<T> action1, Action1<Throwable> error){
        rxManage.add(observable, action1, error);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public abstract int getLayoutResId();
    public abstract void initDatas();
    public abstract void initView(View view);
    public abstract void initAction();


}
