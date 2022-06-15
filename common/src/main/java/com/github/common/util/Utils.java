package com.github.common.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @PrjectName common
 * @Describe 描述
 * @Author cd
 * @CreateTime 2022/6/15 14:49
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
