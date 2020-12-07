package com.gallery.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.*
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_simple_scan.*
import kotlinx.android.synthetic.main.item_simple_scan.*

class SimpleScanActivity : AppCompatActivity(R.layout.activity_simple_scan) {

    companion object {
        const val args = "args"
    }

    private val arrayList: ArrayList<SimpleEntity> = arrayListOf()

    @ContainerOptions(cache = CacheImplementation.SPARSE_ARRAY)
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LayoutContainer {
        override val containerView: View
            get() = itemView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scanType = intent.getSerializableExtra(args) as ScanType
        recyclerview.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_simple_scan, parent, false))
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val simpleEntity = arrayList[position]
                holder.text.text = "id:${simpleEntity.id} \nname:${simpleEntity.name} \nmediaType:${simpleEntity.mediaType}"
            }

            override fun getItemCount(): Int = arrayList.size
        }

        when (scanType) {
            ScanType.FILE -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(null)))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.MIX -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()))))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            ScanType.AUDIO -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.audioExpand(),
                    args = ScanAudioArgs()))
                    .scanAudioImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "音频") })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.PICTURE -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.pictureExpand(),
                    args = ScanPictureArgs()))
                    .scanPictureImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "图片") })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            // old
            ScanType.VIDEO -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()))))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
        }
    }

    class SimpleEntity(val id: String, val name: String, val mediaType: String)

}