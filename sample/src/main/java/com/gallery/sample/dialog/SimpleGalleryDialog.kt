package com.gallery.sample.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.compat.fragment.GalleryCompatFragment
import com.gallery.compat.internal.simple.SimpleGalleryCallback
import com.gallery.compat.widget.GalleryImageView
import com.gallery.core.GalleryBundle
import com.gallery.core.callback.IGalleryImageLoader
import com.gallery.core.delegate.IScanDelegate
import com.gallery.core.entity.ScanEntity
import com.gallery.core.extensions.safeToastExpand
import com.gallery.sample.R

class SimpleGalleryDialog : DialogFragment(), SimpleGalleryCallback, IGalleryImageLoader {

    companion object {
        fun newInstance(): SimpleGalleryDialog {
            return SimpleGalleryDialog()
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = inflater.inflate(R.layout.simple_dialog_gallery, container, false)
        slideToUp(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        childFragmentManager.findFragmentByTag(GalleryCompatFragment::class.java.simpleName)?.let {
            childFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
        } ?: childFragmentManager
            .beginTransaction()
            .add(
                R.id.galleryFragment,
                GalleryCompatFragment.newInstance(
                    GalleryBundle(
                        radio = true,
                        crop = false,
                        hideCamera = true
                    )
                ),
                GalleryCompatFragment::class.java.simpleName
            )
            .commitAllowingStateLoss()
    }

    private fun slideToUp(view: View) {
        val slide: Animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        )
        slide.duration = 400
        slide.fillAfter = true
        slide.isFillEnabled = true
        view.startAnimation(slide)
    }

    override fun onGalleryResource(context: Context, scanEntity: ScanEntity) {
        scanEntity.toString().safeToastExpand(requireActivity())
        dismiss()
    }

    override fun onGalleryCreated(
        delegate: IScanDelegate,
        bundle: GalleryBundle,
        savedInstanceState: Bundle?
    ) {
        delegate.rootView.setBackgroundColor(Color.BLACK)
    }

    override fun onDisplayGallery(
        width: Int,
        height: Int,
        scanEntity: ScanEntity,
        container: FrameLayout,
        checkBox: TextView
    ) {
        container.removeAllViews()
        val imageView = GalleryImageView(container.context)
        Glide.with(container.context)
            .load(scanEntity.uri)
            .apply(
                RequestOptions()
                    .centerCrop()
                    .override(width, height)
            )
            .into(imageView)
        container.addView(imageView, FrameLayout.LayoutParams(width, height))
    }

}