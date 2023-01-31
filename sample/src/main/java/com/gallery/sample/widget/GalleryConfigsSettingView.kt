package com.gallery.sample.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.gallery.core.CameraConfig
import com.gallery.core.GalleryConfigs
import com.gallery.core.GridConfig
import com.gallery.core.entity.ScanEntity
import com.gallery.sample.R
import com.gallery.sample.clickShowColorPicker
import com.gallery.sample.databinding.SimpleLayoutGallerySettingBinding
import com.gallery.scan.Types

@SuppressLint("SetTextI18n")
class GalleryConfigsSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: SimpleLayoutGallerySettingBinding =
        SimpleLayoutGallerySettingBinding.inflate(LayoutInflater.from(getContext()), this, true)

    init {
        viewBinding.includeCamera.cameraIconColor.clickShowColorPicker()
        viewBinding.includeCamera.cameraBgColor.clickShowColorPicker()
    }

    fun isCustomCamera(): Boolean {
        return viewBinding.includeBool.customCamera.isChecked
    }

    fun createGalleryConfigs(select: ArrayList<ScanEntity> = arrayListOf()): GalleryConfigs {
        viewBinding.includeSelect.defaultSelectData.text = select.toString()
        viewBinding.includePictureOutPath.pictureOutPath.text =
            Environment.DIRECTORY_PICTURES.toString()
        viewBinding.includePictureOutPathLow.pictureOutPathLow.text =
            Environment.DIRECTORY_PICTURES.toString()

        val scanArray = when (viewBinding.includeScanType.scanTypeRb.checkedRadioButtonId) {
            R.id.scan_image -> intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
            R.id.scan_video -> intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
            R.id.scan_mix -> intArrayOf(
                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
                MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO
            )

            else -> intArrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
        }

        val sort = when (viewBinding.includeScanType.scanSortRb.checkedRadioButtonId) {
            R.id.sort_desc -> Types.Sort.DESC
            R.id.sort_asc -> Types.Sort.ASC
            else -> Types.Sort.DESC
        }

        val checkBoxRes =
            when (viewBinding.includeSelectRadioView.selectRadioView.checkedRadioButtonId) {
                R.id.radio_accent -> R.drawable.simple_app_selector_gallery_item_check
                R.id.radio_box -> R.drawable.simple_default_selector_rb_check
                R.id.radio_blue -> R.drawable.simple_blue_selector_gallery_item_check
                R.id.radio_default -> R.drawable.selector_default_gallery_item_check
                else -> R.drawable.selector_default_gallery_item_check
            }

        val orientation =
            when (viewBinding.includeListOrientation.listOrientation.checkedRadioButtonId) {
                R.id.orientation_vertical -> RecyclerView.VERTICAL
                R.id.orientation_horizontal -> RecyclerView.HORIZONTAL
                else -> RecyclerView.VERTICAL
            }

        var maxCount =
            viewBinding.includeSelectMaxAndSpanCount.selectMax.text.toString().toIntOrNull() ?: 9
        if (maxCount <= 3) {
            maxCount = 9
            viewBinding.includeSelectMaxAndSpanCount.selectMax.setText(maxCount.toString())
        }

        var spanCount =
            viewBinding.includeSelectMaxAndSpanCount.spanCount.text.toString().toIntOrNull() ?: 3
        if (spanCount <= 3) {
            spanCount = 3
            viewBinding.includeSelectMaxAndSpanCount.spanCount.setText(spanCount.toString())
        }

        val takeName =
            viewBinding.includeTakePictureName.takePictureName.text.toString().ifEmpty {
                viewBinding.includeTakePictureName.takePictureName.setText("gallery_take_picture_name")
                "gallery_take_picture_name"
            }

        val cropName =
            viewBinding.includeCropPictureName.cropPictureName.text.toString().ifEmpty {
                viewBinding.includeCropPictureName.cropPictureName.setText("gallery_crop_picture_name")
                "gallery_crop_picture_name"
            }

        val sdName = viewBinding.includeSdName.sdName.text.toString().ifEmpty {
            viewBinding.includeSdName.sdName.setText("根目录")
            "根目录"
        }
        val allName = viewBinding.includeAllName.allName.text.toString().ifEmpty {
            viewBinding.includeAllName.allName.setText("全部")
            "全部"
        }

        val hideCamera = viewBinding.includeBool.hideCamera.isChecked
        val radio = viewBinding.includeBool.radio.isChecked
        val crop = viewBinding.includeBool.crop.isChecked
        val takePictureCrop = viewBinding.includeBool.takePictureCrop.isChecked

        val tempConfigs = GalleryConfigs(
            selects = select,
            maxCount = maxCount,
            hideCamera = hideCamera,
            radio = radio,
            crop = crop,
            takePictureCrop = takePictureCrop,
            relativePath = Environment.DIRECTORY_PICTURES,
            picturePathAndCropPath = Environment.DIRECTORY_PICTURES to Environment.DIRECTORY_PICTURES,
            type = scanArray,
            sdNameAndAllName = sdName to allName,
            cropName = cropName to "jpg",
            sort = sort to MediaStore.Files.FileColumns.DATE_MODIFIED,
        )

        val galleryConfigs = tempConfigs.copy(
            cameraConfig = CameraConfig(
                checkBoxIcon = checkBoxRes,
                text = if (tempConfigs.isScanVideoMedia) "摄像" else "拍照",
                iconColor = viewBinding.includeCamera.cameraIconColor.currentTextColor,
                textColor = viewBinding.includeCamera.cameraIconColor.currentTextColor,
                bg = viewBinding.includeCamera.cameraBgColor.currentTextColor,
            ),
            gridConfig = GridConfig(spanCount = spanCount, orientation = orientation),
            cameraName = takeName to if (tempConfigs.isScanVideoMedia) "mp4" else "jpg",
        )

        viewBinding.includeBool.hideCamera.isChecked = false
        viewBinding.includeBool.radio.isChecked = false
        viewBinding.includeBool.crop.isChecked = false
        viewBinding.includeBool.takePictureCrop.isChecked = false
        viewBinding.includeBool.customCamera.isChecked = false

        return galleryConfigs
    }

}