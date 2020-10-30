package com.gallery.sample.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.kotlin.expand.os.getParcelableOrDefault
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.finder.BaseFinderAdapter
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.sample.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.gallery_finder_bottom.*
import kotlinx.android.synthetic.main.gallery_finder_bottom_item.*

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

        private val list: ArrayList<ScanEntity> = arrayListOf()
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
            galleryFinderBottom.adapter = object : RecyclerView.Adapter<ViewHolder>() {

                override fun getItemCount(): Int = list.size

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.gallery_finder_bottom_item, parent, false)).apply {
                        this.itemView.setOnClickListener {
                            adapterFinderListener.onGalleryAdapterItemClick(it, this.bindingAdapterPosition, list[this.bindingAdapterPosition])
                        }
                    }
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val finderEntity: ScanEntity = list[position]
                    holder.tvGalleryFinderNameBt.text = "%s".format(finderEntity.bucketDisplayName)
                    holder.tvGalleryFinderNameBt.setTextColor(galleryUiBundle.finderItemTextColor)
                    holder.tvGalleryFinderFileCountBt.text = "%s".format(finderEntity.count.toString())
                    holder.tvGalleryFinderFileCountBt.setTextColor(galleryUiBundle.finderItemTextCountColor)
                    adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.ivGalleryFinderIconBt)
                }
            }
        }

        fun updateFinder(entities: ArrayList<ScanEntity>) {
            list.clear()
            list.addAll(entities)
            galleryFinderBottom?.adapter?.notifyDataSetChanged()
        }

        @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
            override val containerView: View
                get() = itemView
        }

    }
}
