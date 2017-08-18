# Album
android album




## sample

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

    compile "com.android.support:recyclerview-v7:$supportLibraryVersion"
    compile "com.github.yalantis:ucrop:$ucropVersion"
    compile "com.github.bumptech.glide:glide:$glideVersion"
  
  
#### sampleDemo

        Album
                .getInstance()
                .setAlbumModels(new ArrayList<AlbumModel>())
                .setOptions(new UCrop.Options())
                .setAlbumImageLoader(new SimpleAlbumImageLoader())
                .setConfig(new AlbumConfig())
                .setAlbumListener(new SimpleAlbumListener())
                .start(this);

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
    
#### ucrop

    -dontwarn com.yalantis.ucrop**
    -keep class com.yalantis.ucrop** { *; }
    -keep interface com.yalantis.ucrop** { *; }
    
 
## Thanks

[TouchImageView](https://github.com/MikeOrtiz/TouchImageView)
    
    
## MediaScannerConnection Memory leak

 * https://issuetracker.google.com/issues/37046656
 * https://github.com/square/leakcanary/issues/26


## LICENSE

    Mozilla Public License 2.0
