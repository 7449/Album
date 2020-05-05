package com.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.activity.GalleryBaseActivity

class PopupFinderAdapter : GalleryFinderAdapter, AdapterView.OnItemClickListener {

    private lateinit var popupWindow: ListPopupWindow
    private lateinit var adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener
    private val galleryUiBundle: GalleryUiBundle by lazy { adapterFinderListener.adapterGalleryUiBundle }
    private val finderAdapter: FinderAdapter by lazy {
        FinderAdapter(galleryUiBundle) { finderEntity, container ->
            adapterFinderListener.onGalleryFinderThumbnails(finderEntity, container)
        }
    }

    override fun onGalleryFinderInit(context: GalleryBaseActivity, anchor: View?) {
        popupWindow = ListPopupWindow(context).apply {
            this.anchorView = anchor
            this.width = galleryUiBundle.listPopupWidth
            this.horizontalOffset = galleryUiBundle.listPopupHorizontalOffset
            this.verticalOffset = galleryUiBundle.listPopupVerticalOffset
            this.isModal = true
            this.setOnItemClickListener(this@PopupFinderAdapter)
            this.setAdapter(finderAdapter)
        }
    }

    override fun onGalleryFinderShow() {
        popupWindow.show()
        popupWindow.listView?.setBackgroundColor(galleryUiBundle.finderItemBackground)
    }

    override fun onGalleryFinderHide() {
        popupWindow.dismiss()
    }

    override fun onGalleryFinderUpdate(finderList: ArrayList<ScanEntity>) {
        finderAdapter.updateFinder(finderList)
    }

    override fun setOnAdapterFinderListener(adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener) {
        this.adapterFinderListener = adapterFinderListener
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        adapterFinderListener.onGalleryAdapterItemClick(view, position, finderAdapter.getItem(position))
    }

    private class FinderAdapter(private val galleryUiBundle: GalleryUiBundle, private val displayFinder: (finderEntity: ScanEntity, container: FrameLayout) -> Unit) : BaseAdapter() {

        private val list: ArrayList<ScanEntity> = ArrayList()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val finderEntity = getItem(position)
            val viewHolder: ViewHolder
            val rootView = if (convertView == null) {
                LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_finder, parent, false).apply {
                    viewHolder = ViewHolder(this)
                    this.tag = viewHolder
                }
            } else {
                viewHolder = convertView.tag as ViewHolder
                convertView
            }
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

        private class ViewHolder(view: View) {
            val frameLayout: FrameLayout = view.findViewById(R.id.iv_gallery_finder_icon)
            val appCompatTextView: AppCompatTextView = view.findViewById(R.id.tv_gallery_finder_name)
            val appCompatTextViewCount: AppCompatTextView = view.findViewById(R.id.tv_gallery_finder_file_count)
        }
    }
}