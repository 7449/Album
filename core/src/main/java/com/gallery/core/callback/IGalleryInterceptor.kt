package com.gallery.core.callback

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.FragmentActivity
import androidx.kotlin.expand.text.toastExpand
import com.gallery.core.GalleryBundle
import com.gallery.core.R
import com.gallery.core.ext.cropPathToUri
import com.gallery.core.ui.base.GalleryBaseActivity
import com.gallery.core.ui.fragment.ScanFragment
import com.yalantis.ucrop.UCrop

/**
 * [ScanFragment] 拦截器
 */
interface IGalleryInterceptor {

    /**
     * 自定义相机
     *
     * true 自定义相机，否则打开系统相机
     *
     * 如果自定义相机继承[GalleryBaseActivity]是比较简单的一种方法
     *
     * 简单示例
     *
     * [uri]返回预创建Uri,如果自定义相机这里需要处理两种情况
     *
     * [FragmentActivity.RESULT_OK]
     * [FragmentActivity.RESULT_CANCELED]
     *
     * [uri]的格式一直都是
     *
     * content://media/external/images/media/id
     *
     * 这里的resultCode可自定义,但是回调自行调用
     * [ScanFragment.onCameraResultCanceled]
     * [ScanFragment.onCameraResultOk]
     *
     * 这里需要注意的是[ScanFragment.onCameraResultOk]不需要任何参数,只需要拍照成功之后
     * 手动调用刷新图库即可，因此自定义返回的[Uri]则在自定义的时候非常重要,需要传递过去之后
     * 把拍照的数据写入到这个[uri]中
     *
     * class CustomCameraActivity : GalleryActivity() {
     *
     *     private val cameraLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
     *         when (intent.resultCode) {
     *             Activity.RESULT_CANCELED -> galleryFragment.onCameraResultCanceled()
     *             Activity.RESULT_OK -> galleryFragment.onCameraResultOk()
     *         }
     *     }
     *
     *     override fun onCustomCamera(uri: Uri): Boolean {
     *         cameraLauncher.launch(Intent(this, CameraActivity::class.java).apply {
     *             putExtras(Bundle().apply {
     *                 this.putParcelable(GalleryConfig.CUSTOM_CAMERA_OUT_PUT_URI, uri)
     *             })
     *         })
     *         return true
     *     }
     * }
     *
     * 这里以 com.otaliastudios:cameraview 为例
     *
     * 取消非常简单
     *
     *    override fun onBackPressed() {
     *         setResult(Activity.RESULT_CANCELED)
     *         super.onBackPressed()
     *     }
     *
     * 拍照成功之后的简单示例
     *
     * override fun onPictureTaken(result: PictureResult) {
     *     super.onPictureTaken(result)
     *     val fileUri: Uri = intent.extras?.getParcelable<Uri>(GalleryConfig.CUSTOM_CAMERA_OUT_PUT_URI).orEmptyExpand()
     *     if (hasQExpand()) {
     *         result.toFile(File(externalCacheDir, "${System.currentTimeMillis()}.jpg")) {
     *             val saveCropToGalleryLegacy = saveToGalleryLegacy(
     *                     Uri.fromFile(it),
     *                     fileUri,
     *                     it?.name.toString(),
     *                     "jpg",
     *                     Environment.DIRECTORY_DCIM
     *             )
     *             message(saveCropToGalleryLegacy?.path ?: it?.path.toString(), false)
     *             it?.delete()
     *             setResult(Activity.RESULT_OK)
     *             finish()
     *         }
     *     } else {
     *         result.toFile(File(findPathByUriExpand(fileUri).toString())) {
     *             message(it?.path.toString(), false)
     *             setResult(Activity.RESULT_OK)
     *             finish()
     *         }
     *     }
     * }
     *
     * Android 10 的读写操作具体可以看下这篇文章
     *
     * [Android Q 存储变化的适配](https://7449.github.io/2020/05/17/android_q_write_and_uri.html)
     *
     */
    fun onCustomCamera(uri: Uri): Boolean = false

