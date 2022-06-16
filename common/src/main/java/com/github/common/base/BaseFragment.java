package com.github.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    private CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        if (useEvent()) {
            // 注册对象
            EventBus.getDefault().register(this);
        }
    }

    protected void initView(@NonNull View view) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    public void addDisposable(Disposable... disposables) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        for (Disposable disposable : disposables) {
            mCompositeDisposable.add(disposable);
        }
    }

    protected void initData() {

    }
    /**
     * 按钮点击
     *
     * @param view
     */
    protected void onUClick(View view) {

    }

    public void showMsg(String msg) {
        ToastUtils.showShort(msg);
    }

    public abstract int getLayoutId();

    @Override
    public void onDestroy() {
        if (useEvent()) {
            // 注销
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    protected boolean useEvent() {
        //默认不使用，需要使用请继承后重新写该方法
        return false;
    }
}
