@file:Suppress("MemberVisibilityCanBePrivate")

object Plugin {
    const val androidVersion = "7.4.0"
    const val kotlinVersion = "1.7.20"
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin_android = "android"
    const val kotlin_parcelize = "kotlin-parcelize"
}

object Version {
    const val applicationId = "com.gallery.sample"
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0"
}

object Args {
    const val moduleName = "-module-name"
    const val prefix = "com.github.7449.album."
}

object Dep {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Plugin.kotlinVersion}"

    const val compat = "com.github.7449.Album:compat:v1.1.1"
    const val core = "com.github.7449.Album:core:v1.1.1"
    const val scan = "com.github.7449.Album:scan:v1.1.1"
    const val material_ui = "com.github.7449.Album:material:v1.1.1"
    const val wechat_ui = "com.github.7449.Album:wechat:v1.1.1"

    const val appcompat = "androidx.appcompat:appcompat:1.6.0"
    const val fragment = "androidx.fragment:fragment:1.5.5"
    const val activity = "androidx.activity:activity-ktx:1.6.1"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.1.0-beta01"
    const val material = "com.google.android.material:material:1.8.0"

    const val cameraview = "com.otaliastudios:cameraview:2.7.2"
    const val cropper = "com.theartofdev.edmodo:android-image-cropper:2.8.0"
    const val glide = "com.github.bumptech.glide:glide:4.14.2"

    const val color = "com.github.QuadFlask:colorpicker:0.0.15"
    const val flex = "com.google.android.flexbox:flexbox:3.0.0"
    const val banner = "com.github.7449.BannerLayout:banner:v2.0.3"
    const val ucrop = "com.github.yalantis:ucrop:2.2.6"
}