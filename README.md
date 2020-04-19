## Prepare for the next version

1. `UI`某些回调需要处理

2. `core`需要再优化

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

    implementation 'com.ydevelop:gallery.ui:0.0.2'
    implementation 'com.ydevelop:gallery.glide:0.0.2'
  
#### demo

    Gallery.open(activity)

## Screenshot

| 黑色                                   | 主题色                                | 蓝色                              | 粉色                                  | 默认
| :----:                                | :----:                                |:----:                             | :----:                               | :----: 
| ![](./screenshot/gallery_black.png)   | ![](./screenshot/gallery_app.png)    | ![](./screenshot/gallery_blue.png) | ![](./screenshot/gallery_pink.png)   | ![](./screenshot/gallery_default.png)  

| 嵌套                                        | 自定义布局                                | Dialog                                | 预览                                  
| :----:                                      | :----:                                  |:----:                                 | :----:                               
| ![](./screenshot/gallery_combination.png)   | ![](./screenshot/gallery_banner.png)    | ![](./screenshot/gallery_dialog.png)  | ![](./screenshot/gallery_preview.png)  