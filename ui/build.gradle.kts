plugins {
    id(Plugin.library)
    kotlin(Plugin.kotlin_android)
    id(Plugin.kotlin_parcelize)
}
apply(from = "../gradle/UPLOAD.gradle")
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
    }
    buildFeatures.viewBinding = true
    compileOptions { kotlinOptions.freeCompilerArgs += listOf("-module-name", "com.ydevelop.gallery.ui") }
}
dependencies {
//    api(project(":core"))
    api(Dep.core)

    compileOnly(Dep.glide)
    compileOnly(Dep.cropper)

    /* 和 core library 依赖保持一致 */
    compileOnly(Dep.appcompat)
    compileOnly(Dep.fragment)
    compileOnly(Dep.viewPager2)
    compileOnly(Dep.recyclerView)
    compileOnly(Dep.kotlin)
}