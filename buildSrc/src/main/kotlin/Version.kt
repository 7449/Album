object Plugin {
    const val application = "com.android.application"
    const val library = "com.android.library"
    const val kotlin_android = "android"
    const val kotlin_parcelize = "kotlin-parcelize"
    const val maven = "com.github.dcendents.android-maven"
}

object Version {
    const val applicationId = "com.gallery.sample"
    const val compileSdk = 30
    const val minSdk = 22
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"
}

object ClassPath {
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32"
    const val maven = "com.github.dcendents:android-maven-gradle-plugin:2.1"
    const val gradle = "com.android.tools.build:gradle:4.1.3"
}

object Maven {
    const val jitpack = "https://jitpack.io"
}

object Dep {
    const val scan = "com.github.7449.Album:scan:1.0.0"
    const val core = "com.github.7449.Album:core:1.0.0"
    const val ui = "com.github.7449.Album:ui:1.0.0"

    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.4.32"

    const val appcompat = "androidx.appcompat:appcompat:1.3.0-beta01"
    const val fragment = "androidx.fragment:fragment:1.3.2"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.1.0-alpha01"
    const val material = "com.google.android.material:material:1.3.0"

    const val cameraview = "com.otaliastudios:cameraview:2.7.0"
    const val cropper = "com.theartofdev.edmodo:android-image-cropper:2.8.0"
    const val glide = "com.github.bumptech.glide:glide:4.12.0"
    const val uCrop = "com.github.yalantis:ucrop:2.2.6"
}