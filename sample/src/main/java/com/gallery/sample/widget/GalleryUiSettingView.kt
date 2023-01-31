package com.gallery.sample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.gallery.sample.clickShowColorPicker
import com.gallery.sample.databinding.SimpleLayoutGalleryUiSettingBinding
import com.gallery.ui.material.args.MaterialGalleryConfig

class GalleryUiSettingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: SimpleLayoutGalleryUiSettingBinding =
        SimpleLayoutGalleryUiSettingBinding.inflate(LayoutInflater.from(getContext()), this, true)

    fun createGalleryUiConfig(): MaterialGalleryConfig {
        return MaterialGalleryConfig(
            toolbarTextColor = viewBinding.toolbarTextColor.currentTextColor,
            toolbarIconColor = viewBinding.toolbarBackIconColor.currentTextColor,
            toolbarBackground = viewBinding.toolbarBgColor.currentTextColor,
            statusBarColor = viewBinding.statusBarColor.currentTextColor,
            galleryRootBackground = viewBinding.rootViewBgColor.currentTextColor,
            finderTextColor = viewBinding.galleryBottomFinderTextColor.currentTextColor,
            finderIconColor = viewBinding.galleryBottomFinderIconColor.currentTextColor,
            preViewTextColor = viewBinding.galleryBottomPrevTextColor.currentTextColor,
            selectTextColor = viewBinding.galleryBottomSelectTextColor.currentTextColor,
            bottomViewBackground = viewBinding.galleryBottomRootBgColor.currentTextColor,
            finderItemTextColor = viewBinding.finderItemTextColor.currentTextColor,
            finderItemTextCountColor = viewBinding.finderItemTextCountColor.currentTextColor,
            finderItemBackground = viewBinding.finderItemBgColor.currentTextColor,
            prevRootBackground = viewBinding.prevRootBgColor.currentTextColor,
            preBottomViewBackground = viewBinding.prevRootBottomBgColor.currentTextColor,
            preBottomOkTextColor = viewBinding.prevRootBottomOkColor.currentTextColor,
            preBottomCountTextColor = viewBinding.prevRootBottomCountColor.currentTextColor,
        )
    }

    init {
        viewBinding.toolbarTextColor.clickShowColorPicker()
        viewBinding.toolbarBackIconColor.clickShowColorPicker()
        viewBinding.toolbarBgColor.clickShowColorPicker()
        viewBinding.statusBarColor.clickShowColorPicker()
        viewBinding.rootViewBgColor.clickShowColorPicker()
        viewBinding.galleryBottomFinderTextColor.clickShowColorPicker()
        viewBinding.galleryBottomFinderIconColor.clickShowColorPicker()
        viewBinding.galleryBottomPrevTextColor.clickShowColorPicker()
        viewBinding.galleryBottomSelectTextColor.clickShowColorPicker()
        viewBinding.galleryBottomRootBgColor.clickShowColorPicker()
        viewBinding.finderItemBgColor.clickShowColorPicker()
        viewBinding.finderItemTextColor.clickShowColorPicker()
        viewBinding.finderItemTextCountColor.clickShowColorPicker()
        viewBinding.prevRootBgColor.clickShowColorPicker()
        viewBinding.prevRootBottomBgColor.clickShowColorPicker()
        viewBinding.prevRootBottomOkColor.clickShowColorPicker()
        viewBinding.prevRootBottomCountColor.clickShowColorPicker()
    }

}