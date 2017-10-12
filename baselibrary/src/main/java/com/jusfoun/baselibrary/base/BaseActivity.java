package com.jusfoun.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jusfoun.baselibrary.BaseApplication;
import com.jusfoun.baselibrary.R;
import com.jusfoun.baselibrary.Util.KeyBoardUtil;
import com.jusfoun.baselibrary.Util.PhoneUtil;
import com.jusfoun.baselibrary.view.SwipeBackLayout;

import java.lang.ref.WeakReference;

import rx.functions.Action1;

/**
 * Created by wang on 2016/11/8.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG="";
    private SwipeBackLayout swipeBackLayout;
    private LinearLayout linearLayout;
    private ImageView isShadow;
    public Context mContext;
    protected RxManage rxManage;
    private BaseApplication mApplication;
    /**
     * 当前Activity的弱引用，防止内存泄露
     */
    private WeakReference<Activity> mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setStatusBarLight(true);
        TAG=getClass().getSimpleName();
        mContext=this;
        setContentView(getLayoutResId());
        initBase();
        rxManage=new RxManage();

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (!isNeedSwipe()){
            super.setContentView(layoutResID);
        }else {
            setContentView(getSwipeContainer());
            View view= LayoutInflater.from(this).inflate(layoutResID,null);
            swipeBackLayout.addView(view);
        }
    }

    /**
     * 是否需要侧滑返回
     * @return
     */
    public boolean isNeedSwipe(){
        return false;
    };

    private void initBase() {
        // 将当前Activity压入栈
        mApplication = BaseApplication.getBaseApplication();
        mActivity = new WeakReference<Activity>(this);
        mApplication.pushTask(mActivity);
    }

    private View getSwipeContainer(){
        RelativeLayout container=new RelativeLayout(this);
        swipeBackLayout=new SwipeBackLayout(this);
        swipeBackLayout.setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        isShadow=new ImageView(this);
        isShadow.setBackgroundColor(getResources().getColor(R.color.theme_black_7f));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(isShadow,params);
        container.addView(swipeBackLayout);
        swipeBackLayout.setOnSwipeBackListener(new SwipeBackLayout.SwipeBackListener() {
            @Override
            public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
                isShadow.setAlpha(1-fractionScreen);
            }
        });
        return container;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxManage.clear();//fragment销毁清除rxbus事件及网络请求，防止内存泄漏
        KeyBoardUtil.hideSoftKeyboard(this);
        mApplication.removeTask(mActivity);
    }

    public void showToast(String text){
        if (TextUtils.isEmpty(text))
            return;
        Toast.makeText(mContext,text, Toast.LENGTH_SHORT).show();
    }
    public void showToast(int stringId){
        Toast.makeText(mContext,getString(stringId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转界面
     * @param bundle 传递数据，为NULL不传递
     * @param cls 跳转的界面
     */
    protected void goActivity(Bundle bundle, Class<?> cls){
        Intent intent=new Intent();
        intent.setClass(mContext,cls);
        if (bundle!=null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转界面并在销毁时返回结果
     * @param bundle
     * @param cls
     * @param requestCode
     */
    protected void goActivityForResult(Bundle bundle, Class<?> cls, int requestCode){
        Intent intent=new Intent(mContext,cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    /**
     * 设置状态栏颜色 状态栏为亮色，字体为黑色 反之字体为白色
     * @param drakMode true 字体黑色 false 字体白色
     */
    protected void setStatusBarLight(boolean drakMode){
        PhoneUtil.applyKitKatTranslucency(getWindow(),drakMode);
        PhoneUtil.setMiuiStatusBarDarkMode(this,drakMode);
        PhoneUtil.setMeizuStatusBarDarkIcon(this,drakMode);

    }

    /**
     * 添加网络请求
     * @param observable
     * @param next
     */
    protected <T extends BaseModel> void addNetwork(rx.Observable<T> observable, Action1<T> next, Action1<Throwable> error){
       rxManage.add(observable, next, error);
    }

    public abstract int getLayoutResId();
    public abstract void initDatas();
    public abstract void initView();
    public abstract void initAction();
}
