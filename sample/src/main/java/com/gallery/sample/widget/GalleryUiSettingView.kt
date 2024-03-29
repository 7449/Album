package com.gallery.sample.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.gallery.sample.clickSelectIcon
import com.gallery.sample.clickShowColorPicker
import com.gallery.sample.databinding.SimpleLayoutGalleryUiSettingBinding
import com.gallery.sample.showCompoundDrawables
import com.gallery.sample.toIntOrNull
import develop.file.gallery.ui.material.args.MaterialGalleryConfig
import develop.file.gallery.ui.material.args.MaterialTextConfig

@SuppressLint("NonConstantResourceId")
class GalleryUiSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: SimpleLayoutGalleryUiSettingBinding =
        SimpleLayoutGalleryUiSettingBinding.inflate(LayoutInflater.from(getContext()), this, true)

    companion object {
        private const val DEFAULT_TEXT_SIZE_MAX = 16F
        private const val DEFAULT_TEXT_SIZE_MIN = 12F
        private const val DEFAULT_TOOLBAR_TEXT = "图片选择"
        private const val DEFAULT_TOOLBAR_ELEVATION = 4F
        private val DEFAULT_TOOLBAR_BACK_ICON =
            com.gallery.ui.material.R.drawable.ic_material_gallery_back
        private val DEFAULT_HOME_FINDER_ICON =
            com.gallery.ui.material.R.drawable.ic_material_gallery_finder
        private const val DEFAULT_HOME_FINDER_PREV_TEXT = "预览"
        private const val DEFAULT_HOME_FINDER_OK_TEXT = "确定"
        private const val DEFAULT_HOME_FINDER_WIDTH = 600
    }

    init {
        viewBinding.includeHome.statusBarColor.clickShowColorPicker()
        viewBinding.includeHome.toolbarBgColor.clickShowColorPicker()
        viewBinding.includeHome.toolbarTextColor.clickShowColorPicker()
        viewBinding.includeHome.rootViewBgColor.clickShowColorPicker()
        viewBinding.includeHome.toolbarBackIcon.clickSelectIcon()
        viewBinding.includeHome.toolbarBackIcon.showCompoundDrawables(DEFAULT_TOOLBAR_BACK_ICON)
        viewBinding.includeHome.finderBgColor.clickShowColorPicker()
        viewBinding.includeHome.finderIcon.clickSelectIcon()
        viewBinding.includeHome.finderIcon.showCompoundDrawables(DEFAULT_HOME_FINDER_ICON)

        viewBinding.includeHomeBottom.finderBtnTextColor.clickShowColorPicker()
        viewBinding.includeHomeBottom.finderPrevTextColor.clickShowColorPicker()
        viewBinding.includeHomeBottom.finderOkTextColor.clickShowColorPicker()

        viewBinding.includeHomeFinder.finderItemBgColor.clickShowColorPicker()
        viewBinding.includeHomeFinder.finderItemTextColor.clickShowColorPicker()

        viewBinding.includePrevBottom.prevBottomSelectCountTextColor.clickShowColorPicker()
        viewBinding.includePrevBottom.prevBottomSelectTextColor.clickShowColorPicker()
    }

    private fun EditText.getTextSizeSupport(): Float {
        var textSize = text.toString().toFloatOrNull() ?: DEFAULT_TEXT_SIZE_MAX
        if (textSize < DEFAULT_TEXT_SIZE_MIN || textSize > DEFAULT_TEXT_SIZE_MAX) {
            textSize = DEFAULT_TEXT_SIZE_MAX
            setText(textSize.toString())
        }
        return textSize
    }

    private fun EditText.getTextSupport(default: String): String {
        return text.toString().ifEmpty {
            setText(default)
            getTextSupport(default)
        }
    }

    private fun Button.getIconSupport(default: Int): Int {
        return tag.toIntOrNull {
            tag = default
            getIconSupport(default)
        }
    }

    private fun getToolbarElevation(): Float {
        val elevationView = viewBinding.includeHome.toolbarElevationEt
        var elevation = elevationView.text.toString().toFloatOrNull() ?: DEFAULT_TOOLBAR_ELEVATION
        if (elevation < DEFAULT_TOOLBAR_ELEVATION) {
            elevation = DEFAULT_TOOLBAR_ELEVATION
            elevationView.setText(elevation.toString())
        }
        return elevation
    }

    private fun getHomeFinderWidth(): Int {
        val elevationView = viewBinding.includeHomeFinder.finderItemWidth
        return elevationView.text.toIntOrNull {
            elevationView.setText(DEFAULT_HOME_FINDER_WIDTH.toString())
            getHomeFinderWidth()
        }
    }

    fun createGalleryUiConfig(): MaterialGalleryConfig {
        return MaterialGalleryConfig(
            toolbarTextConfig = MaterialTextConfig(
                text = viewBinding.includeHome.toolbarTitleEt.getTextSupport(
                    DEFAULT_TOOLBAR_TEXT
                ),
                textColor = viewBinding.includeHome.toolbarTextColor.currentTextColor
            ),
            toolbarElevation = getToolbarElevation(),
            toolbarIcon = viewBinding.includeHome.toolbarBackIcon.getIconSupport(
                DEFAULT_TOOLBAR_BACK_ICON
            ),
            toolbarBackground = viewBinding.includeHome.toolbarBgColor.currentTextColor,
            statusBarColor = viewBinding.includeHome.statusBarColor.currentTextColor,
            galleryRootBackground = viewBinding.includeHome.rootViewBgColor.currentTextColor,
            bottomViewBackground = viewBinding.includeHome.finderBgColor.currentTextColor,
            finderTextConfig = MaterialTextConfig(
                textSize = viewBinding.includeHomeBottom.finderBtnTextSize.getTextSizeSupport(),
                textColor = viewBinding.includeHomeBottom.finderBtnTextColor.currentTextColor
            ),
            finderIcon = viewBinding.includeHome.finderIcon.getIconSupport(
                DEFAULT_HOME_FINDER_ICON
            ),
            prevTextConfig = MaterialTextConfig(
                viewBinding.includeHomeBottom.finderPrevText.getTextSupport(
                    DEFAULT_HOME_FINDER_PREV_TEXT
                ),
                viewBinding.includeHomeBottom.finderPrevTextSize.getTextSizeSupport(),
                viewBinding.includeHomeBottom.finderPrevTextColor.currentTextColor
            ),
            selectTextConfig = MaterialTextConfig(
                viewBinding.includeHomeBottom.finderOkText.getTextSupport(
                    DEFAULT_HOME_FINDER_OK_TEXT
                ),
                viewBinding.includeHomeBottom.finderOkTextSize.getTextSizeSupport(),
                viewBinding.includeHomeBottom.finderOkTextColor.currentTextColor
            ),
            listPopupWidth = getHomeFinderWidth(),
            listPopupHorizontalOffset = viewBinding.includeHomeFinder.finderItemHorOffset.getTextSizeSupport()
                .toInt(),
            listPopupVerticalOffset = viewBinding.includeHomeFinder.finderItemVerOffset.getTextSizeSupport()
                .toInt(),
            finderItemBackground = viewBinding.includeHomeFinder.finderItemBgColor.currentTextColor,
            finderItemTextColor = viewBinding.includeHomeFinder.finderItemTextColor.currentTextColor,
            preBottomOkConfig = MaterialTextConfig(
                viewBinding.includePrevBottom.prevBottomSelectText.getTextSupport(
                    DEFAULT_HOME_FINDER_OK_TEXT
                ),
                viewBinding.includePrevBottom.prevBottomSelectTextSize.getTextSizeSupport(),
                viewBinding.includePrevBottom.prevBottomSelectTextColor.currentTextColor,
            ),
            preBottomCountConfig = MaterialTextConfig(
                textColor = viewBinding.includePrevBottom.prevBottomSelectCountTextColor.currentTextColor,
                textSize = viewBinding.includePrevBottom.prevBottomSelectCountTextSize.getTextSizeSupport()
            )
        )
    }

}