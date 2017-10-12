package com.jusfoun.baselibrary.base;


import android.text.TextUtils;

import com.jusfoun.baselibrary.Util.LogUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Author  wangchenchen
 * CreateDate 2016/7/6.
 * Email wcc@jusfoun.com
 * Description 管理rxbus实践和rxjava 相关代码生命周期
 */
public class RxManage {

    public RxBus mRxbus=RxBus.getInstance();
    private Map<Object, Observable<?>> mObservables=new HashMap<>();
    private CompositeSubscription mCompositeSubscription=new CompositeSubscription();

    public void on(String eventName, Action1<Object> action1){
        Observable<?> mObservable=mRxbus.register(eventName);
        mObservables.put(eventName,mObservable);
        mCompositeSubscription.add(
                mObservable.observeOn(AndroidSchedulers.mainThread())
                        .subscribe(action1, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                LogUtil.e(getClass().getSimpleName(),throwable.getMessage());
                            }
                        })
        );
    }

    public void registerSticky(Object subscriber){
        if (subscriber==null){
            throw  new IllegalArgumentException("subscriber not null");
        }
        Observable<?> mObservable=mRxbus.register(subscriber);
    }

    public void postSticky(){

    }

    public void register(Object subscriber){
        if (subscriber==null){
            throw  new IllegalArgumentException("subscriber not null");
        }
        Observable<?> mObservable=mRxbus.register(subscriber);
        mObservables.put(subscriber,mObservable);
        mCompositeSubscription.add(mObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        if (o instanceof RxBus.EventEntry){
                            RxBus.EventEntry eventEntry= (RxBus.EventEntry) o;
                            Class cls=eventEntry.tag.getClass();
                            for (Method method : cls.getMethods()) {
                                if (TextUtils.equals(method.getName(),"onEvent")){
                                    try {
                                        method.invoke(eventEntry.tag,eventEntry.evnet);
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.e(getClass().getSimpleName(),throwable.getMessage());
                    }
                }));
    }

    public void add(Subscription m){
        mCompositeSubscription.add(m);
    }

    /**
     * 添加网络请求
     * @param observable
     * @param action1
     * @param error
     */
    public <T extends BaseModel> void add(Observable<T> observable,Action1<T> action1,Action1<Throwable> error){
        mCompositeSubscription.add(
                observable.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(action1,error)
        );
    }

    public void post(Object event){
        mRxbus.post(event);
    }

    public void post(Object tag, Object content){
        mRxbus.post(tag, content);
    }

    public void clear(){
        mCompositeSubscription.unsubscribe();
        for (Map.Entry<Object,Observable<?>> entry:mObservables.entrySet()){
            mRxbus.unRegister(entry.getKey(),entry.getValue());
        }
    }
}
