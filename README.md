## Prepare for the next version

1. custom camera

## 

    android:requestLegacyExternalStorage="true"
    为了适配UCrop,暂时使用这个方法,可在下一个版本撤回这个方法
    https://github.com/Yalantis/uCrop/pull/613

## version

scan:![](https://api.bintray.com/packages/ydevelop/maven/gallery.scan/images/download.svg)

core:![](https://api.bintray.com/packages/ydevelop/maven/gallery.core/images/download.svg)

ui:![](https://api.bintray.com/packages/ydevelop/maven/gallery.ui/images/download.svg)

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

    implementation 'com.ydevelop:gallery.ui:0.0.4'
    implementation "com.github.yalantis:ucrop:$ucropVersion"
    implementation "androidx.viewpager2:viewpager2:$viewpagerVersion"
    implementation "com.github.bumptech.glide:glide:$glideVersion"
    
#### demo

    private val galleryLauncher: ActivityResultLauncher<Intent> =
            registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult(), 
                    GalleryResultCallback(this, SimpleGalleryCallback())
            )

    Gallery(
            activity = FragmentActivity,
            galleryLauncher = galleryLauncher,
            // 可选，自定义UI
            clz = SimpleGalleryActivity::class.java,
            galleryBundle = GalleryBundle(),
            galleryUiBundle = GalleryUiBundle()
    )
    
    
#### custom page

  [SimpleGalleryActivity](./app/src/main/java/com/gallery/sample/SimpleGalleryActivity.kt)

## Screenshot

| 黑色                                   | 主题色                                | 蓝色                              | 粉色                                  | 默认
| :----:                                | :----:                                |:----:                             | :----:                               | :----: 
| ![](./screenshot/gallery_black.png)   | ![](./screenshot/gallery_app.png)    | ![](./screenshot/gallery_blue.png) | ![](./screenshot/gallery_pink.png)   | ![](./screenshot/gallery_default.png)  

| 嵌套                                        | 自定义布局                                | Dialog                                | 预览                                  
| :----:                                      | :----:                                  |:----:                                 | :----:                               
| ![](./screenshot/gallery_combination.png)   | ![](./screenshot/gallery_banner.png)    | ![](./screenshot/gallery_dialog.png)  | ![](./screenshot/gallery_preview.png)  