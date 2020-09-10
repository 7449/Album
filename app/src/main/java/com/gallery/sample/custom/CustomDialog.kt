package com.gallery.sample.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.fragment.app.DialogFragment
import androidx.kotlin.expand.text.safeToastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryCallback
import com.gallery.core.ui.fragment.ScanFragment
import com.gallery.sample.R
import com.gallery.scan.ScanEntity

class CustomDialog : DialogFragment(), IGalleryCallback {

    companion object {
        fun newInstance(): CustomDialog {
            return CustomDialog()
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog?.window
        window ?: return
        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.BOTTOM
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = params
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = inflater.inflate(R.layout.dialog_gallery, container, false)
        slideToUp(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.findFragmentByTag(ScanFragment::class.java.simpleName)?.let {
            childFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: childFragmentManager
                .beginTransaction()
                .add(R.id.galleryFragment, ScanFragment.newInstance(GalleryBundle(radio = true, crop = false, hideCamera = true)), ScanFragment::class.java.simpleName)
                .commitAllowingStateLoss()
    }

    private fun slideToUp(view: View) {
        val slide: Animation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        slide.duration = 400
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)
        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        scanEntity.toString().safeToastExpand(requireActivity())
        dismiss()
    }

    override fun onPhotoItemClick(context: Context, galleryBundle: GalleryBundle, scanEntity: ScanEntity, position: Int, parentId: Long) {
    }
}