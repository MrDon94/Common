package com.github.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.util.Timer;

import timber.log.Timber;

/**
 * 通过声明 {@link ContentProvider} 自动完成初始化
 */
public class InitProvider extends ContentProvider {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;

    @Override
    public boolean onCreate() {
        mContext = getContext();
        CommonConfig.getInstance()
                .init((Application) getContext().getApplicationContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
