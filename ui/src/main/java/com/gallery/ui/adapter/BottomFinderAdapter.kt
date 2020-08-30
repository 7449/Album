package com.gallery.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.kotlin.expand.os.getParcelableOrDefault
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.ScanEntity
import com.gallery.ui.GalleryUiBundle
import com.gallery.ui.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xadapter.vh.LayoutViewHolder
import com.xadapter.vh.XViewHolder
import kotlinx.android.synthetic.main.gallery_finder_bottom.*

class BottomFinderAdapter : BaseFinderAdapter() {

    companion object {
        internal const val Key = "uiFinderArgs"
    }

    private val bottomFragment: BottomFragment by lazy { BottomFragment.newInstance(uiBundle) }

    override fun show() {
        bottomFragment.show(activity.supportFragmentManager, BottomFragment::class.java.simpleName)
    }

    override fun hide() {
        bottomFragment.dismiss()
    }

    override fun finderUpdate(finderList: ArrayList<ScanEntity>) {
        bottomFragment.updateFinder(finderList)
    }

    class BottomFragment : BottomSheetDialogFragment() {

        companion object {
            fun newInstance(galleryUiBundle: GalleryUiBundle): BottomFragment {
                val bundle = Bundle()
                bundle.putParcelable(Key, galleryUiBundle)
                return BottomFragment().apply {
                    arguments = bundle
                }
            }
        }

        private val list: ArrayList<ScanEntity> = ArrayList()
        private val galleryUiBundle by lazy {
            getParcelableOrDefault<GalleryUiBundle>(Key, GalleryUiBundle())
        }
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener by lazy {
            requireActivity() as GalleryFinderAdapter.AdapterFinderListener
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.gallery_finder_bottom, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.setBackgroundColor(galleryUiBundle.finderItemBackground)
            galleryFinderBottom.layoutManager = LinearLayoutManager(requireContext())
            galleryFinderBottom.adapter = object : RecyclerView.Adapter<XViewHolder>() {
                override fun getItemCount(): Int = list.size

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): XViewHolder {
                    return LayoutViewHolder(parent, R.layout.gallery_finder_bottom_item).apply {
                        this.itemView.setOnClickListener {
                            adapterFinderListener.onGalleryAdapterItemClick(it, this.bindingAdapterPosition, list[this.bindingAdapterPosition])
                        }
                    }
                }

                override fun onBindViewHolder(holder: XViewHolder, position: Int) {
                    val finderEntity: ScanEntity = list[position]
                    holder.setText(R.id.tv_gallery_finder_name, "%s".format(finderEntity.bucketDisplayName))
                    holder.textView(R.id.tv_gallery_finder_name).setTextColor(galleryUiBundle.finderItemTextColor)
                    holder.setText(R.id.tv_gallery_finder_file_count, "%s".format(finderEntity.count.toString()))
                    holder.textView(R.id.tv_gallery_finder_file_count).setTextColor(galleryUiBundle.finderItemTextCountColor)
                    adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.frameLayout(R.id.iv_gallery_finder_icon))
                }
            }
        }

        fun updateFinder(entities: ArrayList<ScanEntity>) {
            list.clear()
            list.addAll(entities)
            galleryFinderBottom?.adapter?.notifyDataSetChanged()
        }

    }
}
