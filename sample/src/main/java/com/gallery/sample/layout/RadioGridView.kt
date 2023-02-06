package com.gallery.sample.layout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gallery.sample.databinding.SimpleLayoutGalleryRadioViewBinding
import com.gallery.sample.databinding.SimpleLayoutGalleryRaidoItemViewBinding
import com.yalantis.ucrop.UCrop
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.args.GridConfig
import develop.file.gallery.entity.ScanEntity
import develop.file.gallery.extensions.ResultCompat.toScanEntity
import develop.file.media.Types
import develop.file.media.args.MediaResult
import develop.file.media.extensions.media
import develop.file.media.impl.MediaImpl
import develop.file.media.impl.file.FileMediaEntity
import java.io.File

class RadioGridView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val viewBinding: SimpleLayoutGalleryRadioViewBinding =
        SimpleLayoutGalleryRadioViewBinding.inflate(LayoutInflater.from(getContext()), this, true)

    private val orientation = RecyclerView.VERTICAL
    private val spanCount = 3
    private val configs = createGalleryConfigs()
    private val mAdapter = RadioAdapter(arrayListOf())

    private fun createGalleryConfigs(): GalleryConfigs {
        return GalleryConfigs(
            hideCamera = false,
            radio = true,
            crop = false,
            takePictureCrop = false,
            gridConfig = GridConfig(spanCount, orientation)
        )
    }

    private val fileMediaArgs = (context as FragmentActivity).media(configs.fileMediaArgs)
    private val mediaScan: MediaImpl<FileMediaEntity> = MediaImpl(fileMediaArgs) {
        when (this) {
            is MediaResult.Multiple -> mAdapter.items.addAll(multipleValue.toScanEntity())
            is MediaResult.Single -> TODO()
        }
    }
    private val cropLauncher: ActivityResultLauncher<Intent> =
        (context as FragmentActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = UCrop.getOutput(requireNotNull(it.data))
                Toast.makeText(context, resultUri.toString(), Toast.LENGTH_SHORT).show()
            }
        }

    init {
        viewBinding.recyclerView.layoutManager = configs.layoutManager(context)
        viewBinding.recyclerView.adapter = mAdapter
        mediaScan.multiple(Types.Id.ALL)
    }

    private inner class RadioAdapter(val items: ArrayList<ScanEntity>) :
        RecyclerView.Adapter<RadioAdapter.ViewHolder>() {

        private inner class ViewHolder(val viewBinding: SimpleLayoutGalleryRaidoItemViewBinding) :
            RecyclerView.ViewHolder(viewBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                SimpleLayoutGalleryRaidoItemViewBinding.inflate(
                    LayoutInflater.from(parent.context)
                )
            ).apply {
                itemView.setOnClickListener {
                    val bindingAdapterPosition = bindingAdapterPosition
                    if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                    val scanEntity = items[bindingAdapterPosition]
                    cropLauncher.launch(
                        UCrop.of(
                            scanEntity.uri,
                            Uri.fromFile(
                                File(
                                    it.context.cacheDir,
                                    "ucrop_${scanEntity.bucketDisplayName}_${System.currentTimeMillis()}.jpg"
                                )
                            )
                        ).getIntent(context)
                    )
                }
            }
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide
                .with(holder.itemView.context)
                .load(items[position].uri)
                .apply(RequestOptions().centerCrop())
                .into(holder.viewBinding.radioView)
        }
    }

}