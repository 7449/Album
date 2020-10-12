package com.gallery.scan.callback

import android.content.Context
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

    /**
     * [Context]
     * 如果[scanOwner] [scanOwnerGeneric] 返回的是 FragmentActivity 或者 Fragment
     * 则[scanContext]不是必须的，否则必须传递context
     */
    val scanContext: Context
        get() = throw KotlinNullPointerException("scanContext == null")

    /**
     * 扫描所需参数
     */
    val scanCursorLoaderArgs: CursorLoaderArgs

    /**
     * 自定义实体类参数
     */
    val scanEntityFactory: ScanEntityFactory

    /**
     *  [scanOwnerGeneric]
     *  [LoaderManager.getInstance]
     *  注意传入的参数是否正确，[ViewModelStoreOwner]不正确会导致强转出错
     *  必须继承于
     *  [ViewModelStoreOwner]
     *  [LifecycleOwner]
     *  两个接口，如有需要直接传递 FragmentActivity 或者 Fragment 即可
     */
    val scanOwner: ViewModelStoreOwner

    /**
     *  [LoaderManager.getInstance]
     *  [scanOwner]
     */
    fun <T> scanOwnerGeneric(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
        @Suppress("UNCHECKED_CAST")
        return scanOwner as T
    }

}
