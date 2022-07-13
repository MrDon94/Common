package com.github.common;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.common.util.log.DiskLogStrategy;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import timber.log.Timber;

/**
 * @PrjectName common
 * @Describe 参数配置类,配置一些必要的自定义参数
 * @Author cd
 * @CreateTime 2022/6/15 14:12
 **/
public class CommonConfig {
    private static volatile CommonConfig sInstance;
    public static CommonConfig getInstance() {
        if (sInstance == null) {
            synchronized (CommonConfig.class) {
                if (sInstance == null) {
                    sInstance = new CommonConfig();
                }
            }
        }
        return sInstance;
    }

    private CommonConfig() {
    }

    public CommonConfig init(Application application){
        initLog(application);
        return this;
    }

    private void initLog(Application application) {
        Logger.addLogAdapter(new AndroidLogAdapter());
        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree(){
                private static final int MAX_TAG_LENGTH = 23;
                @Nullable
                @Override
                protected String createStackElementTag(@NotNull StackTraceElement element) {
                    String tag = "(" + element.getFileName() + ":" + element.getLineNumber() + ")";
                    // 日志 TAG 长度限制已经在 Android 8.0 被移除
                    if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        return tag;
                    }
                    return tag.substring(0, MAX_TAG_LENGTH);
                }

                @Override
                protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
                    Logger.log(priority, tag, message, t);
                }
            });
        }else {
            String diskPath;
            if (application.getExternalCacheDir() != null){
                diskPath = application.getExternalCacheDir().getAbsolutePath();
            }else {
                diskPath = application.getCacheDir().getAbsolutePath();
            }
            String folder = diskPath + File.separatorChar + "logger";

            HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
            ht.start();
            final int MAX_BYTES = 500 * 1024; // 500K averages to a 4000 lines per file
            Handler handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, MAX_BYTES);
            DiskLogStrategy logStrategy = new DiskLogStrategy(handler);
            FormatStrategy formatStrategy = CsvFormatStrategy.newBuilder()
                    .logStrategy(logStrategy)
                    .build();
            Logger.addLogAdapter(new DiskLogAdapter(formatStrategy));
            Timber.plant(new CrashReportingTree());
        }
    }

    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, @NonNull String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            if (t != null) {
                if (priority == Log.ERROR) {
                    Logger.log(priority, tag, message, t);
                } else if (priority == Log.WARN) {
                    Logger.log(priority, tag, message, t);
                }
            }
        }
    }
}
