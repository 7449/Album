package com.gallery.core.callback

import com.gallery.core.ui.fragment.PrevFragment

/**
 * [PrevFragment] 拦截器
 */
interface IGalleryPrevInterceptor {
    /**
     * 是否隐藏[PrevFragment]的选择器
     * 如果隐藏请调用[PrevFragment.checkBoxClick]
     */
    val hideCheckBox: Boolean
        get() = false
}

