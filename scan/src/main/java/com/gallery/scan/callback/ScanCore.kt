package com.gallery.scan.callback

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.loader.app.LoaderManager
import com.gallery.scan.args.CursorLoaderArgs
import com.gallery.scan.args.ScanEntityFactory

/**
 * @author y
 * @create 2019/2/27
 * 核心参数
 */
interface ScanCore {

    companion object;

    /**
     *  注意传入的参数是否正确，[LifecycleOwner]不正确会导致强转出错
     *  必须继承于
     *  [ViewModelStoreOwner]
     *  和
     *  [LifecycleOwner]
     */
    val scanOwner: LifecycleOwner

    /**
     * 扫描所需参数
     */
    val loaderArgs: CursorLoaderArgs

    /**
     * 自定义获取实体类
     */
    val factory: ScanEntityFactory

    /**
     *  [LoaderManager.getInstance]
     *  和
     *  [scanOwner]
     */
    fun <T> scanOwnerGeneric(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
        @Suppress("UNCHECKED_CAST")
        return scanOwner as T
    }

}