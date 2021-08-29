@file:Suppress("MemberVisibilityCanBePrivate")

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
    const val minSdk = 21
    const val targetSdk = 30
    const val versionCode = 1
    const val versionName = "1.0"
}

object Args {
    const val moduleName = "-module-name"
    const val prefix = "com.github.7449.album."
}

object ClassPath {
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30"
    const val maven = "com.github.dcendents:android-maven-gradle-plugin:2.1"

    // old version : ignore
    // new version : 7.0.1 and https://services.gradle.org/distributions/gradle-7.0.2-bin.zip
    const val gradle = "com.android.tools.build:gradle:4.2.1"
}

object Maven {
    const val jitpack = "https://jitpack.io"
}

object Dep {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:1.5.30"

    const val appcompat = "androidx.appcompat:appcompat:1.3.1"
    const val fragment = "androidx.fragment:fragment:1.3.6"
    const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.1.0-beta01"
    const val material = "com.google.android.material:material:1.4.0"

    const val cameraview = "com.otaliastudios:cameraview:2.7.1"
    const val cropper = "com.theartofdev.edmodo:android-image-cropper:2.8.0"
    const val glide = "com.github.bumptech.glide:glide:4.12.0"
}

object DepList {
    /** scan dependencies */
    val scan = mutableListOf(Dep.fragment, Dep.kotlin)

    /** core dependencies */
    val core = scan
        .plus(Dep.appcompat)
        .plus(Dep.viewPager2)
        .plus(Dep.recyclerView)

    /** compat dependencies */
    val compat = core

    /** material dependencies */
    val material = compat
        .plus(Dep.glide)
        .plus(Dep.cropper)

    /** wechat dependencies */
    val wechat = compat
        .plus(Dep.glide)

    /** qq dependencies */
    val qq = compat
        .plus(Dep.glide)

    /** zhihu dependencies */
    val zhihu = compat
        .plus(Dep.glide)

    /** sample dependencies */
    val sample = (scan + core + material + wechat + qq + zhihu)
        .plus(Dep.material)
        .plus(Dep.cameraview)
}

object DepLib {
    /** core dependencies */
    val core = mutableListOf(":scan")

    /** compat dependencies */
    val compat = core.plus(":core")

    /** material dependencies */
    val material = compat.plus(":compat")

    /** wechat dependencies */
    val wechat = compat.plus(":compat")

    /** qq dependencies */
    val qq = compat.plus(":compat")

    /** zhihu dependencies */
    val zhihu = compat.plus(":compat")

    /** sample dependencies */
    val sample = (wechat + material + qq + zhihu)
        .plus(":qq")
        .plus(":zhihu")
        .plus(":wechat")
        .plus(":material")
}