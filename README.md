## version

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}

#### Manifests.xml

        <activity
            android:name="com.gallery.ui.activity.GalleryActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.gallery.ui.activity.PreActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
            
#### dependencies

    implementation 'com.github.7449.Album:ui:1.0.1'
    implementation 'com.github.7449.Album:core:1.0.1'
    implementation 'com.github.7449.Album:scan:1.0.1'
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

  [CustomyActivity](./app/src/main/java/com/gallery/sample/custom/CustomPage.kt)
  
#### custom crop

  [ICrop](./core/src/main/java/com/gallery/core/crop/ICrop.kt)
  
  [UCropImpl](./sample/src/main/java/com/gallery/sample/crop/UCropImpl.kt)

#### custom camera

  [CameraActivity](./sample/src/main/java/com/gallery/sample/camera/CameraActivity.kt)

## Screenshot

| 黑色                                   | 主题色                                | 蓝色                              | 粉色                                  | 默认
| :----:                                | :----:                                |:----:                             | :----:                               | :----: 
| ![](./screenshot/gallery_black.png)   | ![](./screenshot/gallery_app.png)    | ![](./screenshot/gallery_blue.png) | ![](./screenshot/gallery_pink.png)   | ![](./screenshot/gallery_default.png)  

| 嵌套                                        | 自定义布局                                | Dialog                                | 预览                                  
| :----:                                      | :----:                                  |:----:                                 | :----:                               
| ![](./screenshot/gallery_combination.png)   | ![](./screenshot/gallery_banner.png)    | ![](./screenshot/gallery_dialog.png)  | ![](./screenshot/gallery_preview.png)  