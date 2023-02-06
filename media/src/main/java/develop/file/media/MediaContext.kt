package develop.file.media

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import develop.file.media.args.MediaCursorLoaderArgs
import develop.file.media.args.MediaEntityFactory

interface MediaContext {

    val loaderArgs: MediaCursorLoaderArgs

    val factory: MediaEntityFactory

    fun <T> context(): T where T : LifecycleOwner, T : ViewModelStoreOwner

}