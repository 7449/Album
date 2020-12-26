plugins {
    id(Plugin.application)
    kotlin(Plugin.kotlin_android)
    kotlin(Plugin.kotlin_ext)
}
android {
    compileSdkVersion(Version.compileSdk)
    defaultConfig {
        applicationId = Version.applicationId
        minSdkVersion(Version.minSdk)
        targetSdkVersion(Version.targetSdk)
        versionCode = Version.versionCode
        versionName = Version.versionName
        vectorDrawables.useSupportLibrary = true
    }
    androidExtensions { isExperimental = true }
    buildFeatures.viewBinding = true
}
dependencies {
    api(project(":wechat"))

    implementation(Dep.glide)
    implementation(Dep.material)
    implementation(Dep.cropper)
    implementation(Dep.uCrop)
    implementation(Dep.cameraview)

    implementation(Dep.kotlin)

    implementation(Dep.appcompat)
    implementation(Dep.fragment)
    implementation(Dep.viewPager2)
    implementation(Dep.recyclerView)
}