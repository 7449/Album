package com.gallery.scan.callback

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

    companion object {
        fun Fragment.scanCore(
                factory: ScanEntityFactory,
                args: CursorLoaderArgs
        ): ScanCore {
            return object : ScanCore {
                override val scanOwner: LifecycleOwner
                    get() = this@scanCore
                override val scanCursorLoaderArgs: CursorLoaderArgs
                    get() = args
                override val scanEntityFactory: ScanEntityFactory
                    get() = factory
            }
        }

        fun FragmentActivity.scanCore(
                factory: ScanEntityFactory,
                args: CursorLoaderArgs
        ): ScanCore {
            return object : ScanCore {
                override val scanOwner: LifecycleOwner
                    get() = this@scanCore
                override val scanCursorLoaderArgs: CursorLoaderArgs
                    get() = args
                override val scanEntityFactory: ScanEntityFactory
                    get() = factory
            }
        }
    }

    /**
     *  注意传入的参数是否正确，[LifecycleOwner]不正确会导致强转出错
     *  必须继承于
     *  [ViewModelStoreOwner]
     *  [LifecycleOwner]
     */
    val scanOwner: LifecycleOwner

    /**
     * 扫描所需参数
     */
    val scanCursorLoaderArgs: CursorLoaderArgs

    /**
     * 自定义实体类参数
     */
    val scanEntityFactory: ScanEntityFactory

    /**
     *  [LoaderManager.getInstance]
     *  [scanOwner]
     */
    fun <T> scanOwnerGeneric(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
        @Suppress("UNCHECKED_CAST")
        return scanOwner as T
    }

}