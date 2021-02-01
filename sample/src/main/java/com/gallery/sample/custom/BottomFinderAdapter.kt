package com.gallery.sample.custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gallery.compat.GalleryUiBundle
import com.gallery.compat.finder.BaseFinderAdapter
import com.gallery.compat.finder.GalleryFinderAdapter
import com.gallery.core.entity.ScanEntity
import com.gallery.compat.extensions.getParcelableOrDefault
import com.gallery.sample.databinding.GalleryFinderBottomBinding
import com.gallery.sample.databinding.GalleryFinderBottomItemBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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
            arguments.getParcelableOrDefault<GalleryUiBundle>(Key, GalleryUiBundle())
        }
        private val adapterFinderListener: GalleryFinderAdapter.AdapterFinderListener by lazy {
            requireActivity() as GalleryFinderAdapter.AdapterFinderListener
        }
        private val viewBinding: GalleryFinderBottomBinding by lazy {
            GalleryFinderBottomBinding.inflate(layoutInflater)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return viewBinding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            view.setBackgroundColor(galleryUiBundle.finderItemBackground)
            viewBinding.galleryFinderBottom.layoutManager = LinearLayoutManager(requireContext())
            viewBinding.galleryFinderBottom.adapter = object : RecyclerView.Adapter<ViewHolder>() {

                override fun getItemCount(): Int = list.size

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                    return ViewHolder(GalleryFinderBottomItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)).apply {
                        this.itemView.setOnClickListener {
                            adapterFinderListener.onGalleryAdapterItemClick(it, this.bindingAdapterPosition, list[this.bindingAdapterPosition])
                        }
                    }
                }

                override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                    val finderEntity: ScanEntity = list[position]
                    holder.viewBinding.tvGalleryFinderNameBt.text = "%s".format(finderEntity.bucketDisplayName)
                    holder.viewBinding.tvGalleryFinderNameBt.setTextColor(galleryUiBundle.finderItemTextColor)
                    holder.viewBinding.tvGalleryFinderFileCountBt.text = "%s".format(finderEntity.count.toString())
                    holder.viewBinding.tvGalleryFinderFileCountBt.setTextColor(galleryUiBundle.finderItemTextCountColor)
                    adapterFinderListener.onGalleryFinderThumbnails(finderEntity, holder.viewBinding.ivGalleryFinderIconBt)
                }
            }
        }

        fun updateFinder(entities: ArrayList<ScanEntity>) {
            list.clear()
            list.addAll(entities)
            if (isAdded) {
                viewBinding.galleryFinderBottom.adapter?.notifyDataSetChanged()
            }
        }

        class ViewHolder(val viewBinding: GalleryFinderBottomItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    }
}
