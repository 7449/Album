package develop.file.gallery.crop

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import develop.file.gallery.args.GalleryConfigs
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.extensions.ContextCompat.takeCropUri

interface ICrop {

    val cropImpl: ICrop?
        get() = null

    fun cropOutPutUri(context: Context, configs: GalleryConfigs): Uri? {
        return context.takeCropUri(configs)
    }

    fun openCrop(context: Context, configs: GalleryConfigs, inputUri: Uri): Intent {
        TODO("cropping has not been initialized")
    }

    /**
     * 裁剪回调
     * [ActivityResult.mResultCode]
     * 以及
     * [ActivityResult.mData]
     * 裁剪完成之后需要[IScanDelegate.onScanResult]来进行数据刷新
     */
    fun onCropResult(delegate: IScanDelegate, intent: ActivityResult) {
        TODO("need to handle crop callback")
    }

}