package develop.file.media.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import develop.file.media.MediaContext
import develop.file.media.Types
import develop.file.media.args.MediaCursorLoaderArgs
import develop.file.media.args.MediaEntityFactory
import develop.file.media.impl.file.file

/** 是否是扫描全部的Id */
val Long.isAllMediaParent: Boolean get() = this == Types.Id.ALL

/** 是否禁止扫描,也可以自定义参数,不一定非要使用内置的参数 */
val Long.isNoNeMediaParent: Boolean get() = this == Types.Id.NONE

fun Fragment.media(
    args: MediaCursorLoaderArgs, factory: MediaEntityFactory = MediaEntityFactory.file()
): MediaContext = object : MediaContext {
    override fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
        @Suppress("UNCHECKED_CAST")
        return this@media as T
    }

    override val args: MediaCursorLoaderArgs get() = args
    override val factory: MediaEntityFactory get() = factory
}

fun FragmentActivity.media(
    args: MediaCursorLoaderArgs, factory: MediaEntityFactory = MediaEntityFactory.file()
): MediaContext = object : MediaContext {
    override fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner {
        @Suppress("UNCHECKED_CAST")
        return this@media as T
    }

    override val args: MediaCursorLoaderArgs get() = args
    override val factory: MediaEntityFactory get() = factory
}