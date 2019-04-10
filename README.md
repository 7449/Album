# Album
android album

Chinese : [wiki](https://github.com/7449/Album/wiki)

## version

core:![](https://api.bintray.com/packages/ydevelop/maven/album.core/images/download.svg)
library:![](https://api.bintray.com/packages/ydevelop/maven/album/images/download.svg)
ui:![](https://api.bintray.com/packages/ydevelop/maven/album.ui/images/download.svg)

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
            
#### dependencies

     implementation "androidx.appcompat:appcompat:$appcompatVersion"
     implementation "androidx.viewpager2:viewpager2:$viewpagerVersion"
     implementation "com.ydevelop:album.ui:version"
     implementation "com.github.yalantis:ucrop:$ucropVersion"
     implementation "com.github.bumptech.glide:glide:$glideVersion"
     implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
  
#### demo

    Album
            .instance
            .apply {
            // album config
            }.start(this)
            
## custom ui

[CustomWeChat](https://github.com/android-develop-team/Album/tree/master/UIWeChat)

[CustomDialog](https://github.com/android-develop-team/Album/tree/master/UIDialog)

     implementation "com.ydevelop:album:version"
              
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

## Screenshot

#### multiple, radio, preview, crop, sample ui,customize camera

![](https://github.com/7449/Album/blob/master/screenshot/album_multiple.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_radio.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_preview.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_crop.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_sample_ui.png)
![](https://github.com/7449/Album/blob/master/screenshot/album_customize_camera.png)

## LICENSE

    Mozilla Public License 2.0
