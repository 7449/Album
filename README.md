## version

	allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
	
[![](https://jitpack.io/v/7449/Album.svg)](https://jitpack.io/#7449/Album)

#### dependencies

    implementation "com.github.7449.Album:wechat:$lastVersion"
    implementation "com.github.7449.Album:ui:$lastVersion"
    implementation "com.github.7449.Album:core:$lastVersion"
    implementation "com.github.7449.Album:scan:$lastVersion"
    
#### simple camera

  [SimpleMaterialGalleryCameraActivity](./sample/src/main/java/com/gallery/sample/camera/SimpleMaterialGalleryCameraActivity.kt)
  
  [SimpleCameraActivity](./sample/src/main/java/com/gallery/sample/camera/SimpleCameraActivity.kt)

#### simple dialog

  [SimpleGalleryDialog](./sample/src/main/java/com/gallery/sample/dialog/SimpleGalleryDialog.kt)

## Screenshot

| 黑色                                   | 主题色                                | 蓝色                              | 粉色                                  | 默认
| :----:                                | :----:                                |:----:                             | :----:                               | :----: 
| ![](./screenshot/gallery_black.webp)   | ![](./screenshot/gallery_app.webp)    | ![](./screenshot/gallery_blue.webp) | ![](./screenshot/gallery_pink.webp)   | ![](./screenshot/gallery_default.webp)  

| 嵌套                                        | 自定义布局                                | Dialog                                | 预览                                  
| :----:                                      | :----:                                  |:----:                                 | :----:                               
| ![](./screenshot/gallery_combination.webp)   | ![](./screenshot/gallery_banner.webp)    | ![](./screenshot/gallery_dialog.webp)  | ![](./screenshot/gallery_preview.webp)  