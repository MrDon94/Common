package com.github.common.base

import android.app.Activity
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.greenrobot.eventbus.EventBus

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName BaseFragment
 * Package com.kingyun.common.base
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-02-15 17:21
 * version V1.0
 */
abstract class BaseFragment<VB : ViewDataBinding> : Fragment(), CoroutineScope by MainScope() {
    protected var mBinding: VB? = null
    private var mCompositeDisposable: CompositeDisposable? = null
    lateinit var mActivity: Activity
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        retainInstance = true

        if (mBinding == null) {
            mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        }

        return if (mBinding != null) {
            mBinding!!.root.apply { (parent as? ViewGroup)?.removeView(this) }
        } else super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding?.lifecycleOwner = this
        initView(view)
        if (useEvent()) {
            // 注册对象
            EventBus.getDefault().register(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(view: View)

    protected abstract fun initData()

    @Deprecated("这属于rxjava的方法，建议使用协程来替代")
    open fun addDisposable(vararg disposables: Disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = CompositeDisposable()
        }
        for (disposable in disposables) {
            mCompositeDisposable?.add(disposable)
        }
    }

    fun showMsg(msg: String) {
        ToastUtils.showShort(msg)
    }

    fun showMsg(msgResId: Int){
        ToastUtils.showShort(msgResId)
    }

    override fun onDestroy() {
        cancel()
        mBinding?.unbind()
        if (useEvent()) {
            // 注销
            EventBus.getDefault().unregister(this)
        }
        if (mCompositeDisposable != null) {
            mCompositeDisposable!!.clear()
            mCompositeDisposable = null
        }
        super.onDestroy()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    protected open fun useEvent(): Boolean {
        //默认不使用，需要使用请继承后重新写该方法
        return false
    }
}