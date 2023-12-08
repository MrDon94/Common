package com.github.common.base

import android.content.Intent
import android.content.res.Resources
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.RomUtils
import com.github.common.BuildConfig
import com.github.common.util.NavigationBarUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.AutoSizeConfig
import org.greenrobot.eventbus.EventBus

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName BaseActivity
 * Package com.kingyun.common.base
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-01-15 10:37
 * version V1.0
 */
abstract class BaseActivity<VB : ViewDataBinding, T : BasePresenter<*>?> : AppCompatActivity(), KeyboardAction,
    CoroutineScope by MainScope() {
    lateinit var mBinding: VB
    private var mCompositeDisposable: CompositeDisposable? = null
    @JvmField
    var mPresenter: T? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            LogUtils.e("当前Activity:" + javaClass.simpleName)
        }
        if (useEvent()) {
            // 注册对象
            EventBus.getDefault().register(this)
        }
        init()
    }

    protected fun init() {

        mCompositeDisposable = CompositeDisposable()
        initLayout()
        initView()
        KeyboardUtils.fixAndroidBug5497(this)
        initData()

        //适配华为手机底部有虚拟导航栏时会给根布局底部增加内边距的问题
        if (RomUtils.isHuawei() && NavigationBarUtil.checkDeviceHasNavigationBar(this)) {
            val rootView = getContentView()?.getChildAt(0)
            rootView?.post { rootView.setPadding(0, 0, 0, 0) }
        }
    }

    /**
     * 获取布局 ID
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化控件
     */
    protected abstract fun initView()

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * 初始化布局
     */
    protected open fun initLayout() {
        if (getLayoutId() > 0) {
            mBinding = DataBindingUtil.setContentView(this, getLayoutId()) as VB
            mBinding.lifecycleOwner = this
            initSoftKeyboard()
        }
    }

    /**
     * 初始化软键盘
     */
    protected open fun initSoftKeyboard() {
        // 点击外部隐藏软键盘，提升用户体验
        getContentView()!!.setOnClickListener {
            // 隐藏软键，避免内存泄漏
            hideKeyboard(currentFocus)
        }
    }

    /**
     * 和 setContentView 对应的方法
     */
    open fun getContentView(): ViewGroup? {
        return findViewById(Window.ID_ANDROID_CONTENT)
    }

    override fun finish() {
        // 隐藏软键，避免内存泄漏
        hideKeyboard(currentFocus)
        super.finish()
    }

    /**
     * 如果当前的 Activity（singleTop 启动模式） 被复用时会回调
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        // 设置为当前的 Intent，避免 Activity 被杀死后重启 Intent 还是最原先的那个
        setIntent(intent)
    }

    override fun onDestroy() {
        cancel()
        mBinding.unbind()
        if (useEvent()) {
            // 注销
            EventBus.getDefault().unregister(this)
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
            mCompositeDisposable = null
        }
        if (mPresenter != null) {
            mPresenter?.onDestroy()
            mPresenter = null
        }
        super.onDestroy()
    }

    fun addDisposable(vararg disposables: Disposable?) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        for (disposable in disposables) {
            mCompositeDisposable!!.add(disposable!!)
        }
    }

    fun showMsg(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    protected val tag: String
        protected get() = this.javaClass.simpleName

    protected open fun useEvent(): Boolean {
        //默认不使用，需要使用请继承后重新写该方法
        return false
    }

    fun getContext() = this

    override fun getResources(): Resources {
        try {
            AutoSizeConfig.getInstance().isBaseOnWidth = true

            AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.getResources()
    }
}