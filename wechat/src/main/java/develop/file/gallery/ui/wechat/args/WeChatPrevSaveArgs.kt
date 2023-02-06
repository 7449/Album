package develop.file.gallery.ui.wechat.args

import android.os.Bundle
import android.os.Parcelable
import develop.file.gallery.extensions.ResultCompat.parcelableVersion
import kotlinx.parcelize.Parcelize

@Parcelize
internal data class WeChatPrevSaveArgs(
    val ids: ArrayList<Long>,
) : Parcelable {
    companion object {
        private const val Key = "weChatPrevSaveArgs"
        fun WeChatPrevSaveArgs.toBundle(bundle: Bundle): Bundle {
            bundle.putParcelable(Key, this)
            return bundle
        }

        internal val Bundle.weChatPrevSaveArgs
            get() = parcelableVersion<WeChatPrevSaveArgs>(Key)
    }
}