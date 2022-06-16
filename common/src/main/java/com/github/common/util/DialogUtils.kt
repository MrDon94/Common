package com.github.common.util

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog

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