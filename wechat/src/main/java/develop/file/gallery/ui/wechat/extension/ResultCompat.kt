package develop.file.gallery.ui.wechat.extension

import android.os.Parcelable
import develop.file.gallery.ui.wechat.args.WeChatGalleryConfig

internal object ResultCompat {

    internal val Parcelable?.weChatGalleryArgOrDefault: WeChatGalleryConfig
        get() = this as? WeChatGalleryConfig ?: WeChatGalleryConfig()

}