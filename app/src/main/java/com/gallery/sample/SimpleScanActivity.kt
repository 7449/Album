package com.gallery.sample

import android.os.Bundle
import android.provider.MediaStore
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.*
import com.xadapter.vh.LayoutViewHolder
import kotlinx.android.synthetic.main.activity_simple_scan.*

class SimpleScanActivity : AppCompatActivity(R.layout.activity_simple_scan) {

    companion object {
        const val args = "args"
    }

    private val arrayList: ArrayList<SimpleEntity> = arrayListOf()

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
            ScanType.FILE -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(null)))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.MIX -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.fileExpand(),
                    args = ScanFileArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()))))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            ScanType.AUDIO -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.audioExpand(),
                    args = ScanAudioArgs()))
                    .scanAudioImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "音频") })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.PICTURE -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
                    factory = ScanEntityFactory.pictureExpand(),
                    args = ScanPictureArgs()))
                    .scanPictureImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "图片") })
                        recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            // old
            ScanType.VIDEO -> ViewModelProvider(this, ScanViewModelFactory(ownerActivity = this,
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