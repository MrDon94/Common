package com.github.common.util.parse;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;

import timber.log.Timber;


/**
 * 根据注解自动解析Bundle参数和值
 */

public class ParseData {

    private static final String TAG = "CommonParseData";
    /**
     * 需要解析的实例
     */
    private Object mParseSource = null;
    /**
     * 解析数据源
     */
    private Bundle mBundle = null;

    /**
     * 构造方法 传入需要解析实例
     *
     * @param parseSource 需要解析的类 只能为{@link Activity}，{@link Fragment}，{@link OnParseBundleCallback}
     */
    public ParseData(Object parseSource) {
        if (parseSource == null) {
            throw new NullPointerException("parseSource not null");
        }
        mParseSource = parseSource;
        if (parseSource instanceof Activity) {
            mBundle = ((Activity) parseSource).getIntent().getExtras();
        } else if (parseSource instanceof Fragment) {
            mBundle = ((Fragment) parseSource).getArguments();
        } else if (parseSource instanceof android.support.v4.app.Fragment) {
            mBundle = ((android.support.v4.app.Fragment) parseSource).getArguments();
        } else if (parseSource instanceof OnParseBundleCallback) {
            mBundle = ((OnParseBundleCallback) parseSource).getParseDataBundle();
        } else {
            throw new NullPointerException("not find bundle ");
        }
        if (mBundle != null) {
            initParse(mParseSource);
        }
    }

    private boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    private void parseData(Class<?> parseData, Object parseSource) {
        Field[] fields = parseData.getDeclaredFields();
        try {
            for (Field field : fields) {
                BindKey parse = field.getAnnotation(BindKey.class);
                if (parse == null) {
                    continue;
                }
                String type = field.getGenericType().toString().replace("class ", "");
                Timber.d("要解析的类型" + type);
                field.setAccessible(true);
                String key = parse.value();
                if (isEmpty(key)) {
                    //默认是当前变量名
                    key = field.getName();
                }
                Object value = mBundle.get(key);
                //为空则不赋值 值就为默认定义的参数值
                if (value != null) {
                    field.set(parseSource, value);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void initParse(Object bean) {
        Class<?> cls = bean.getClass();
        parseData(cls, bean);
        Class t = cls.getSuperclass();
        if (t != null) {
            parseData(t, bean);
        }
    }
}
