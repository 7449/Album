package com.gallery.sample

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.gallery.sample.databinding.ActivitySimpleScanBinding
import com.gallery.sample.databinding.ItemSimpleScanBinding
import com.gallery.sample.scan.*
import com.gallery.scan.args.ScanEntityFactory
import com.gallery.scan.extensions.*
import com.gallery.scan.impl.file.FileScanArgs
import com.gallery.scan.impl.file.fileExpand

class SimpleScanActivity : AppCompatActivity() {

    companion object {
        const val args = "args"
    }

    private val arrayList: ArrayList<SimpleEntity> = arrayListOf()
    private val viewBinding: ActivitySimpleScanBinding by lazy { ActivitySimpleScanBinding.inflate(layoutInflater) }

    class ViewHolder(val viewBinding: ItemSimpleScanBinding) : RecyclerView.ViewHolder(viewBinding.root)

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val scanType = intent.getSerializableExtra(args) as ScanType
        viewBinding.recyclerview.adapter = object : RecyclerView.Adapter<ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(ItemSimpleScanBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val simpleEntity = arrayList[position]
                holder.viewBinding.text.text = "id:${simpleEntity.id} \nname:${simpleEntity.name} \nmediaType:${simpleEntity.mediaType}"
            }

            override fun getItemCount(): Int = arrayList.size
        }

        when (scanType) {
            ScanType.FILE -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = FileScanArgs(null)))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        viewBinding.recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.MIX -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = FileScanArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(), MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO.toString()))))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        viewBinding.recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            ScanType.AUDIO -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.audioExpand(),
                    args = ScanAudioArgs()))
                    .scanAudioImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "音频") })
                        viewBinding.recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(Bundle())
            ScanType.PICTURE -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.pictureExpand(),
                    args = ScanPictureArgs()))
                    .scanPictureImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, "图片") })
                        viewBinding.recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
            // old
            ScanType.VIDEO -> ViewModelProvider(this, scanViewModelFactory(
                    factory = ScanEntityFactory.fileExpand(),
                    args = FileScanArgs(arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()))))
                    .scanFileImpl()
                    .registerLiveData(this) { result ->
                        arrayList.addAll(result.multipleValue.map { SimpleEntity(it.id.toString(), it.displayName, it.mediaType) })
                        viewBinding.recyclerview.adapter?.notifyDataSetChanged()
                    }.scanMultiple(com.gallery.scan.types.ScanType.SCAN_ALL.multipleScanExpand())
        }
    }

    class SimpleEntity(val id: String, val name: String, val mediaType: String)

}