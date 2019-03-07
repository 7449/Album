# Album
android album

Chinese : [wiki](https://github.com/7449/Album/wiki)

## Screenshot

#### multiple, radio, preview, crop, sample ui,customize camera

![](https://github.com/7449/Album/blob/master/screenshot/album_multiple.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_radio.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_preview.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_crop.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_sample_ui.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_customize_camera.png)

## sample

#### Manifests.xml

        <activity
            android:name="com.yalantis.ucrop.UCropActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.AlbumActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.PreviewActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
            
#### gradle

     implementation 'com.ydevelop:album:album:0.0.7'
     implementation "com.ydevelop:album.ui:beta02"
     implementation "com.android.support:recyclerview-v7:$supportLibraryVersion"
     implementation "com.github.bumptech.glide:glide:$glideVersion"
  
  If you use the built-in frame, please rely on glide
  
    glide
    
#### demo-kotlin

    Album
            .instance
            .apply {
            // album config
            }.start(this)
              
## customize camera

> picture folder can not exist `.nomedia`, otherwise the picture is not scanned

[CustomizeCamera](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/camera)

     Album
        .instance
        .apply {
            customCameraListener = object : AlbumCameraListener {
                override fun startCamera(fragment: AlbumBaseFragment) {
                }
            }
        }.start(this)


    finishCamera(SimpleCameraActivity.this, cameraFile.path);
    
    fun finishCamera(activity: Activity, path: String) {
        val bundle = Bundle()
        bundle.putString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path)
        val intent = Intent()
        intent.putExtras(bundle)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }
                
## ImageLoader

[SimpleFrescoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleFrescoAlbumImageLoader.kt)

[SimpleImageLoaderAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleImageLoaderAlbumImageLoader.kt)

[SimplePicassoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimplePicassoAlbumImageLoader.kt)


    class SimpleImageLoader : AlbumImageLoader {
    
        override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) : View{
    
        }
    
        override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) : View{
    
        }
    
        override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) : View{
    
        }
    }


## UI

#### album

see: [SimpleAlbumUI](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/ui/SimpleAlbumUI.kt)

#### preview

see: [SimpleAlbumUI](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/ui/SimplePreviewUI.kt)

## Listener

see: [SimpleAlbumListener](https://github.com/7449/Album/blob/master/AlbumLibrary/src/main/java/com/album/Listener.kt)

    open class SimpleAlbumListener : AlbumListener {
        override fun onAlbumActivityFinish() {}
        override fun onAlbumResultCameraError() {}
        override fun onAlbumPermissionsDenied(type: Int) {}
        override fun onAlbumPreviewFileNotExist() {}
        override fun onAlbumFinderEmpty() {}
        override fun onAlbumPreviewEmpty() {}
        override fun onAlbumSelectEmpty() {}
        override fun onAlbumFileNotExist() {}
        override fun onAlbumPreviewSelectEmpty() {}
        override fun onAlbumCheckFileNotExist() {}
        override fun onAlbumCropCanceled() {}
        override fun onAlbumCameraCanceled() {}
        override fun onAlbumUCropError(data: Throwable?) {}
        override fun onAlbumResources(list: List<AlbumEntity>) {}
        override fun onAlbumUCropResources(scannerFile: File) {}
        override fun onAlbumMaxCount() {}
        override fun onAlbumActivityBackPressed() {}
        override fun onAlbumOpenCameraError() {}
        override fun onAlbumEmpty() {}
        override fun onAlbumNoMore() {}
        override fun onVideoPlayError() {}
        override fun onCheckBoxAlbum(count: Int, maxCount: Int) {}
    }

## TestPhone

* onePlus3T           7.1.1
* huawei ale-cl00        4.4.4
* meizu mx5         5.1
* galaxy S8+         7.0
* hanzhong       5.1
* xiaomi note        6.0.1
* redmi note4 6.0
* oppo R7c      4.4.4
* Lenovo K30-T  4.4.4

## ProGuard

    -dontwarn com.album.**
    -keep class  com.album.** { *;}
    -keep interface  com.album.** { *;}
    
#### ucrop

    -dontwarn com.yalantis.ucrop**
    -keep class com.yalantis.ucrop** { *; }
    -keep interface com.yalantis.ucrop** { *; }
    
## Thanks

[cameraview](https://github.com/google/cameraview)
    
## MediaScannerConnection Memory leak

 * https://issuetracker.google.com/issues/37046656
 * https://github.com/square/leakcanary/issues/26

## LICENSE

    Mozilla Public License 2.0
