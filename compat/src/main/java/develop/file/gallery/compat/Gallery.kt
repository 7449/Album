package develop.file.gallery.compat

import android.content.Intent
import android.os.Parcelable
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.compat.activity.args.GalleryCompatArgs
import develop.file.gallery.compat.activity.args.GalleryCompatArgs.Companion.toBundle

open class Gallery(
    private val activity: FragmentActivity,
    private val args: GalleryCompatArgs,
    private val clz: Class<*>,
    launcher: ActivityResultLauncher<Intent>,
) {

    companion object {
        fun FragmentActivity.startGallery(
            configs: GalleryConfigs = GalleryConfigs(),
            gap: Parcelable? = null,
            clz: Class<*>,
            launcher: ActivityResultLauncher<Intent>,
        ): Gallery {
            return Gallery(this, GalleryCompatArgs(configs, gap), clz, launcher)
        }
    }

    init {
        launcher.launch(launchIntent())
    }

    private fun launchIntent(): Intent {
        return Intent(activity, clz).apply { putExtras(args.toBundle()) }
    }

}