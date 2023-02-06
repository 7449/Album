package develop.file.gallery.ui.wechat.result

import android.os.Bundle
import develop.file.gallery.compat.GalleryConfig
import develop.file.gallery.compat.extensions.ResultCompat.parcelableArrayList
import develop.file.gallery.compat.extensions.callbacks.GalleryListener
import develop.file.gallery.compat.extensions.callbacks.GalleryResultCallback
import develop.file.gallery.ui.wechat.WeChatConfig

class WeChatGalleryResultCallback(
    private val galleryListener: GalleryListener,
) : GalleryResultCallback(galleryListener) {

    override fun onMultipleDataResult(bundle: Bundle) {
        galleryListener.onGalleryResources(
            bundle.parcelableArrayList(GalleryConfig.RESULT_CODE_MULTIPLE_DATA.toString()),
            bundle.getBoolean(WeChatConfig.FULL_IMAGE)
        )
    }

}