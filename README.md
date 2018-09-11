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

#### filter damaged pictures

    AlbumConfig().apply {
        filterImg = true
    }

#### Manifests.xml

        <activity
            android:name="com.yalantis.ucrop.UCropActivity"
            android:screenOrientation="portrait"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.AlbumActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
        <activity
            android:name="com.album.ui.activity.PreviewActivity"
            android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
            
#### gradle

     api 'com.ydevelop:album:album:0.0.6'
     api "com.android.support:recyclerview-v7:$supportLibraryVersion"
     api "com.github.bumptech.glide:glide:$glideVersion"
  
  If you use the built-in frame, please rely on glide
  
    glide
    
#### demo-kotlin

    Album
            .instance
            .apply {
            // album config
            }.start(this)
            
#### demo-java

    Album
            .instance
            .setImageLoader()
            .setAlbumListener()
            .setAlbumEntityList(list)
            .setOptions()
            .setOnEmptyClickListener()
            .setConfig()
            .setAlbumCameraListener()
            .setAlbumClass()
            .start(this)
              
## customize camera

> picture folder can not exist `.nomedia`, otherwise the picture is not scanned

[CustomizeCamera](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/camera)

         Album
                .getInstance()
                .setAlbumCameraListener(new AlbumCameraListener() {
                      @Override
                      public void startCamera(@NonNull Fragment fragment) {
                      
                           FragmentActivity activity = fragment.getActivity();
                           Intent intent = new Intent(activity, SimpleCameraActivity.class);
                           // AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE
                           fragment.startActivityForResult(intent, AlbumConstant.CUSTOMIZE_CAMERA_RESULT_CODE);
                      }
                })


    FileUtils.finishCamera(SimpleCameraActivity.this, cameraFile.getPath());
    
    
    public static void finishCamera(Activity activity, String path) {
        Bundle bundle = new Bundle();
        
        //AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path
        bundle.putString(AlbumConstant.CUSTOMIZE_CAMERA_RESULT_PATH_KEY, path);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        activity.setResult(RESULT_OK, intent);
        activity.finish();
    }
                
## ImageLoader

> Fresco

    Album
            .instance
            .apply {
                config = AlbumConfig().apply {
                    isFrescoImageLoader = true
                }
            }.start(this)

[SimpleFrescoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleFrescoAlbumImageLoader.kt)

[SimpleImageLoaderAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimpleImageLoaderAlbumImageLoader.kt)

[SimplePicassoAlbumImageLoader](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/SimplePicassoAlbumImageLoader.kt)


    class SimpleImageLoader : AlbumImageLoader {
    
        override fun displayAlbum(view: ImageView, width: Int, height: Int, albumEntity: AlbumEntity) {
    
        }
    
        override fun displayAlbumThumbnails(view: ImageView, finderEntity: FinderEntity) {
    
        }
    
        override fun displayPreview(view: ImageView, albumEntity: AlbumEntity) {
    
        }
    
        override fun frescoView(context: Context, type: Int): ImageView? {
            return null
        }
    }


## UI

#### album

see: [SimpleAlbumUI](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/ui/SimpleAlbumUI.kt)

#### preview

see: [SimpleAlbumUI](https://github.com/7449/Album/blob/master/app/src/main/java/com/album/sample/ui/SimplePreviewUI.kt)

## Listener

see: [SimpleAlbumListener](https://github.com/7449/Album/blob/master/AlbumLibrary/src/main/java/com/album/ui/widget/SimpleAlbumListener.kt)

    class SimpleAlbumListener : AlbumListener {
    
        override fun onAlbumActivityFinish() {
    
        }
    
        override fun onAlbumPermissionsDenied(type: Int) {
    
        }
    
        override fun onAlbumFragmentNull() {
    
        }
    
        override fun onAlbumPreviewFileNull() {
    
        }
    
        override fun onAlbumFinderNull() {
    
        }
    
        override fun onAlbumBottomPreviewNull() {
    
        }
    
        override fun onAlbumBottomSelectNull() {
    
        }
    
        override fun onAlbumFragmentFileNull() {
    
        }
    
        override fun onAlbumPreviewSelectNull() {
    
        }
    
        override fun onAlbumCheckBoxFileNull() {
    
        }
    
        override fun onAlbumFragmentCropCanceled() {
    
        }
    
        override fun onAlbumFragmentCameraCanceled() {
    
        }
    
        override fun onAlbumFragmentUCropError(data: Throwable?) {
    
        }
    
        override fun onAlbumResources(list: List<AlbumEntity>) {
    
        }
    
        override fun onAlbumUCropResources(scannerFile: File) {
    
        }
    
        override fun onAlbumMaxCount() {
    
        }
    
        override fun onAlbumActivityBackPressed() {
    
        }
    
        override fun onAlbumOpenCameraError() {
    
        }
    
        override fun onAlbumEmpty() {
    
        }
    
        override fun onAlbumNoMore() {
    
        }
    
        override fun onVideoPlayError() {
        }
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

[TouchImageView](https://github.com/MikeOrtiz/TouchImageView)

[cameraview](https://github.com/google/cameraview)
    
    
## MediaScannerConnection Memory leak

 * https://issuetracker.google.com/issues/37046656
 * https://github.com/square/leakcanary/issues/26


## LICENSE

    Mozilla Public License 2.0




