package com.album.listener

import android.view.View

/**
 * @author y
 * @create 2019/2/27
 *
 * 没有图片时点击显示的占位符点击事件
 */
interface OnEmptyClickListener {
    /**
     * true 打开相机，false自定义点击事件
     */
    fun click(view: View): Boolean
}