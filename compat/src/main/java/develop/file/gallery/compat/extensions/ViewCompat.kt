package develop.file.gallery.compat.extensions

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible

object ViewCompat {

    fun View.hide(): View = apply { if (!isGone) visibility = View.GONE }

    fun View.show(): View = apply { if (!isVisible) visibility = View.VISIBLE }

}