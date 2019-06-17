## version

core:![](https://api.bintray.com/packages/ydevelop/maven/album.core/images/download.svg)

library:![](https://api.bintray.com/packages/ydevelop/maven/album/images/download.svg)

ui:![](https://api.bintray.com/packages/ydevelop/maven/album.ui/images/download.svg)

## TODO(需要修改源码)

为了简单处理,回调最终使用单例,这样确实很方便,但是带来了几个小问题

* 内存泄露

    这个很简单就能处理，在页面销毁的时候重新设置为`null`即可
    
* 内存回收空指针异常

    这个问题一般不会出现,当用户进入图片选择界面时按`Home`返回桌面,长时间不使用导致系统回收,有一定可能会造成空指针
    `kotlin`很好的避免了报错,但是会没有任何提示

既然目前的问题是单例引起的那么只需要去除掉单例即可

核心`library`已经改成了`fragment`，具体可参考`ui library`如何去自定义一个页面,
自定义之后`fragment`和`activity`之间的通信想必也会很容易就会实现，如果是自己用,直接修改即可,
如果需要后期维护建议在`onActivityResult`返回不同的状态码,然后利用`kotlin`的扩展属性自行扩展一个方法,直接使用即可,
如果回调过多,可直接在方法中插入一个`callback`,简单处理回调即可

#### 为什么回调不使用`rxbus`或者`eventbus`

不想引入过多的库,如果只是简单的实现回调,没有必要

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
            }.ui(this)
            
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
            customCameraListener = {

            }
        }.ui(this)


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
