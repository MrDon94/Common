package com.github.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 通用工具类
 **/
public class Utils {
    @NonNull
    public static <T> T checkNotNull(@Nullable final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
}
