## version

core:![](https://api.bintray.com/packages/ydevelop/maven/album.core/images/download.svg)

library:![](https://api.bintray.com/packages/ydevelop/maven/album/images/download.svg)

ui:![](https://api.bintray.com/packages/ydevelop/maven/album.ui/images/download.svg)


#### next version

delete path attribute

#### Manifests.xml

        <activity
            android:name="com.yalantis.ucrop.UCropActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.gallery.ui.activity.GalleryActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.gallery.ui.activity.PreActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
            
#### dependencies

     implementation "androidx.appcompat:appcompat:$appcompatVersion"
     implementation "androidx.viewpager2:viewpager2:$viewpagerVersion"
     implementation "com.ydevelop:album.ui:version"
     implementation "com.github.yalantis:ucrop:$ucropVersion"
     implementation "com.github.bumptech.glide:glide:$glideVersion"
     implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
  
#### demo

    Gallery
            .instance
            .apply {
            // gallery config
            }.ui(this)
            
## customize camera

> picture folder can not exist `.nomedia`, otherwise the picture is not scanned

[CustomizeCamera](https://github.com/7449/Album/blob/master/app/src/main/java/com/gallery/sample/camera)

     Gallery
        .instance
        .apply {
            customCameraListener = {

            }
        }.ui(this)


    finishCamera(SimpleCameraActivity.this, cameraFile.path);
    
    fun finishCamera(activity: Activity, path: String) {
        val bundle = Bundle()
        bundle.putString(GalleryConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path)
        val intent = Intent()
        intent.putExtras(bundle)
        activity.setResult(RESULT_OK, intent)
        activity.finish()
    }
                
## ImageLoader

[SimpleGlideImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/gallery/sample/imageloader/SimpleGlideImageLoader.kt)

[SimplePicassoGalleryImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/gallery/sample/imageloader/SimplePicassoGalleryImageLoader.kt)

[SimpleSubsamplingScaleImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/gallery/sample/imageloader/SimpleSubsamplingScaleImageLoader.kt)


    class SimpleImageLoader : GalleryImageLoader {
    
        override fun displayGallery(view: ImageView, width: Int, height: Int, galleryEntity: ScanEntity) : View{
    
        }
    
        override fun displayGalleryThumbnails(view: ImageView, finderEntity: FinderEntity) : View{
    
        }
    
        override fun displayPreview(view: ImageView, galleryEntity: ScanEntity) : View{
    
        }
    }

## MediaScannerConnection Memory leak

 * https://issuetracker.google.com/issues/37046656
 * https://github.com/square/leakcanary/issues/26

## Screenshot

![](https://github.com/7449/Album/blob/master/screenshot/gallery_multiple.png)
![](https://github.com/7449/Album/blob/master/screenshot/gallery_radio.png)
![](https://github.com/7449/Album/blob/master/screenshot/gallery_preview.png)
![](https://github.com/7449/Album/blob/master/screenshot/gallery_crop.png)
![](https://github.com/7449/Album/blob/master/screenshot/gallery_sample_ui.png)
![](https://github.com/7449/Album/blob/master/screenshot/gallery_customize_camera.png)