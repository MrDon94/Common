package com.github.common.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName RxUtils
 * Package com.kingyun.common.util
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-01-16 14:34
 * version V1.0
 */
public class RxUtils {

    public static <T> FlowableTransformer<T, T> flowableIO() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> ObservableIO() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> flowableComputation() {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation());
            }
        };
    }

    public static <T> ObservableTransformer<T, T> ObservableComputation() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.computation());
            }
        };
    }

    public static <T> FlowableTransformer<T, T> flowableRxLoadingDialog(Context context) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                final MaterialDialog loadingDialog = DialogUtils.getLoadingDialog(context);
                return upstream.doOnSubscribe(subscription -> loadingDialog.show())
                        .doOnTerminate(loadingDialog::dismiss)
                        .doFinally(loadingDialog::dismiss);
            }
        };
    }

    public static <T> FlowableTransformer<T, T> flowableRxLoadingDialog(final Context context, final String loadingText) {
        return new FlowableTransformer<T, T>() {
            @Override
            public Publisher<T> apply(Flowable<T> upstream) {
                final MaterialDialog loadingDialog = DialogUtils.getLoadingDialog(context, loadingText);
                return upstream.doOnSubscribe(subscription -> loadingDialog.show())
                        .doOnTerminate(loadingDialog::dismiss);
            }
        };
    }

    public static <T> ObservableTransformer<T, T> rxLoadingDialog(final Context context) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                final MaterialDialog loadingDialog = DialogUtils.getLoadingDialog(context);
                return upstream.doAfterNext(t -> loadingDialog.show()).doOnTerminate(loadingDialog::dismiss);
            }
        };
    }

    public static <T> ObservableTransformer<T, T> rxLoadingDialog(final Context context, final String loadingText) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                final MaterialDialog loadingDialog = DialogUtils.getLoadingDialog(context, loadingText);
                return upstream.doOnSubscribe(disposable -> loadingDialog.show())
                        .doOnTerminate(loadingDialog::dismiss);
            }
        };
    }
}
