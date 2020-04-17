
## Prepare for the next version

1. `UI`某些回调需要处理

2. 图片加载框架需要更新

3. `ext`需要更新

4.  `core`需要再优化

## version

scan:![](https://api.bintray.com/packages/ydevelop/maven/gallery.scan/images/download.svg)

core:![](https://api.bintray.com/packages/ydevelop/maven/gallery.core/images/download.svg)

ui:![](https://api.bintray.com/packages/ydevelop/maven/gallery.ui/images/download.svg)

glide:![](https://api.bintray.com/packages/ydevelop/maven/gallery.glide/images/download.svg)

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

    implementation 'com.ydevelop:gallery.ui:0.0.2'
    implementation 'com.ydevelop:gallery.glide:0.0.2'
  
#### demo

    Gallery
            .instance
            .apply {
            // gallery config
            }.ui(this)
            
## ImageLoader

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