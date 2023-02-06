package develop.file.gallery.ui.material.extensions

import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import develop.file.gallery.compat.widget.GalleryImageView

internal object ViewCompat {

    internal fun ViewGroup.createGalleryImageView(): GalleryImageView {
        return GalleryImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER
            }
        }
    }

}