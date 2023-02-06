package develop.file.gallery.callback

import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import develop.file.gallery.delegate.IScanDelegate
import develop.file.gallery.entity.ScanEntity

interface IGalleryInterceptor {
    /**
     * 自定义相机
     *
     * true 自定义相机，否则打开系统相机
     *
     * [uri]返回预创建Uri,如果自定义相机这里需要处理两种情况
     *
     * [FragmentActivity.RESULT_OK] 以及 [FragmentActivity.RESULT_CANCELED]
     *
     * [uri]的格式一直都是`content://media/external/images/media/id`,只是插入了路径,没有插入其他数据
     *
     * 这里的resultCode可自定义,但是回调自行调用
     *
     * [IScanDelegate.takePictureCanceled] 或者 [IScanDelegate.takePictureSuccess]
     *
     * 这里需要注意的是[IScanDelegate.takePictureSuccess]不需要任何参数,只需要拍照成功之后
     * 手动调用刷新图库即可
     *
     * 因此自定义返回的[Uri]则在自定义相机的时候非常重要,需要传递过去之后
     * 把拍照的数据写入到[uri]中
     *
     * class CustomCameraActivity : GalleryActivity() {
     *
     *     private val cameraLauncher: ActivityResultLauncher<Intent> =
     *                  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { intent ->
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
     * 取消
     *
     *    onBackPressedDispatcher.addCallback(this, true) {
     *        setResult(Activity.RESULT_CANCELED)
     *        finish()
     *    }
     *
     * 拍照成功
     *
     *    override fun onPictureTaken(result: PictureResult) {
     *         super.onPictureTaken(result)
     *         val fileUri: Uri = intent.extras?.getParcelable<Uri>(GalleryConfig.CUSTOM_CAMERA_OUT_PUT_URI).orEmpty()
     *         contentResolver.openOutputStream(fileUri)?.use { it.write(result.data) }
     *         setResult(Activity.RESULT_OK)
     *         finish()
     *    }
     *
     * Android 10 的读写操作具体可以看下这篇文章
     *
     * [Android Q 存储变化的适配](https://7449.github.io/2020/05/17/android_q_write_and_uri.html)
     *
     */
    fun onCustomCamera(uri: Uri): Boolean = false

    /**
     * 无图片或视频时触发,true会自动打开相机
     */
    fun onEmptyPhotoClick(view: View): Boolean = true

    /**
     * 是否拦截视频播放逻辑
     * true 是
     * false 否
     * default:false
     */
    fun onOpenVideo(entity: ScanEntity): Boolean = false
}