package com.github.common.util

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

/**
 * company 重庆庆云石油工程技术有限责任公司
 * FileName DialogUtils
 * Package com.kingyun.common.util
 * Description ${DESCRIPTION}
 * author jiexiaoqiang
 * create 2019-01-16 15:12
 * version V1.0
 */
object DialogUtils {

    @JvmStatic
    fun getLoadingDialog(context: Context): MaterialDialog {
        return getLoadingDialog(context,"请稍后")
    }

    @JvmStatic
    fun getLoadingDialog(context: Context, loadingText: String = "请稍后..."): MaterialDialog {
        return MaterialDialog.Builder(context)
                .progress(true, 0)
                .content(loadingText)
                .canceledOnTouchOutside(false)
                .build()
    }
}