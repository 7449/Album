package com.gallery.sample

import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gallery.sample.enums.ScanType
import com.gallery.sample.enums.ScanType.*
import com.gallery.scan.ScanViewModelFactory
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.args.audio.ScanAudioArgs
import com.gallery.scan.args.audio.audioExpand
import com.gallery.scan.args.file.ScanFileArgs
import com.gallery.scan.args.file.fileExpand
import com.gallery.scan.args.file.multipleFileExpand
import com.gallery.scan.scanAudioImpl
import com.gallery.scan.scanFileImpl
import com.gallery.scan.types.SCAN_ALL
import com.gallery.scan.types.registerMultipleLiveData
import com.xadapter.vh.LayoutViewHolder
import kotlinx.android.synthetic.main.activity_simple_scan.*

class SimpleScanActivity : AppCompatActivity(R.layout.activity_simple_scan) {

    companion object {
        const val args = "args"
    }

    private val arrayList: ArrayList<SimpleEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scanType = intent.getSerializableExtra(args) as ScanType
        recyclerview.adapter = object : RecyclerView.Adapter<LayoutViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayoutViewHolder {
                return LayoutViewHolder(parent, R.layout.item_simple_scan)
            }

            override fun onBindViewHolder(holder: LayoutViewHolder, position: Int) {
                val simpleEntity = arrayList[position]
                holder.setText(R.id.text, "id:${simpleEntity.id} \nname:${simpleEntity.name} \nmediaType:${simpleEntity.mediaType}")
            }

            override fun getItemCount(): Int = arrayList.size
        }

        when (scanType) {
            FILE -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(null)))
                    .scanFileImpl()
                    .registerMultipleLiveData(this) { _, result ->
                        arrayList.addAll(result.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            AUDIO -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.audioExpand(),
                    args = ScanAudioArgs()))
                    .scanAudioImpl()
                    .registerMultipleLiveData(this) { _, result ->
                        arrayList.addAll(result.map { SimpleEntity(it.id.toString(), it.displayName, "音频") })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            IMAGE -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()))))
                    .scanFileImpl()
                    .registerMultipleLiveData(this) { _, result ->
                        arrayList.addAll(result.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(SCAN_ALL.multipleFileExpand())
            VIDEO -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()))))
                    .scanFileImpl()
                    .registerMultipleLiveData(this) { _, result ->
                        arrayList.addAll(result.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(SCAN_ALL.multipleFileExpand())
            MIX -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()))))
                    .scanFileImpl()
                    .registerMultipleLiveData(this) { _, result ->
                        arrayList.addAll(result.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(SCAN_ALL.multipleFileExpand())
        }
    }

    class SimpleEntity(val id: String, val name: String, val mediaType: String)

}