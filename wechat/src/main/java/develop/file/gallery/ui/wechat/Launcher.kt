package develop.file.gallery.ui.wechat

import android.content.Intent
import android.graphics.Color
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import com.gallery.ui.wechat.R
import develop.file.gallery.args.CameraConfig
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.compat.Gallery.Companion.startGallery
import develop.file.gallery.ui.wechat.activity.WeChatGalleryActivity
import develop.file.gallery.ui.wechat.args.WeChatGalleryConfig

internal val scanType = listOf(
    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
)

internal val finderIcon = R.drawable.ic_wechat_gallery_finder_action

internal val checkBoxResource = R.drawable.wechat_gallery_selector_gallery_item_check

internal val checkBoxFrameResource = R.drawable.wechat_gallery_selector_gallery_select

internal val checkboxFImageResource = R.drawable.wechat_gallery_selector_gallery_full_image_check

internal val rgb19 = Color.rgb(19, 19, 19)

internal val rgb38 = Color.rgb(38, 38, 38)

internal val color76 = Color.parseColor("#767676")

internal const val alpha9 = 0.9F

internal const val size14 = 14F

internal const val size12 = 12F

internal const val size13 = 13F

internal const val colorWhite = Color.WHITE

internal const val colorBlack = Color.BLACK

internal const val textPrev = "预览"

internal const val textSend = "发送"

internal const val textSelect = "选择"

internal const val textAllVideo = "全部视频"

internal const val textRootSdName = "根目录"

internal const val textAllSdName = "图片和视频"

internal const val maxVideoDuration = 500000

internal val configs = GalleryConfigs(
    sdNameAndAllName = textRootSdName to textAllSdName,
    hideCamera = true,
    type = scanType,
    cameraConfig = CameraConfig(checkBoxIcon = checkBoxResource)
)

internal val gapConfigs = WeChatGalleryConfig()

fun FragmentActivity.weChatGallery(launcher: ActivityResultLauncher<Intent>) {
    startGallery(
        launcher = launcher,
        gap = gapConfigs,
        configs = configs,
        clz = WeChatGalleryActivity::class.java
    )
}