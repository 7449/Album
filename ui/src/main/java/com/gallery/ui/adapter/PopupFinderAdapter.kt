package com.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.ListPopupWindow
import com.gallery.core.delegate.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.gallery.ui.base.adapter.BaseFinderAdapter

class PopupFinderAdapter : BaseFinderAdapter(), AdapterView.OnItemClickListener {

    private val popupWindow: ListPopupWindow by lazy {
        ListPopupWindow(activity).apply {
            this.anchorView = viewAnchor
            this.width = uiBundle.listPopupWidth
            this.horizontalOffset = uiBundle.listPopupHorizontalOffset
            this.verticalOffset = uiBundle.listPopupVerticalOffset
            this.isModal = true
            this.setOnItemClickListener(this@PopupFinderAdapter)
            this.setAdapter(finderAdapter)
        }
    }
    private val finderAdapter: FinderAdapter by lazy {
        FinderAdapter(uiBundle) { finderEntity, container ->
            listener.onGalleryFinderThumbnails(finderEntity, container)
        }
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
        listener.onGalleryAdapterItemClick(view, position, finderAdapter.getItem(position))
    }

    private class FinderAdapter(private val galleryUiBundle: GalleryUiBundle, private val displayFinder: (finderEntity: ScanEntity, container: FrameLayout) -> Unit) : BaseAdapter() {

        private val list: ArrayList<ScanEntity> = ArrayList()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val finderEntity: ScanEntity = getItem(position)
            val rootView: View = convertView
                    ?: LayoutInflater.from(parent.context).inflate(R.layout.gallery_item_finder, parent, false).apply {
                        this.tag = ViewHolder(this)
                    }
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

        private class ViewHolder(view: View) {
            val frameLayout: FrameLayout = view.findViewById(R.id.iv_gallery_finder_icon)
            val appCompatTextView: AppCompatTextView = view.findViewById(R.id.tv_gallery_finder_name)
            val appCompatTextViewCount: AppCompatTextView = view.findViewById(R.id.tv_gallery_finder_file_count)
        }
    }
}