    /**
     * 自定义图片裁剪
     *
     * 需要注意的是[cropPathToUri]返回的格式为
     *
     * file:///path/xxxxx.jpg
     *
     * 在低版本中图库的文件如果存储正常或者设置自定义目录,那么裁剪的文件会出现在该目录中
     * 在q中默认的路径为[Context.getExternalCacheDir],如果希望裁剪的文件出现在图库中
     * 设置[GalleryBundle.cropSuccessSave]为true即可,那么裁剪成功后会主动将缓存的裁剪文件
     * 重新设置到对应的裁剪路径,详见[GalleryBundle.cropPath]
     *
     * 库里自带uCrop,如果不喜欢则可以替换为自己喜欢的裁剪库,自定义裁剪和相机有点不同,
     * 裁剪这里使用预设好的[ActivityResultLauncher]只需要裁剪提供裁剪的[Uri]和裁剪错误
     * 的 resultCode 和 异常信息[Throwable](可为空)
     *
     * 已经为uCrop设置好了各种参数
     *
     * [onCropSuccessUriRule]
     * [onCropErrorResultCode]
     * [onCropErrorThrowable]
     *
     * 这里以 com.theartofdev.edmodo:android-image-cropper 为例
     *
     * 至于裁剪的OutputUri则使用 [cropPathToUri] 即可获取到正确的[Uri]
     *
     * override fun onCustomPhotoCrop(activity: FragmentActivity, uri: Uri, galleryBundle: GalleryBundle): Intent {
     *     return CropImage
     *             .activity(uri)
     *             .setOutputUri(cropPathToUri())
     *             .getIntent(this)
     * }
     *
     * override fun onCropSuccessUriRule(intent: Intent?): Uri? {
     *     return CropImage.getActivityResult(intent).uri
     * }
     *
     * override fun onCropErrorResultCode(): Int {
     *     return CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE
     * }
     *
     * override fun onCropErrorThrowable(intent: Intent?): Throwable? {
     *     return CropImage.getActivityResult(intent).error
     * }
     *
     */
    fun onCustomPhotoCrop(activity: FragmentActivity, uri: Uri, galleryBundle: GalleryBundle): Intent {
        return UCrop.of(uri, activity.cropPathToUri(galleryBundle))
                .withOptions(UCrop.Options().apply {
                    optionBundle.putAll(onUCropOptions())
                })
                .getIntent(activity)
    }

    /**
     * 裁剪成功返回正确的Uri
     */
    fun onCropSuccessUriRule(intent: Intent?): Uri? {
        return intent?.let { UCrop.getOutput(it) }
    }

    /**
     * 裁剪错误的ResultCode
     */
    fun onCropErrorResultCode(): Int = UCrop.RESULT_ERROR

    /**
     * 裁剪错误的Throwable
     */
    fun onCropErrorThrowable(intent: Intent?): Throwable? {
        return intent?.let { UCrop.getError(it) }
    }

    /**
     * [onCustomPhotoCrop]默认uCrop会触发
     */
    fun onUCropOptions() = Bundle()

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 取消裁剪
     */
    fun onCropCanceled(context: Context) {
        context.getString(R.string.gallery_crop_canceled).toastExpand(context)
    }

    /**
     * 在[onCustomPhotoCrop]为false的情况下会触发
     * 裁剪异常
     */
    fun onCropError(context: Context, throwable: Throwable?) {
        context.getString(R.string.gallery_crop_error).toastExpand(context)
    }

    /**
     * 裁剪成功
     *
     * 需要注意的是Uri分为两种情况
     * Android 10
     *
     *   content://media/external/images/media/id
     *
     *   如果获取的 content uri 为null,则返回
     *   file:///storage/emulated/0/Android/data/packageName/cache/xxxxx.jpg
     *
     * Android 9 及以下
     *
     *   file:///path/xxxxx.jpg
     *
     */
    fun onCropResources(uri: Uri) {
        //crop rewrite this method
    }

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true
}