package com.github.common.util.parse;

import android.os.Bundle;

/**
 * 如果当前类不是Activity fragment 可实现OnParseBundleCallback方法获取一个Bundle
 */

public interface OnParseBundleCallback {
    /**
     * 获取数据源Bundle
     */
    Bundle getParseDataBundle();
}
