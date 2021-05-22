package com.gallery.ui.finder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.activity.GalleryCompatActivity
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.ui.databinding.GalleryItemFinderBinding

class PopupFinderAdapter(
    private val activity: GalleryCompatActivity,
    private val viewAnchor: View,
    private val uiBundle: GalleryUiBundle,
    private val finderListener: GalleryFinderAdapter.AdapterFinderListener
) : GalleryFinderAdapter, AdapterView.OnItemClickListener {

    private val finderAdapter: FinderAdapter = FinderAdapter(uiBundle) { finderEntity, container ->
        finderListener.onGalleryFinderThumbnails(finderEntity, container)
    }
    private val popupWindow: ListPopupWindow = ListPopupWindow(activity).apply {
        this.anchorView = viewAnchor
        this.width = uiBundle.listPopupWidth
        this.horizontalOffset = uiBundle.listPopupHorizontalOffset
        this.verticalOffset = uiBundle.listPopupVerticalOffset
        this.isModal = true
        this.setOnItemClickListener(this@PopupFinderAdapter)
        this.setAdapter(finderAdapter)
    }

    init {
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (source.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                    activity.lifecycle.removeObserver(this)
                    if (popupWindow.isShowing) {
                        popupWindow.dismiss()
                    }
                }
            }
        })
    }

    override fun show() {
        popupWindow.show()
        popupWindow.listView?.setBackgroundColor(uiBundle.finderItemBackground)
    }

    override fun hide() {
        popupWindow.dismiss()
    }

    override fun finderUpdate(finderList: ArrayList<ScanEntity>) {
        finderAdapter.updateFinder(finderList)
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        finderListener.onGalleryAdapterItemClick(view, position, finderAdapter.getItem(position))
    }

    private class FinderAdapter(
        private val galleryUiBundle: GalleryUiBundle,
        private val displayFinder: (finderEntity: ScanEntity, container: FrameLayout) -> Unit
    ) : BaseAdapter() {

        private val list: ArrayList<ScanEntity> = arrayListOf()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val finderEntity: ScanEntity = getItem(position)
            val rootView: View = convertView
                ?: GalleryItemFinderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).apply {
                    this.root.tag = ViewHolder(this)
                }.root
            val viewHolder: ViewHolder = rootView.tag as ViewHolder
            viewHolder.appCompatTextView.setTextColor(galleryUiBundle.finderItemTextColor)
            viewHolder.appCompatTextView.text = "%s".format(finderEntity.bucketDisplayName)
            viewHolder.appCompatTextViewCount.setTextColor(galleryUiBundle.finderItemTextCountColor)
            viewHolder.appCompatTextViewCount.text = "%s".format(finderEntity.count.toString())
            displayFinder.invoke(finderEntity, viewHolder.frameLayout)
            return rootView
        }

        override fun getItem(position: Int): ScanEntity = list[position]
        override fun getItemId(position: Int): Long = position.toLong()
        override fun getCount(): Int = list.size

        fun updateFinder(entities: ArrayList<ScanEntity>) {
            list.clear()
            list.addAll(entities)
            notifyDataSetChanged()
        }

        private class ViewHolder(viewBinding: GalleryItemFinderBinding) {
            val frameLayout: FrameLayout = viewBinding.ivGalleryFinderIcon
            val appCompatTextView: AppCompatTextView = viewBinding.tvGalleryFinderName
            val appCompatTextViewCount: AppCompatTextView = viewBinding.tvGalleryFinderFileCount
        }
    }
}