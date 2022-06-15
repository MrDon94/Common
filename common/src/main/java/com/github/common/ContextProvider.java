package com.github.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.Method;

/**
 * 无侵入式全局上下文获取
 */
public class ContextProvider {
    @SuppressLint("StaticFieldLeak")
    private static volatile ContextProvider instance;
    private Context mContext;

    private ContextProvider(Context context) {
        mContext = context;
    }

    /**
     * 获取实例
     */
    public static ContextProvider get() {
        if (instance == null) {
            synchronized (ContextProvider.class) {
                if (instance == null) {
                    Context context = AppContextProvider.mContext;
                    if (context == null) {
                        //跨进程时通过反射获取上下文
                        try {
                            Class<?> clazz = Class.forName("android.app.ActivityThread");
                            Method method = clazz.getMethod("currentActivityThread");
                            Object currentActivityThread = method.invoke(clazz);
                            Method method2 = currentActivityThread.getClass().getMethod("getApplication");
                            context =(Context)method2.invoke(currentActivityThread);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (context == null){
                        throw new IllegalStateException("context == null");
                    }
                    instance = new ContextProvider(context);
                }
            }
        }
        return instance;
    }

    /**
     * 获取上下文
     */
    public Context getContext() {
        return mContext;
    }

    public Application getApplication() {
        return (Application) mContext.getApplicationContext();
    }
}
