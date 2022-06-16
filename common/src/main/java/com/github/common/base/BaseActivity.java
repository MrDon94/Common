package com.github.common.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RomUtils;
import com.github.common.BuildConfig;
import com.github.common.base.action.KeyboardAction;
import com.github.common.util.NavigationBarUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

/**
 * Activity基类
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity
        implements KeyboardAction {
    public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000; //需要自己定义标志

    private CompositeDisposable mCompositeDisposable;
    public T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);//禁用home按键关键代码
        Timber.d("当前Activity:"+getClass().getSimpleName());
        if (useEvent()) {
            // 注册对象
            EventBus.getDefault().register(this);
        }
        init();
    }

    protected void init() {
        mCompositeDisposable = new CompositeDisposable();
        initLayout();
        initView();
        KeyboardUtils.fixAndroidBug5497(this);
        initData();

        //适配华为手机底部有虚拟导航栏时会给根布局底部增加内边距的问题
        if (RomUtils.isHuawei() && NavigationBarUtil.checkDeviceHasNavigationBar(this)){
            View rootView = getContentView().getChildAt(0);
            rootView.post(() -> rootView.setPadding(0,0,0,0));
        }
    }

    /**
     * 获取布局 ID
     */
    protected abstract int getLayoutId();
    /**
     * 初始化控件
     */
    protected abstract void initView();
    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化布局
     */
    protected void initLayout() {
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            initSoftKeyboard();
        }
    }

    /**
     * 初始化软键盘
     */
    protected void initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView().setOnClickListener(v -> {
            // 隐藏软键，避免内存泄漏
            hideKeyboard(getCurrentFocus());
        });
    }

    /**
     * 和 setContentView 对应的方法
     */
    public ViewGroup getContentView() {
        return findViewById(Window.ID_ANDROID_CONTENT);
    }

    @Override
    public void finish() {
        // 隐藏软键，避免内存泄漏
        hideKeyboard(getCurrentFocus());
        super.finish();
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        if (useEvent()) {
            // 注销
            EventBus.getDefault().unregister(this);
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
        if (mPresenter != null) {
            mPresenter.onDestroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

    public void addDisposable(Disposable... disposables) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        for (Disposable disposable : disposables) {
            mCompositeDisposable.add(disposable);
        }
    }

    protected void showToastMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void showMsg(String msg) {
        showToastMsg(msg);
    }

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    protected boolean useEvent() {
        //默认不使用，需要使用请继承后重新写该方法
        return false;
    }

    public Context getContext() {
        return this;
    }

}
