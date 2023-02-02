package com.gallery.sample.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.args.CameraConfig
import com.gallery.core.args.FileConfig
import com.gallery.core.args.GalleryConfigs
import com.gallery.core.args.GridConfig
import com.gallery.core.entity.ScanEntity
import com.gallery.sample.R
import com.gallery.sample.clickSelectIcon
import com.gallery.sample.clickShowColorPicker
import com.gallery.sample.databinding.SimpleLayoutGallerySettingBinding
import com.gallery.sample.showCompoundDrawables
import com.gallery.sample.toIntOrNull
import com.gallery.scan.Types
import java.io.File

@SuppressLint("NonConstantResourceId")
class GalleryConfigsSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_TAKE_PICTURE_NAME = "gallery_take_picture_name"
        private const val DEFAULT_CROP_PICTURE_NAME = "gallery_crop_picture_name"
        private const val DEFAULT_SD_NAME = "根目录"
        private const val DEFAULT_ALL_NAME = "全部"
        private const val DEFAULT_SELECT_MAX_COUNT = 9
        private const val DEFAULT_SELECT_MIN_COUNT = 3
        private const val DEFAULT_SPAN_COUNT = 3
        private const val DEFAULT_MAX_SPAN_COUNT = 6
        private const val DEFAULT_IMAGE_SUFFIX = "jpg"
        private const val DEFAULT_VIDEO_SUFFIX = "mp4"
        private const val DEFAULT_CAMERA_TEXT = "相机"
        private const val DEFAULT_TEXT_SIZE_MAX = 16F
        private const val DEFAULT_TEXT_SIZE_MIN = 12F

        private const val DEFAULT_EMPTY_AND_CAMERA_ICON = R.drawable.ic_default_camera_drawable
    }

    private val viewBinding: SimpleLayoutGallerySettingBinding =
        SimpleLayoutGallerySettingBinding.inflate(LayoutInflater.from(getContext()), this, true)

    init {
        viewBinding.includeCameraInfo.cameraTextColor.clickShowColorPicker()
        viewBinding.includeCameraInfo.cameraBgColor.clickShowColorPicker()
        viewBinding.includeCameraInfo.cameraIcon.clickSelectIcon()
        viewBinding.includeCameraInfo.cameraIcon.showCompoundDrawables(DEFAULT_EMPTY_AND_CAMERA_ICON)
        viewBinding.includeCameraInfo.emptyIcon.clickSelectIcon()
        viewBinding.includeCameraInfo.emptyIcon.showCompoundDrawables(DEFAULT_EMPTY_AND_CAMERA_ICON)
    }

    val customCamera: Boolean
        get() = viewBinding.includeBool.customCamera.isChecked

    private fun getScanTypeArray(): Array<String> {
        return when (viewBinding.includeScanType.scanTypeRb.checkedRadioButtonId) {
            R.id.scan_image -> arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
            R.id.scan_video -> arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
            R.id.scan_mix -> arrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
            )

            else -> arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        }
    }

    private fun getScanSort(): String {
        return when (viewBinding.includeScanType.scanSortRb.checkedRadioButtonId) {
            R.id.sort_desc -> Types.Sort.DESC
            R.id.sort_asc -> Types.Sort.ASC
            else -> Types.Sort.DESC
        }
    }

    private fun getPicturePath(): String {
        return when (viewBinding.includePictureOutPath.picturePathTitle.checkedRadioButtonId) {
            R.id.picture -> Environment.DIRECTORY_PICTURES
            R.id.dcim -> Environment.DIRECTORY_DCIM
            else -> Environment.DIRECTORY_PICTURES
        } + File.separator + viewBinding.includePictureOutPath.picturePathSuffix.text.toString()
    }

    private fun getCheckBoxRes(): Int {
        return when (viewBinding.includeSelectRadioView.selectRadioView.checkedRadioButtonId) {
            R.id.radio_accent -> R.drawable.simple_app_selector_gallery_item_check
            R.id.radio_box -> R.drawable.simple_default_selector_rb_check
            R.id.radio_blue -> R.drawable.simple_blue_selector_gallery_item_check
            R.id.radio_default -> R.drawable.selector_default_gallery_item_check
            else -> R.drawable.selector_default_gallery_item_check
        }
    }

    private fun getOrientation(): Int {
        return when (viewBinding.includeListOrientation.listOrientation.checkedRadioButtonId) {
            R.id.orientation_vertical -> RecyclerView.VERTICAL
            R.id.orientation_horizontal -> RecyclerView.HORIZONTAL
            else -> RecyclerView.VERTICAL
        }
    }

    private fun getSelectMaxCount(): Int {
        val selectMaxCountView = viewBinding.includeSelectMaxAndSpanCount.selectMax
        var maxCount = selectMaxCountView.text.toString().toIntOrNull() ?: DEFAULT_SELECT_MAX_COUNT
        if (maxCount <= DEFAULT_SELECT_MIN_COUNT) {
            maxCount = DEFAULT_SELECT_MAX_COUNT
            selectMaxCountView.setText(maxCount.toString())
        }
        return maxCount
    }

    private fun getSpanCount(): Int {
        val spanCountView = viewBinding.includeSelectMaxAndSpanCount.spanCount
        var spanCount = spanCountView.text.toString().toIntOrNull() ?: DEFAULT_SPAN_COUNT
        if (spanCount <= DEFAULT_SPAN_COUNT || spanCount > DEFAULT_MAX_SPAN_COUNT) {
            spanCount = DEFAULT_SPAN_COUNT
            spanCountView.setText(spanCount.toString())
        }
        return spanCount
    }

    private fun getTakePictureName(): String {
        val takePictureName = viewBinding.includeTakePictureName.takePictureName
        return takePictureName.text.toString().ifEmpty {
            takePictureName.setText(DEFAULT_TAKE_PICTURE_NAME)
            getTakePictureName()
        }
    }

    private fun getCropPictureName(): String {
        val cropPictureName = viewBinding.includeCropPictureName.cropPictureName
        return cropPictureName.text.toString().ifEmpty {
            cropPictureName.setText(DEFAULT_CROP_PICTURE_NAME)
            getCropPictureName()
        }
    }

    private fun getSdNameAndAllName(): Pair<String, String> {
        val sdName = viewBinding.includeSdAndAllName.sdName.text.toString().ifEmpty {
            viewBinding.includeSdAndAllName.sdName.setText(DEFAULT_SD_NAME)
            DEFAULT_SD_NAME
        }
        val allName = viewBinding.includeSdAndAllName.allName.text.toString().ifEmpty {
            viewBinding.includeSdAndAllName.allName.setText(DEFAULT_ALL_NAME)
            DEFAULT_ALL_NAME
        }
        return sdName to allName
    }

    private fun getCameraTextSize(): Float {
        val textSizeView = viewBinding.includeCameraInfo.titleSize
        var textSize = textSizeView.text.toString().toFloatOrNull() ?: DEFAULT_TEXT_SIZE_MAX
        if (textSize < DEFAULT_TEXT_SIZE_MIN || textSize > DEFAULT_TEXT_SIZE_MAX) {
            textSize = DEFAULT_TEXT_SIZE_MAX
            textSizeView.setText(textSize.toString())
        }
        return textSize
    }

    private fun getCameraText(): String {
        val cameraNameView = viewBinding.includeCameraInfo.cameraName
        return cameraNameView.text.toString().ifEmpty {
            cameraNameView.setText(DEFAULT_CAMERA_TEXT)
            getCameraText()
        }
    }

    private fun getCameraIcon(): Int {
        val cameraIconView = viewBinding.includeCameraInfo.cameraIcon
        return cameraIconView.tag.toIntOrNull {
            cameraIconView.tag = DEFAULT_EMPTY_AND_CAMERA_ICON
            getCameraIcon()
        }
    }

    private fun getEmptyIcon(): Int {
        val emptyIconView = viewBinding.includeCameraInfo.emptyIcon
        return emptyIconView.tag.toIntOrNull {
            emptyIconView.tag = DEFAULT_EMPTY_AND_CAMERA_ICON
            getEmptyIcon()
        }
    }

    fun updateDefaultSelectItems(select: ArrayList<ScanEntity>) {
        viewBinding.includeSelect.defaultSelectData.text =
            select.joinToString("\n") { it.delegate.displayName }
    }

    fun createGalleryConfigs(select: ArrayList<ScanEntity> = arrayListOf()): GalleryConfigs {
        updateDefaultSelectItems(select)

        val scanArray = getScanTypeArray()
        val picturePath = getPicturePath()
        val sort = getScanSort()
        val checkBoxRes = getCheckBoxRes()
        val orientation = getOrientation()
        val maxCount = getSelectMaxCount()
        val spanCount = getSpanCount()
        val takeName = getTakePictureName()
        val cropName = getCropPictureName()
        val sdAndAllName = getSdNameAndAllName()
        val cameraText = getCameraText()
        val cameraTextSize = getCameraTextSize()
        val cameraIcon = getCameraIcon()
        val emptyIcon = getEmptyIcon()

        val hideCamera = viewBinding.includeBool.hideCamera.isChecked
        val radio = viewBinding.includeBool.radio.isChecked
        val crop = viewBinding.includeBool.crop.isChecked
        val takePictureCrop = viewBinding.includeBool.takePictureCrop.isChecked
        val isScanVideoMedia =
            scanArray.size == 1 && scanArray.contains(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())

        val gridConfig = GridConfig(spanCount, orientation)

        val configs = GalleryConfigs(
            selects = select,
            type = scanArray,
            sort = sort to MediaStore.Files.FileColumns.DATE_MODIFIED,
            hideCamera = hideCamera,
            radio = radio,
            crop = crop,
            takePictureCrop = takePictureCrop,
            maxCount = maxCount,
            sdNameAndAllName = sdAndAllName,
            fileConfig = FileConfig(
                picturePath = picturePath,
                cropPath = picturePath,
                pictureName = takeName,
                pictureNameSuffix = if (isScanVideoMedia) DEFAULT_VIDEO_SUFFIX else DEFAULT_IMAGE_SUFFIX,
                cropName = cropName,
                cropNameSuffix = DEFAULT_IMAGE_SUFFIX
            ),
            cameraConfig = CameraConfig(
                text = cameraText,
                textSize = cameraTextSize,
                textColor = viewBinding.includeCameraInfo.cameraTextColor.currentTextColor,
                icon = cameraIcon,
                background = viewBinding.includeCameraInfo.cameraBgColor.currentTextColor,
                emptyIcon = emptyIcon,
                checkBoxIcon = checkBoxRes,
            ),
            gridConfig = gridConfig,
        )

        viewBinding.includeBool.hideCamera.isChecked = false
        viewBinding.includeBool.radio.isChecked = false
        viewBinding.includeBool.crop.isChecked = false
        viewBinding.includeBool.takePictureCrop.isChecked = false
        viewBinding.includeBool.customCamera.isChecked = false

        return configs
    }

}